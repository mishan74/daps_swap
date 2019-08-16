package io.lastwill.eventscan.events.model.contract.erc20;

import io.mywish.blockchain.ContractEvent;
import io.mywish.blockchain.ContractEventDefinition;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class TransferEvent extends ContractEvent {
    private final String from;
    private final String to;
    private final BigInteger tokens;

    public TransferEvent(ContractEventDefinition definition, String from, String to, BigInteger tokens, String address) {
        super(definition, address);
        this.from = from;
        this.to = to;
        this.tokens = tokens;
    }
}
