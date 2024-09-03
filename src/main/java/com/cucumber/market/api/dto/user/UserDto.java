package com.cucumber.market.api.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        private int snsId;

        private String snsValue;

        private String email;
    }

    @Getter
    @Setter
    public static class userProfileGet {
        @NotNull
        private Integer memberId;
    }

    @Getter
    @Setter
    public static class userProfilePut {
        @NotNull
        private Integer memberId;

        @NotNull
        private Integer snsId;

        @NotBlank
        private String name;

        @NotBlank
        private String email;

        @NotNull
        private Integer regionId;
    }
}
