package io.lastwill.eventscan.services.monitors.dapsswap;

import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.TransferStatus;
import io.lastwill.eventscan.repositories.EthToDapsTransitionEntryRepository;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ConfirmationMonitor extends AbstractMonitor {
    @Autowired
    private EthToDapsTransitionEntryRepository transitionRepository;

    @Override
    protected boolean checkCondition(NewBlockEvent newBlockEvent) {
        return newBlockEvent.getNetworkType() == NetworkType.DAPS_MAINNET;
    }

    @Override
    protected void processBlockEvent(NewBlockEvent newBlockEvent) {
        List<String> txHashes = newBlockEvent
                .getTransactionsByAddress()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .map(WrapperTransaction::getHash)
                .distinct()
                .collect(Collectors.toList());

        transitionRepository
                .findByTransferStatus(TransferStatus.WAIT_FOR_CONFIRM)
                .stream()
                .filter(transitionEntry -> txHashes.contains(transitionEntry.getDapsTxHash()))
                .forEach(transitionEntry -> {
                    transitionEntry.setTransferStatus(TransferStatus.OK);
                    transitionRepository.save(transitionEntry);
                    log.info("Transaction " + transitionEntry.getDapsTxHash() + " confirmed");
                });
    }
}
