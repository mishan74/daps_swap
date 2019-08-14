package io.lastwill.eventscan.events.model.contract.swap;

import io.mywish.blockchain.ContractEventDefinition;
import lombok.Getter;

@Getter
public class DapsReconnectEvent extends DapsConnectEvent {
    private final String eth;
    private final String oldDaps;

    public DapsReconnectEvent(final ContractEventDefinition definition, String eth, String oldDaps, String newDaps, String address) {
        super(definition, eth, newDaps, address);
        this.eth = eth;
        this.oldDaps = oldDaps;
    }
}
