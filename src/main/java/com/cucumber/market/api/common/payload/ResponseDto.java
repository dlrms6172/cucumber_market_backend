package com.cucumber.market.api.common.payload;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDto<T> {

    private int resultCode;
    private String resultMsg;
    private String resultDescription;
    private T data;
}
