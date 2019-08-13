package io.mywish.daps.blockchain.model;

import com.binance.dex.api.client.domain.broadcast.Transaction;
import io.mywish.blockchain.WrapperOutput;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class WrapperOutputDaps extends WrapperOutput {
    private final String from;
    private final String symbol;
    private final String memo;

    public WrapperOutputDaps(Transaction tx, String from, String to, String symbol, String amount) {
        super(tx.getHash(), 0, to, new BigInteger(amount), new byte[0]);
        this.from = from;
        this.symbol = symbol;
        this.memo = tx.getMemo();
    }
}
