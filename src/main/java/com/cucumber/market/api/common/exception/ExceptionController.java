package com.cucumber.market.api.common.exception;

import com.cucumber.market.api.common.payload.ResponseDto;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.cucumber.market.api.controller"})
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    // NullPointerException 오류
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity NullPointerException(NullPointerException e) {
        ResponseDto body = ResponseDto.builder()
                .resultCode(ValidationCode.SERVER_ERROR.getCode())
                .resultMsg(ValidationCode.SERVER_ERROR.getMessage())
                .resultDescription(ValidationCode.SERVER_ERROR.getDescription())
                .build();

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.SERVER_ERROR.getCode()).body(body);
    }

    // JWT 토큰 오류
    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity JwtException(JwtException e) {
        ResponseDto body = ResponseDto.builder()
                .resultCode(ValidationCode.JWT_VALID_ERROR.getCode())
                .resultMsg(ValidationCode.JWT_VALID_ERROR.getMessage())
                .resultDescription(ValidationCode.JWT_VALID_ERROR.getDescription())
                .build();

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.JWT_VALID_ERROR.getCode()).body(body);
    }

    // 그 외 오류
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity Exception(Exception e){
        ResponseDto body = ResponseDto.builder()
                .resultCode(ValidationCode.SERVER_ERROR.getCode())
                .resultMsg(ValidationCode.SERVER_ERROR.getMessage())
                .resultDescription(ValidationCode.SERVER_ERROR.getDescription())
                .build();

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.SERVER_ERROR.getCode()).body(body);
    }


}
