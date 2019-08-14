package io.lastwill.eventscan.events.model.dapsswap;

import io.lastwill.eventscan.events.model.BaseEvent;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import lombok.Getter;

@Getter
public class TokensBurnedEvent extends BaseEvent {
    private final String coin;
    private final int decimals;
    private final EthToDapsTransitionEntry swapEntry;
    private final String ethAddress;
    private final String dapsAddress;

    public TokensBurnedEvent(
            String coin,
            int decimals,
            EthToDapsTransitionEntry swapEntry,
            String ethAddress,
            String dapsAddress
    ) {
        super(NetworkType.ETHEREUM_MAINNET);
        this.coin = coin;
        this.decimals = decimals;
        this.swapEntry = swapEntry;
        this.ethAddress = ethAddress;
        this.dapsAddress = dapsAddress;
    }
}
