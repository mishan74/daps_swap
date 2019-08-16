package io.lastwill.eventscan.services.monitors.dapsswap;

import io.lastwill.eventscan.events.model.TokensBurnedEvent;
import io.lastwill.eventscan.events.model.contract.erc20.TransferEvent;
import io.lastwill.eventscan.model.CryptoCurrency;
import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.EthToDapsConnectEntryRepository;
import io.lastwill.eventscan.repositories.EthToDapsTransitionEntryRepository;
import io.lastwill.eventscan.services.TransactionProvider;
import io.lastwill.eventscan.utils.CurrencyUtil;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import io.mywish.scanner.services.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;

@Slf4j
@Component
public class BurnMonitor extends AbstractMonitor {
    @Autowired
    private TransactionProvider transactionProvider;

    @Autowired
    private EthToDapsConnectEntryRepository connectRepository;

    @Autowired
    private EthToDapsTransitionEntryRepository transitionRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Value("${io.lastwill.eventscan.daps-transition.burner-address}")
    private String burnerAddress;

    @Value("${io.lastwill.eventscan.daps-transition.token-address}")
    private String ethTokenAddress;

    @Override
    protected boolean checkCondition(NewBlockEvent newBlockEvent) {
        return newBlockEvent.getNetworkType() == NetworkType.ETHEREUM_MAINNET;
    }

    @Override
    protected void processBlockEvent(NewBlockEvent newBlockEvent) {
        filterTransactionsByAddress(newBlockEvent, ethTokenAddress)
                .forEach(transaction -> transactionProvider.getTransactionReceiptAsync(newBlockEvent.getNetworkType(), transaction)
                        .thenAccept(receipt -> receipt.getLogs()
                                .stream()
                                .filter(event -> event instanceof TransferEvent)
                                .map(event -> (TransferEvent) event)
                                .filter(event -> burnerAddress.equalsIgnoreCase(event.getTo()))
                                .map(event -> getTransitionEntry(event, transaction))
                                .filter(Objects::nonNull)
                                .map(transitionRepository::save)
                                .peek(transitionEntry -> log.info(
                                        "{} burned {} {}",
                                        transitionEntry.getConnectEntry().getEthAddress(),
                                        CurrencyUtil.toString(transitionEntry.getAmount()),
                                        CryptoCurrency.ETH_DAPS)
                                )
                                .map(TokensBurnedEvent::new)
                                .forEach(eventPublisher::publish)
                        )
                );
    }

    private EthToDapsTransitionEntry getTransitionEntry(TransferEvent transferEvent, WrapperTransaction transaction) {
        String ethAddress = transferEvent.getFrom().toLowerCase();
        EthToDapsConnectEntry connectEntry = connectRepository.findFirstByEthAddressOrderByIdDesc(ethAddress);
        if (connectEntry == null) {
//            log.warn("\"{}\" not connected", ethAddress);
            return null;
        }

        String txHash = transaction.getHash();
        EthToDapsTransitionEntry transitionEntry = transitionRepository.findByEthTxHash(txHash);
        if (transitionEntry != null) {
            log.warn("Transition entry already in DB: {}", txHash);
            return null;
        }

        BigInteger amount = CurrencyUtil.convertEthToDaps(transferEvent.getTokens());
        return new EthToDapsTransitionEntry(
                connectEntry,
                amount,
                txHash
        );
    }
}
