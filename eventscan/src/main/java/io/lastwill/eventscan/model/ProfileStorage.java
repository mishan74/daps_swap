package io.lastwill.eventscan.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Getter
public class ProfileStorage {

//    @Autowired
    private List<EthDapsProfile> ethDapsProfiles;

    public EthDapsProfile getProfileByEthConnectAddress(String linkAddress) {
        return ethDapsProfiles
                .stream()
                .filter(e -> e.getEthConnectAddress().equals(linkAddress))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "There is a transaction at address "
                                + linkAddress
                                + ", but the profile is not found"));
    }

    public EthDapsProfile getProfileByEthTokenAddress(String tokenAddress) {
        return ethDapsProfiles
                .stream()
                .filter(e -> e.getEthTokenAddress().equals(tokenAddress))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "There is a transaction at address "
                                + tokenAddress
                                + ", but the profile is not found"));
    }

    public EthDapsProfile getProfileByEthSymbol(String ethSymbol) {
        return ethDapsProfiles
                .stream()
                .filter(e -> e.getEth().name().equals(ethSymbol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No matching profiles by "
                                + ethSymbol
                                + " ETH symbol"));
    }
}
