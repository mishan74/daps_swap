package io.lastwill.eventscan.events.model;

import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import io.lastwill.eventscan.model.NetworkType;
import lombok.Getter;

@Getter
public class TokensBurnedEvent extends BaseEvent {
    private final EthToDapsTransitionEntry transitionEntry;

    public TokensBurnedEvent(EthToDapsTransitionEntry transitionEntry) {
        super(NetworkType.ETHEREUM_MAINNET);
        this.transitionEntry = transitionEntry;
    }
}
