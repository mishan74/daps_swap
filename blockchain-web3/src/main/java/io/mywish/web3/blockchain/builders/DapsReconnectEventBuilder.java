package io.mywish.web3.blockchain.builders;

import io.lastwill.eventscan.events.model.contract.swap.DapsReconnectEvent;
import io.mywish.web3.blockchain.model.Web3ContractEventDefinition;
import io.mywish.web3.blockchain.model.WrapperType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
@NoArgsConstructor
public class DapsReconnectEventBuilder extends Web3ContractEventBuilder<DapsReconnectEvent> {
    private final Web3ContractEventDefinition definition = new Web3ContractEventDefinition(
            "Reconnect",
            Arrays.asList(
                    WrapperType.create(Address.class, true),
                    WrapperType.create(Utf8String.class, true),
                    WrapperType.create(Utf8String.class, true)
            )
    );

    @Override
    public DapsReconnectEvent build(String address, List<Object> values) {
        return new DapsReconnectEvent(
                definition,
                (String) values.get(0),
                TypeEncoder.encode(new Bytes32((byte[]) values.get(1))),
                TypeEncoder.encode(new Bytes32((byte[]) values.get(2))),
                address
        );
    }
}
