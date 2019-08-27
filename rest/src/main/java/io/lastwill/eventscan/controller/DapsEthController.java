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

    private final String sharedKey = "SHARED_KEY";

    private static final String ERROR_STATUS = "error";
    private static final String NEW_ADDRESS_STATUS = "new";
    private static final String ALREADY_EXIST_STATUS = "exist";
    private static final String TEST_STATUS = "test";

    @GetMapping
    public BaseResponse showStatus() {
        return new BaseResponse(TEST_STATUS, "0X0fdfjdjs...");
    }

    @PostMapping(value = "/get-eth-address", produces = "application/json")
    public BaseResponse pay(@RequestParam(value = "key") String key, @RequestBody DapsRequest request) {
        final BaseResponse response;

        if (sharedKey.equalsIgnoreCase(key)) {
            if (request == null || request.getDapsAddress() == null) {
                return null;
            }
            String dapsAddress = request.getDapsAddress();

            response = getResponceByAddress(dapsAddress);
        } else {
            response = new BaseResponse(ERROR_STATUS, request.getDapsAddress());
        }
        return response;
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