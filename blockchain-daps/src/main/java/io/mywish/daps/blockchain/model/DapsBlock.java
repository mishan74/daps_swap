package io.mywish.daps.blockchain.model;

import com.binance.dex.api.client.domain.BlockMeta;
import com.binance.dex.api.client.domain.broadcast.Transaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DapsBlock {
    private final BlockMeta blockMeta;
    private final List<Transaction> transactions;
}
