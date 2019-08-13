package io.lastwill.eventscan.model;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum NetworkType {
    ETHEREUM_MAINNET(NetworkProviderType.WEB3),
    DAPS_MAINNET(NetworkProviderType.DAPS);

    public final static String ETHEREUM_MAINNET_VALUE = "ETHEREUM_MAINNET";
    public final static String DAPS_MAINNET_VALUE = "DAPS_MAINNET";


    private final NetworkProviderType networkProviderType;

    NetworkType(NetworkProviderType networkProviderType) {
        this.networkProviderType = networkProviderType;
    }

}
