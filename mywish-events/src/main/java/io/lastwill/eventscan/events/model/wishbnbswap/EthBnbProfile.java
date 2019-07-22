package io.lastwill.eventscan.events.model.wishbnbswap;

import io.lastwill.eventscan.model.CryptoCurrency;
import lombok.Getter;


@Getter
public abstract class EthBnbProfile {

    private String wishLinkAddress;

    private String ethBurnerAddress;

    private String ethTokenAddress;

    CryptoCurrency eth;

    CryptoCurrency bnb;



    public EthBnbProfile(String wishLinkAddress,
                         String ethBurnerAddress,
                         String ethTokenAddress,
                         CryptoCurrency eth,
                         CryptoCurrency bnb) {

        this.wishLinkAddress = wishLinkAddress;
        this.ethBurnerAddress = ethBurnerAddress;
        this.ethTokenAddress = ethTokenAddress;
        this.eth = eth;
        this.bnb = bnb;

    }

}
