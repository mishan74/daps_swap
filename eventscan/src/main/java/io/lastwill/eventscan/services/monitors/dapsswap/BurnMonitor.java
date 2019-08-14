package io.lastwill.eventscan.services.monitors.dapsswap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BurnMonitor {
    /*
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private TransactionProvider transactionProvider;
    @Autowired
    private EthToDapsConnectEntryRepository connectRepository;
    @Autowired
    private EthToDapsSwapEntryRepository swapRepository;
    @Autowired
    private DapsSender dapsSender;

    @Value("${io.lastwill.eventscan.daps-transition.linker-address}")
    private String connectorAddress;
    @Value("${io.lastwill.eventscan.daps-transition.burner-address}")
    private String burnerAddress;
    @Value("${io.lastwill.eventscan.contract.token-address.daps}")
    private String ethTokenAddress;
    @Value("${io.lastwill.eventscan.daps.token-symbol}")
    private String transferSymbol;

    @EventListener
    public void onBurn(final NewBlockEvent newBlockEvent) {
        if (newBlockEvent.getNetworkType() != NetworkType.ETHEREUM_MAINNET) {
            return;
        }
        Map<String, List<WrapperTransaction>> entries = filterTransactionsByTokenAddress(newBlockEvent);

        if (entries == null || entries.size() == 0) return;

        for (Map.Entry<String, List<WrapperTransaction>> entry : entries.entrySet()) {
            TransitionProfile transitionProfile;
            try {
                transitionProfile = profileStorage.getProfileByEthTokenAddress(entry.getKey());
            } catch (NoSuchElementException ex) {
                log.error(ex.getMessage());
                continue;
            }
            for (WrapperTransaction transaction : entry.getValue()) {
                List<TransferEvent> transferEvents = new ArrayList<>();
                transactionProvider.getTransactionReceiptAsync(newBlockEvent.getNetworkType(), transaction)
                        .thenAccept(receipt -> {
                            receipt.getLogs()
                                    .stream()
                                    .filter(event -> event instanceof TransferEvent)
                                    .map(event -> (TransferEvent) event)
                                    .filter(event -> transitionProfile.getEthBurnerAddress().equalsIgnoreCase(event.getTo()))
                                    .forEach(transferEvents::add);
                            List<EthToDapsSwapEntry> swapEntries = publishEvent(transferEvents, transaction, transitionProfile);
                            sendToken(swapEntries, transitionProfile);
                        });
            }
        }
    }

    private Map<String, List<WrapperTransaction>> filterTransactionsByTokenAddress(NewBlockEvent event) {
        return event.getTransactionsByAddress()
                .entrySet()
                .stream()
                .filter(entry -> profileStorage.getTransitionProfiles()
                        .stream()
                        .map(TransitionProfile::getEthTokenAddress)
                        .collect(Collectors.toList())
                        .contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<EthToDapsSwapEntry> publishEvent(List<TransferEvent> events, WrapperTransaction transaction, TransitionProfile profile) {
        return events
                .stream()
                .map(transferEvent -> {
                    String ethAddress = transferEvent.getFrom().toLowerCase();
                    BigInteger amount = convertEthToDaps(transferEvent.getTokens(), profile);
                    String dapsAddress = null;

                    EthToDapsConnectEntry connectEntry = connectRepository.findFirstByEthAddressOrderByIdDesc(ethAddress);
                    if (connectEntry != null) {
                        dapsAddress = connectEntry.getDapsAddress();
                    } else {
                        log.warn("\"{}\" not connected", ethAddress);
                    }

                    EthToDapsSwapEntry swapEntry = swapRepository.findByEthTxHash(transaction.getHash());
                    if (swapEntry != null) {
                        log.warn("Swap entry already in DB: {}", transaction.getHash());
                        return null;
                    }
                    swapEntry = new EthToDapsSwapEntry(
                            connectEntry,
                            amount,
                            transaction.getHash()
                    );
                    swapEntry = swapRepository.save(swapEntry);
                    CryptoCurrency ethDapsCoin = profile.getEth();
                    CryptoCurrency dapsCoin = profile.getDaps();
                    log.info("{} burned {} {}", ethAddress, profile.getSender().toString(amount, dapsCoin.getDecimals()), ethDapsCoin);

                    eventPublisher.publish(new TokensBurnedEvent(
                            ethDapsCoin.name(),
                            dapsCoin.getDecimals(),
                            swapEntry,
                            ethAddress,
                            dapsAddress
                    ));

                    return swapEntry;
                }).collect(Collectors.toList());
    }

    private void sendToken(List<EthToDapsSwapEntry> swapEntries, TransitionProfile profile) {
        swapEntries
                .stream()
                .filter(Objects::nonNull)
                .forEach(profile.getSender()::send);
    }

    private BigInteger convertEthToDaps(BigInteger amount, TransitionProfile profile) {
        int ethDapsDecimals = profile.getEth().getDecimals();
        int dapsDecimals = profile.getDaps().getDecimals();

        return amount
                .multiply(BigInteger.TEN.pow(dapsDecimals))
                .divide(BigInteger.TEN.pow(ethDapsDecimals));
    }
     */
}
