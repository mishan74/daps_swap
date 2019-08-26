package io.lastwill.eventscan.controller;

import org.springframework.stereotype.Component;

@Component
public class EthAddressGenerator {
    String generate(String dapsAddress) {
        return "generateAddress" + dapsAddress;
    }
}
