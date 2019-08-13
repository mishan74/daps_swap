package io.mywish.daps.blockchain.model.params;

import org.bitcoinj.params.TestNet3Params;

public class DapsTestNetParams extends TestNet3Params {
    public DapsTestNetParams() {
        addressHeader = 139;
        p2shHeader = 19;
    }
}
