package io.lastwill.eventscan.events.model.contract;

import io.mywish.blockchain.ContractEvent;
import io.mywish.blockchain.ContractEventDefinition;
import lombok.Getter;

@Getter
public class DapsReconnectEvent extends ContractEvent {
    private final String eth;
    private final String oldDaps;
    private final String newDaps;

    public DapsReconnectEvent(final ContractEventDefinition definition, String eth, String oldDaps, String newDaps, String address) {
        super(definition, address);
        this.eth = eth;
        this.oldDaps = oldDaps;
        this.newDaps = newDaps;
    }
}
