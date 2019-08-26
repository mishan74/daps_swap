package io.lastwill.eventscan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
    private final String status;
    private final String ethAddress;
}
