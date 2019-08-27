package io.lastwill.eventscan.model;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;


import java.math.BigInteger;
import java.util.List;

@Component
public class DetermenistivMasterKey {

    private final String seedCode = "yard impulse luxury drive today throw farm pepper survey wreck glass federal";

    public String getAddress(int childId) {
        // BitcoinJ
        DeterministicSeed seed = null;
        try {
            seed = new DeterministicSeed(seedCode, null, "", 1409478661L);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/" + childId + "/");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();
        // Web3j
        Credentials credentials = Credentials.create(privKey.toString(16));
        return credentials.getAddress();
    }
}
