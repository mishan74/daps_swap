package io.mywish.daps.blockchain.services;

import com.neemre.btcdcli4j.core.client.BtcdClient;
import io.lastwill.eventscan.model.NetworkType;
import io.mywish.blockchain.WrapperBlock;
import io.mywish.blockchain.WrapperNetwork;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.blockchain.WrapperTransactionReceipt;
import org.bitcoinj.core.NetworkParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class DapsNetwork extends WrapperNetwork {
    final private BtcdClient dapsClient;

    @Autowired
    BtcdClient client;

    @Autowired
    private WrapperBlockDapsService blockBuilder;

    private final NetworkParameters networkParameters;

    public DapsNetwork(NetworkType type, BtcdClient dapsClient, NetworkParameters networkParameters) {
        super(type);
        this.dapsClient = dapsClient;
        this.networkParameters = networkParameters;
    }

    @Override
    public Long getLastBlock() throws Exception {
        return dapsClient.getBlockCount().longValue();
    }

    @Override
    public WrapperBlock getBlock(String hash) throws Exception {
        long height = dapsClient.getBlock(hash).getHeight();
        return blockBuilder.build(client.getBlock(hash),
                height,
                networkParameters
        );
    }

    @Override
    public WrapperBlock getBlock(Long number) throws Exception {
        String hash = dapsClient.getBlockHash(number.intValue());
        return getBlock(hash);
    }

    @Override
    public BigInteger getBalance(String address, Long blockNo) {
        throw new UnsupportedOperationException("Method not supported");
    }

    @Override
    public WrapperTransactionReceipt getTxReceipt(WrapperTransaction transaction) {
        throw new UnsupportedOperationException("Method not supported");
    }
}
