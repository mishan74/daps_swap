package io.lastwill.eventscan.controller;

import io.lastwill.eventscan.model.BaseResponse;
import io.lastwill.eventscan.model.DapsRequest;
import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import io.lastwill.eventscan.repositories.EthToDapsConnectEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class DapsEthController {

    @Autowired
    private EthAddressGenerator generator;
    @Autowired
    private EthToDapsConnectEntryRepository ethToDapsConnectEntryRepository;

    private static final String NEW_ADDRESS_STATUS = "new";
    private static final String ALREADY_EXIST_STATUS = "exist";


    @PostMapping(value = "/get-eth-address", produces = "application/json")
    public BaseResponse pay(@RequestBody DapsRequest request) {
        if (request == null || request.getDapsAddress() == null) {
            return null;
        }
        return getResponceByAddress(request.getDapsAddress());
    }

    private BaseResponse getResponceByAddress(String dapsAddress) {
        BaseResponse response;
        if (dapsAddress == null) {
            return null;
        }
        EthToDapsConnectEntry entry;
        entry = ethToDapsConnectEntryRepository.findFirstByDapsAddress(dapsAddress);
        if (entry == null) {
            entry = new EthToDapsConnectEntry("", dapsAddress);
            ethToDapsConnectEntryRepository.save(entry);
        }
        if (!entry.getEthAddress().isEmpty()) {
            response = new BaseResponse(ALREADY_EXIST_STATUS, entry.getEthAddress());
        } else {
            String ethAddress = generator.generate(entry.getId().intValue());
            entry.setEthAddress(ethAddress);
            ethToDapsConnectEntryRepository.save(entry);
            response = new BaseResponse(NEW_ADDRESS_STATUS, entry.getEthAddress());
        }
        return response;
    }
}