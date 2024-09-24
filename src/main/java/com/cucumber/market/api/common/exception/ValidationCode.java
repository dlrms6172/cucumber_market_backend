package com.cucumber.market.api.common.exception;

public enum ValidationCode {
    SUCCESS(200,"SUCCESS","성공"),
    REQUEST_ERROR(400,"REQUEST ERROR","호출 에러"),
    SERVER_ERROR(500,"SERVER ERROR","서버 에러");


    private final int code;

    private final String message;

    private final String description;

    ValidationCode(int code, String message, String description){
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    public String getDescription(){
        return this.description;
    }
}
