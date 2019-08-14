package io.lastwill.eventscan.events.model.dapsswap;

import io.lastwill.eventscan.events.model.BaseEvent;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import lombok.Getter;

@Getter
public class TokensTransferErrorEvent extends BaseEvent {
    private final String coin;
    private final int decimals;
    private final EthToDapsTransitionEntry ethEntry;

    public TokensTransferErrorEvent(
            String coin,
            int decimals,
            EthToDapsTransitionEntry ethEntry
    ) {
        super(NetworkType.ETHEREUM_MAINNET);
        this.coin = coin;
        this.decimals = decimals;
        this.ethEntry = ethEntry;
    }
}
