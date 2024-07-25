package com.cucumber.market.api.dto.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Description;

public class UserDto {

    @Getter
    @Setter
    public static class signInDto {
        @NotBlank
        private String platform;
    }

    @Getter
    @Setter
    public static class signInCallBackDto {

        @NotBlank
        private String platform;

        @NotBlank
        private String code;
    }
}
