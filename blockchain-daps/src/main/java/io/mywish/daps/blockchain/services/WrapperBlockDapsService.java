package io.mywish.daps.blockchain.services;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Block;
import io.mywish.blockchain.WrapperBlock;
import io.mywish.blockchain.WrapperTransaction;

import org.bitcoinj.core.NetworkParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WrapperBlockDapsService {

    @Autowired
    private WrapperTransactionDapsService transactionBuilder;

    @Autowired
    BtcdClient client;

    public WrapperBlock build(Block block, Long height, NetworkParameters networkParameters) {

        String hash = block.getHash();
        Instant timestamp = Instant.ofEpochSecond(block.getTime());
        List<WrapperTransaction> transactions = block
                .getTx()
                .stream()
                .map(tx -> {
                    try {
                        return transactionBuilder.build(client.getTransaction(tx), networkParameters);
                    } catch (BitcoindException | CommunicationException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        return new WrapperBlock(hash, height, timestamp, transactions);
    }
}
