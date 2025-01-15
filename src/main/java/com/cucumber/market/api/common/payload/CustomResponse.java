package com.cucumber.market.api.common.payload;

import com.cucumber.market.api.common.exception.ValidationCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse {

    public static <T> ResponseEntity<ResponseDto<T>> ok() {
        ResponseDto<T>  body = ResponseDto.<T>builder()
                .resultCode(ValidationCode.SUCCESS.getCode())
                .resultMsg(ValidationCode.SUCCESS.getMessage())
                .resultDescription(ValidationCode.SUCCESS.getDescription())
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> ok(T result) {
        ResponseDto<T> body = ResponseDto.<T>builder()
                .resultCode(ValidationCode.SUCCESS.getCode())
                .resultMsg(ValidationCode.SUCCESS.getMessage())
                .resultDescription(ValidationCode.SUCCESS.getDescription())
                .data(result)
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
