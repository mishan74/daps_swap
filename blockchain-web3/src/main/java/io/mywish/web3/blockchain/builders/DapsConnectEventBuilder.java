package io.mywish.web3.blockchain.builders;

import io.lastwill.eventscan.events.model.contract.swap.DapsConnectEvent;
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
public class DapsConnectEventBuilder extends Web3ContractEventBuilder<DapsConnectEvent> {
    private final Web3ContractEventDefinition definition = new Web3ContractEventDefinition(
            "Connect",
            Arrays.asList(
                    WrapperType.create(Address.class, true),
                    WrapperType.create(Utf8String.class, true)
            )
    );

    @Override
    public DapsConnectEvent build(String address, List<Object> values) {
        return new DapsConnectEvent(
                definition,
                (String) values.get(0),
                TypeEncoder.encode(new Bytes32((byte[]) values.get(1))),
                address
        );
    }
}
