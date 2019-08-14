package io.lastwill.eventscan.events.model.dapsswap;

import io.lastwill.eventscan.events.model.BaseEvent;
import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import io.lastwill.eventscan.model.NetworkType;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class LowBalanceEvent extends BaseEvent {
    private final String coin;
    private final int decimals;
    private final EthToDapsTransitionEntry swapEntry;
    private final BigInteger need;
    private final BigInteger have;
    private final String fromAddress;

    public LowBalanceEvent(String coin, int decimals, EthToDapsTransitionEntry swapEntry, BigInteger need, BigInteger have, String fromAddress) {
        super(NetworkType.DAPS_MAINNET);
        this.coin = coin;
        this.decimals = decimals;
        this.swapEntry = swapEntry;
        this.need = need;
        this.have = have;
        this.fromAddress = fromAddress;
    }
}
