package io.mywish.daps.blockchain.services;

import io.mywish.blockchain.WrapperOutput;
import io.mywish.blockchain.WrapperTransaction;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.NetworkParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WrapperTransactionDapsService {

    @Autowired
    private WrapperOutputDapsService outputBuilder;


    public WrapperTransaction build(org.bitcoinj.core.Transaction transaction, NetworkParameters networkParameters) {
        String hash = transaction.getHashAsString();
        List<String> inputs = new ArrayList<>();
        List<WrapperOutput> outputs = transaction.getOutputs().stream()
                .map(output -> outputBuilder.build(transaction, output, networkParameters))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new WrapperTransaction(
                hash,
                inputs,
                outputs,
                false
        );
    }
}
