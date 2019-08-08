package io.lastwill.eventscan.services.senders;

import io.lastwill.eventscan.model.EthToBnbSwapEntry;

import java.math.BigInteger;

public interface Sender {
    void send(EthToBnbSwapEntry swapEntry);
    String toString(BigInteger amount, int decimals);
}
