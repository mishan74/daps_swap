package io.lastwill.eventscan.events.model.contract.swap;

import io.mywish.blockchain.ContractEvent;
import io.mywish.blockchain.ContractEventDefinition;
import lombok.Getter;

@Getter
public class DapsConnectEvent extends ContractEvent {
    private final String eth;
    private final String daps;

    public DapsConnectEvent(final ContractEventDefinition definition, String eth, String daps, String address) {
        super(definition, address);
        this.eth = eth;
        this.daps = daps;
    }
}
