package io.mywish.daps.blockchain.services;


import com.neemre.btcdcli4j.core.domain.PaymentOverview;
import com.neemre.btcdcli4j.core.domain.Transaction;
import io.mywish.blockchain.WrapperOutput;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@Slf4j
public class WrapperOutputDapsService {
    public WrapperOutput build(Transaction transaction, PaymentOverview output) {

        String address = output.getAddress();

        return new WrapperOutput(
                transaction.getTxId(),
                output.getVOut(),
                address,
                output.getAmount().multiply(BigDecimal.TEN.pow(8)).toBigInteger(),
                transaction.getHex().getBytes()
        );
    }

}
