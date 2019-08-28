package io.lastwill.eventscan.controller;

import io.lastwill.eventscan.model.HdWallet;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.util.List;

@Component
public class EthAddressGenerator {
    private final HdWallet hdWallet;

    @Autowired
    public EthAddressGenerator(HdWallet hdWallet) {
        this.hdWallet = hdWallet;
    }

    String generate(int childId) {
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(hdWallet.getSeed()).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/" + childId + "/");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();
        Credentials credentials = Credentials.create(privKey.toString(16));
        return credentials.getAddress();
    }
}
