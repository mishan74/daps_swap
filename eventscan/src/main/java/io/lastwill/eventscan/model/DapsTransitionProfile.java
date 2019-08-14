package io.lastwill.eventscan.model;

import io.lastwill.eventscan.services.senders.DapsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DapsTransitionProfile extends TransitionProfile {
    @Autowired
    public DapsTransitionProfile(
            @Value("${io.lastwill.eventscan.daps.wish-swap.linker-address}") String connectorAddress,
            @Value("${io.lastwill.eventscan.daps.wish-swap.burner-address}") String burnerAddress,
            @Value("${io.lastwill.eventscan.contract.token-address.daps}") String ethTokenAddress,
            @Value("${io.lastwill.eventscan.daps.token-symbol}") String transferSymbol,
            DapsSender dapsSender
    ) {
        super(connectorAddress.toLowerCase(),
                burnerAddress.toLowerCase(),
                ethTokenAddress.toLowerCase(),
                transferSymbol,
                dapsSender,
                CryptoCurrency.ETH_DAPS,
                CryptoCurrency.DAPS);
    }
}
