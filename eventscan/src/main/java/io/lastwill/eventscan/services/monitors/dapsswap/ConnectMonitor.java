package io.lastwill.eventscan.services.monitors.dapsswap;

import io.lastwill.eventscan.events.model.contract.DapsConnectEvent;
import io.lastwill.eventscan.events.model.contract.DapsReconnectEvent;
import io.lastwill.eventscan.model.TransitionProfile;
import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.ProfileStorage;
import io.lastwill.eventscan.repositories.EthToDapsConnectEntryRepository;
import io.lastwill.eventscan.services.TransactionProvider;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ConnectMonitor {
    @Autowired
    private TransactionProvider transactionProvider;

    @Autowired
    private EthToDapsConnectEntryRepository connectRepository;

    @Autowired
    private ProfileStorage profileStorage;

    @EventListener
    public void onConnect(final NewBlockEvent newBlockEvent) {
        if (newBlockEvent.getNetworkType() != NetworkType.ETHEREUM_MAINNET) {
            return;
        }

        Map<String, List<WrapperTransaction>> entries = filterTransactionsByAddress(newBlockEvent);

        if (entries == null || entries.size() == 0) return;

        for (Map.Entry<String, List<WrapperTransaction>> entry : entries.entrySet()) {
            TransitionProfile transitionProfile;
            try {
                transitionProfile = profileStorage.getProfileByEthConnectAddress(entry.getKey());
            } catch (NoSuchElementException ex) {
                log.error(ex.getMessage());
                continue;
            }
            for (WrapperTransaction transaction : entry.getValue()) {
                List<DapsConnectEvent> dapsConnectEvents = new ArrayList<>();
                transactionProvider.getTransactionReceiptAsync(newBlockEvent.getNetworkType(), transaction)
                        .thenAccept(receipt -> {
                            receipt.getLogs()
                                    .stream()
                                    .filter(event -> event instanceof DapsConnectEvent) // todo: + reconnect
                                    .forEach(event -> dapsConnectEvents.add((DapsConnectEvent) event));
                            List<EthToDapsConnectEntry> ethToDapsConnectEntries = getEthToDapsConnectEntries(dapsConnectEvents, transaction, transitionProfile);
                            saveConnectEntries(ethToDapsConnectEntries);
                        });
            }
        }
    }

    private Map<String, List<WrapperTransaction>> filterTransactionsByAddress(NewBlockEvent newBlockEvent) {
        return newBlockEvent.getTransactionsByAddress()
                .entrySet()
                .stream()
                .filter(entry -> profileStorage.getTransitionProfiles()
                        .stream()
                        .map(TransitionProfile::getEthConnectAddress)
                        .collect(Collectors.toList())
                        .contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private List<EthToDapsConnectEntry> getEthToDapsConnectEntries(List<DapsConnectEvent> events, WrapperTransaction transaction, TransitionProfile transitionProfile) {
        return events
                .stream()
                .map(putEvent -> {
                    String eth = putEvent.getEth().toLowerCase();
                    byte[] input = transaction.getOutputs().get(0).getRawOutputScript();
                    byte[] dapsBytes = Arrays.copyOfRange(input, 36, input.length);
                    String daps = new String(dapsBytes).trim();

                    // todo: remove
                    if (connectRepository.existsByEthAddressAndSymbol(eth, transitionProfile.getEth().name())) {
                        log.warn("\"{} : {} - {}\" already connected.", transitionProfile.getEth().name(), eth, daps);
                        return null;
                    }
                    return new EthToDapsConnectEntry(transitionProfile.getEth().name(), eth, daps);
                }).collect(Collectors.toList());
    }

    private void saveConnectEntries(List<EthToDapsConnectEntry> connectEntries) {
        connectEntries
                .stream()
                .filter(Objects::nonNull)
                .map(connectRepository::save)
                .forEach(connectEntry -> log.info("Connected \"{} \"{} : {}\"",
                        connectEntry.getSymbol(), connectEntry.getEthAddress(), connectEntry.getDapsAddress()));
    }
}
