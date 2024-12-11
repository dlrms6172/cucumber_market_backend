package com.cucumber.market.api.common.exception;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice(basePackages = {"com.cucumber.market.api.controller"})
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    // NullPointerException 오류
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity NullPointerException(NullPointerException e) {
        LinkedHashMap body = new LinkedHashMap();

        body.put("resultCode",ValidationCode.SERVER_ERROR.getCode());
        body.put("resultMsg",ValidationCode.SERVER_ERROR.getMessage());
        body.put("resultDescription",ValidationCode.SERVER_ERROR.getDescription());

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.SERVER_ERROR.getCode()).body(body);
    }

    // JWT 토큰 오류
    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity JwtException(JwtException e) {
        LinkedHashMap body = new LinkedHashMap();

        body.put("resultCode",ValidationCode.JWT_VALID_ERROR.getCode());
        body.put("resultMsg",ValidationCode.JWT_VALID_ERROR.getMessage());
        body.put("resultDescription",ValidationCode.JWT_VALID_ERROR.getDescription());

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.JWT_VALID_ERROR.getCode()).body(body);
    }

    // 그 외 오류
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity Exception(Exception e){
        LinkedHashMap body = new LinkedHashMap();

        body.put("resultCode",ValidationCode.SERVER_ERROR.getCode());
        body.put("resultMsg",ValidationCode.SERVER_ERROR.getMessage());
        body.put("resultDescription",ValidationCode.SERVER_ERROR.getDescription());

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.SERVER_ERROR.getCode()).body(body);
    }


}
