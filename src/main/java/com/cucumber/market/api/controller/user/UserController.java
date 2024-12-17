package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.common.security.JwtTokenProvider;
import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.user.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private HttpHeaders headers;
    private Map<String, Object> body = new LinkedHashMap<String, Object>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };

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
    public ResponseEntity signIn(@Valid UserDto.signInDto dto){

        body.put("data",userService.signInService(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 회원가입 or 로그인
     * @param platform
     * @param dto
     * @return
     */
    @GetMapping("/signin/callback/{platform}")
    public ResponseEntity signInCallBack(@PathVariable String platform, @Valid UserDto.signInCallBackDto dto, HttpServletResponse response){
        dto.setPlatform(platform);

        // 로그인 후 처리 서비스 호출(DB에 유저 정보 생성)
        Integer memberId = userService.signInCallBackService(dto);

        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(memberId));
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
    public ResponseEntity userProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        body.put("data", userService.userProfileGet(memberId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 프로필 수정
     * @param dto
     * @return
     */
    @PutMapping("/profile")
    public ResponseEntity userProfile(@RequestPart(value = "file", required = false) MultipartFile file,
                                      @RequestPart(value = "dto") @Valid UserDto.userProfilePut dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        dto.setMemberId(memberId);
        dto.setProfileImage(file);

        if (!file.isEmpty()) dto.setDeletedOldProfileImage(true);
        body.put("data", userService.userProfilePut(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 토큰 재발급
     * @param refreshToken
     * @return
     */
    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(@RequestHeader("Authorization") String refreshToken) throws JwtException {

        if(jwtTokenProvider.validateRefreshToken(refreshToken)) {
            // Refresh Token으로 DB에서 memberId 추출
            String memberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken);

            // 새로운 Access Token 발급
            String newAccessToken = jwtTokenProvider.generateAccessToken(memberId);

            body.put("data",new HashMap<>(Map.of("accessToken",newAccessToken)));

        } else {
            throw new JwtException("");
        }

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
