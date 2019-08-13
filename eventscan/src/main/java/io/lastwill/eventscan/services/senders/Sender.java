package io.lastwill.eventscan.services.senders;

import io.lastwill.eventscan.model.EthToDapsSwapEntry;

import java.math.BigInteger;

public interface Sender {
    void send(EthToDapsSwapEntry swapEntry);
    String toString(BigInteger amount, int decimals);
}
