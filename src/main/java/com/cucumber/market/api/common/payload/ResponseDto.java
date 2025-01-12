package com.cucumber.market.api.common.payload;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class ResponseDto {

    private int resultCode;
    private String resultMsg;
    private String resultDescription;
    private Map data;
}
