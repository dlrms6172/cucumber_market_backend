package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.common.security.JwtTokenProvider;
import com.cucumber.market.api.dto.request.UserDto;
import com.cucumber.market.api.service.user.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 페이지 리턴
     * @param dto
     * @return
     */
    @GetMapping("/signin")
    public ResponseEntity signIn(@Valid UserDto.SignInDto dto){
        return CustomResponse.ok(userService.signInService(dto));
    }

    /**
     * 회원가입 or 로그인
     * @param platform
     * @param dto
     * @return
     */
    @GetMapping("/signin/callback/{platform}")
    public ResponseEntity signInCallBack(@PathVariable String platform, @Valid UserDto.SignInCallBackDto dto, HttpServletResponse response){
        dto.setPlatform(platform);

        // 로그인 후 처리 서비스 호출(DB에 유저 정보 생성)
        Integer memberId = userService.signInCallBackService(dto);

        String accessToken = jwtTokenProvider.generateAccessToken(memberId);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        // refreshToken 저장
        userService.saveRefreshToken(memberId,refreshToken);

        // 쿠키에 JWT 토큰을 포함해서 클라이언트에 전달
        Cookie accessCookie = new Cookie("ACCESS_TOKEN", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);

        // 쿠키를 응답에 추가
        response.addCookie(accessCookie); // Access Token 쿠키 추가
        response.addCookie(refreshCookie); // Refresh Token 쿠키 추가

        // 헤더에 JWT 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:8080"+"?access="+accessToken+"&"+"refresh="+refreshToken));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * 프로필 조회
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity userProfile(@AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(userService.userProfileGet(memberId));
    }

    /**
     * 프로필 수정
     * @param dto
     * @return
     */
    @PutMapping("/profile")
    public ResponseEntity userProfile(@RequestPart(value = "file", required = false) MultipartFile file,
                                      @RequestPart(value = "dto") @Valid UserDto.UserProfilePutDto dto,
                                      @AuthenticationPrincipal Integer memberId) {
        dto.setMemberId(memberId);
        dto.setProfileImage(file);

        if (!file.isEmpty()) dto.setDeletedOldProfileImage(true);
        return CustomResponse.ok(userService.userProfilePut(dto));
    }

    /**
     * 토큰 재발급
     * @param refreshToken
     * @return
     */
    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(@RequestHeader("Authorization") String refreshToken) throws JwtException {
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new JwtException("");
        }

        // Refresh Token으로 DB에서 memberId 추출
        Integer memberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken);

        // 새로운 Access Token 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId);

        return CustomResponse.ok(new HashMap<>(Map.of("accessToken", newAccessToken)));
    }
}
