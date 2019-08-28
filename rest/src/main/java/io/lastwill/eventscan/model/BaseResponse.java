package io.lastwill.eventscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
    private final String status;
    @JsonProperty("eth_address")
    private final String ethAddress;
}
