package io.lastwill.eventscan.events.model.dapsswap;

import io.lastwill.eventscan.events.model.BaseEvent;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import lombok.Getter;

@Getter
public class TokensTransferredEvent extends BaseEvent {
    private final String coin;
    private final int decimals;
    private final EthToDapsTransitionEntry ethEntry;
    private final String dapsSenderAddress;

    public TokensTransferredEvent(
            String coin,
            int decimals,
            EthToDapsTransitionEntry ethEntry,
            String dapsSenderAddress
    ) {
        super(NetworkType.DAPS_MAINNET);
        this.coin = coin;
        this.decimals = decimals;
        this.ethEntry = ethEntry;
        this.dapsSenderAddress = dapsSenderAddress;
    }
}
