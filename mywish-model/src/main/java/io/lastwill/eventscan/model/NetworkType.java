package io.lastwill.eventscan.model;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum NetworkType {
    ETHEREUM_MAINNET(NetworkProviderType.WEB3),
    BINANCE_MAINNET(NetworkProviderType.BINANCE);

    public final static String ETHEREUM_MAINNET_VALUE = "ETHEREUM_MAINNET";
    public final static String BINANCE_MAINNET_VALUE = "BINANCE_MAINNET";


    private final NetworkProviderType networkProviderType;

    NetworkType(NetworkProviderType networkProviderType) {
        this.networkProviderType = networkProviderType;
    }

}
