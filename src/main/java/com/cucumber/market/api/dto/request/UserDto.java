package com.cucumber.market.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class UserDto {

    @Getter
    @Setter
    public static class SignInDto {
        @NotBlank
        private String platform;
    }

    @Getter
    @Setter
    public static class SignInCallBackDto {

        @NotBlank
        private String platform;

        @NotBlank
        private String code;

        private String refreshToken;

        private int snsId;

        private String snsValue;

        private String email;
    }

    @Getter
    @Setter
    public static class UserProfilePutDto {
        private Integer memberId;

        @NotNull
        private Integer snsId;

        @NotBlank
        private String name;

        @NotBlank
        private String email;

        @NotNull
        private Integer regionId;

        private MultipartFile profileImage;

        private boolean deletedOldProfileImage = false;  //프로필 이미지 삭제의 경우 프론트에서 true 로 입력

    }

    @Getter
    @Setter
    public static class ProfileImageDto {

        private Integer memberId;
        private String originalName;
        private String keyName;

    }
}
