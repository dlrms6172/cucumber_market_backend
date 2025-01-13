package com.cucumber.market.api.common.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class CustomResponse {

    public static ResponseEntity<ResponseDto> ok() {
        ResponseDto body = ResponseDto.builder()
                .resultCode(200)
                .resultMsg("success")
                .resultDescription("요청에 성공했습니다.")
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseDto> ok(Map result) {
        ResponseDto body = ResponseDto.builder()
                .resultCode(200)
                .resultMsg("success")
                .resultDescription("요청에 성공했습니다.")
                .data(result)
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseDto> created(Map result) {
        ResponseDto body = ResponseDto.builder()
                .resultCode(201)
                .resultMsg("success")
                .resultDescription("요청에 성공했습니다.")
                .data(result)
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
