package io.lastwill.eventscan.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Getter
public class ProfileStorage {
    @Autowired
    private List<TransitionProfile> transitionProfiles;

    public TransitionProfile getProfileByEthConnectAddress(String connectAddress) {
        return transitionProfiles
                .stream()
                .filter(e -> e.getEthConnectAddress().equals(connectAddress))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "There is a transaction at address "
                                + connectAddress
                                + ", but the profile is not found"));
    }

    public TransitionProfile getProfileByEthTokenAddress(String tokenAddress) {
        return transitionProfiles
                .stream()
                .filter(e -> e.getEthTokenAddress().equals(tokenAddress))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "There is a transaction at address "
                                + tokenAddress
                                + ", but the profile is not found"));
    }

    public TransitionProfile getProfileByEthSymbol(String ethSymbol) {
        return transitionProfiles
                .stream()
                .filter(e -> e.getEth().name().equals(ethSymbol))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No matching profiles by "
                                + ethSymbol
                                + " ETH symbol"));
    }
}
