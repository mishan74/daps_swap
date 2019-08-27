package io.lastwill.eventscan.controller;

import io.lastwill.eventscan.model.DetermenistivMasterKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EthAddressGenerator {
    @Autowired
    DetermenistivMasterKey masterKey;

    String generate(int childId) {
        return masterKey.getAddress(childId);
    }
}
