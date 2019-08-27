package io.lastwill.eventscan.controller;

import io.lastwill.eventscan.model.HdWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EthAddressGenerator {
    @Autowired
    HdWallet hdWallet;

    String generate(int childId) {
        return hdWallet.getChildAddress(childId);
    }
}
