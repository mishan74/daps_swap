package io.lastwill.eventscan.services.senders;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import io.lastwill.eventscan.model.CryptoCurrency;
import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import io.lastwill.eventscan.model.TransferStatus;
import io.lastwill.eventscan.repositories.EthToDapsTransitionEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Component
public class DapsSender implements Sender {
    private static final BigInteger TRANSFER_FEE = BigInteger.valueOf(37500); // todo: set real

    @Autowired
    private EthToDapsTransitionEntryRepository transitionRepository;

    @Autowired
    private BtcdClient dapsClient;

    @Override
    public void send(EthToDapsTransitionEntry transitionEntry) {
        if (!checkTransition(transitionEntry)) return;

        String address = transitionEntry.getConnectEntry().getDapsAddress();
        BigDecimal amount = new BigDecimal(transitionEntry.getAmount(), CryptoCurrency.DAPS.getDecimals());
        try {
            String txHash = dapsClient.sendToStealthAddress(address, amount);
            transitionEntry.setTransferStatus(TransferStatus.WAIT_FOR_CONFIRM);
            transitionEntry.setDapsTxHash(txHash);
            log.info("DAPS tokens transferred: {}", txHash);
        } catch (BitcoindException | CommunicationException e) {
            transitionEntry.setTransferStatus(TransferStatus.ERROR);
            log.warn("Bitcoind library exception when sending", e);
        }

        transitionRepository.save(transitionEntry);
    }

    private boolean checkTransition(EthToDapsTransitionEntry transitionEntry) {
        if (transitionEntry.getConnectEntry() == null) {
            return false;
        }

        if (transitionEntry.getAmount().equals(BigInteger.ZERO)) {
            log.warn("Zero transition amount");
            return false;
        }

        BigInteger balance;
        try {
            balance = getBalance();
        } catch (BitcoindException | CommunicationException e) {
            log.warn("Bitcoind library exception when getting balance", e);
            return false;
        }

        BigInteger need = transitionEntry.getAmount().add(TRANSFER_FEE);
        if (balance.compareTo(need) < 0) {
            log.warn("Insufficient balance: {}, but needed {}", balance, need);
            return false;
        }

        return true;
    }

    private BigInteger getBalance() throws BitcoindException, CommunicationException {
        return dapsClient.getBalance()
                .multiply(BigDecimal.TEN.pow(8))
                .toBigInteger();
    }
}
