package io.lastwill.eventscan.services.senders;

import io.lastwill.eventscan.model.EthToDapsTransitionEntry;

import java.math.BigInteger;

public interface Sender {
    void send(EthToDapsTransitionEntry transitionEntry);
}
