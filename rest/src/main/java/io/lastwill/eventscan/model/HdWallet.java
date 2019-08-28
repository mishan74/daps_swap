package io.lastwill.eventscan.model;

import lombok.Getter;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class HdWallet {

    private DeterministicSeed seed;

    public HdWallet(@Value("${io.lastwill.eventscan.model.hdwallet.seed}") String seedCode,
                    @Value("${io.lastwill.eventscan.model.hdwallet.creation.time.seconds}") long creationTimeSeconds) {
        try {
            this.seed = new DeterministicSeed(seedCode, null, "", creationTimeSeconds);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
    }
}
