package io.lastwill.eventscan.services.monitors.dapsswap;

import io.lastwill.eventscan.events.model.contract.swap.DapsConnectEvent;
import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.EthToDapsConnectEntryRepository;
import io.lastwill.eventscan.services.TransactionProvider;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class ConnectMonitor extends AbstractMonitor {
    @Autowired
    private TransactionProvider transactionProvider;

    @Autowired
    private EthToDapsConnectEntryRepository connectRepository;

    @Value("${io.lastwill.eventscan.daps-transition.connector-address}")
    private String connectorAddress;

    @Override
    protected boolean checkCondition(NewBlockEvent newBlockEvent) {
        return newBlockEvent.getNetworkType() == NetworkType.ETHEREUM_MAINNET;
    }

    @Override
    protected void processBlockEvent(NewBlockEvent newBlockEvent) {
        filterTransactionsByAddress(newBlockEvent, connectorAddress)
                .forEach(transaction -> transactionProvider.getTransactionReceiptAsync(newBlockEvent.getNetworkType(), transaction)
                        .thenAccept(receipt -> receipt.getLogs()
                                .stream()
                                .filter(event -> event instanceof DapsConnectEvent)
                                .map(event -> (DapsConnectEvent) event)
                                .map(event -> getConnectEntry(event, transaction))
                                .filter(Objects::nonNull)
                                .forEach(connectEntry -> {
                                    connectRepository.save(connectEntry);
                                    log.info("Connected \"{} : {}\"", connectEntry.getEthAddress(), connectEntry.getDapsAddress());
                                })
                        )
                );
    }

    private EthToDapsConnectEntry getConnectEntry(DapsConnectEvent connectEvent, WrapperTransaction transaction) {
        String hash = transaction.getHash().toLowerCase();
        String eth = connectEvent.getEth().toLowerCase();
        byte[] input = transaction.getOutputs().get(0).getRawOutputScript();
        byte[] dapsBytes = Arrays.copyOfRange(input, 68, input.length);
        String daps = new String(dapsBytes).trim();

        if (connectRepository.existsByConnectTxHash(hash)) {
            log.warn("Already connected \"{} - {}\".", eth, daps);
            return null;
        }
        return new EthToDapsConnectEntry(eth, daps, hash);
    }
}
