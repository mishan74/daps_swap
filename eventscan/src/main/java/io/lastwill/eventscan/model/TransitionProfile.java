package io.lastwill.eventscan.model;

import io.lastwill.eventscan.services.senders.Sender;
import lombok.Getter;

@Getter
public abstract class TransitionProfile {
    private String ethConnectAddress;
    private String ethBurnerAddress;
    private String ethTokenAddress;
    private String transferSymbol;
    private Sender sender;
    private CryptoCurrency eth; // todo: rename
    private CryptoCurrency daps; // todo: rename

    public TransitionProfile(String ethConnectAddress,
                             String ethBurnerAddress,
                             String ethTokenAddress,
                             String transferSymbol,
                             Sender sender,
                             CryptoCurrency eth,
                             CryptoCurrency daps) {
        this.ethConnectAddress = ethConnectAddress;
        this.ethBurnerAddress = ethBurnerAddress;
        this.ethTokenAddress = ethTokenAddress;
        this.transferSymbol = transferSymbol;
        this.sender = sender;
        this.eth = eth;
        this.daps = daps;
    }
}
