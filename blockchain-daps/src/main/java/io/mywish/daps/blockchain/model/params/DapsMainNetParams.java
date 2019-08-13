package io.mywish.daps.blockchain.model.params;

import org.bitcoinj.params.TestNet3Params;

public class DapsMainNetParams extends TestNet3Params {
    public DapsMainNetParams() {
        addressHeader = 30;
        p2shHeader = 13;
    }
}
