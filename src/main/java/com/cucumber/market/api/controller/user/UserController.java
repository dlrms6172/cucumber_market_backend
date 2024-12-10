package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.common.security.JwtTokenProvider;
import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
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
    public ResponseEntity signInCallBack(@PathVariable String platform, @Valid UserDto.signInCallBackDto dto){
        dto.setPlatform(platform);

        // 로그인 후 처리 서비스 호출(DB에 유저 정보 생성)
        Integer memberId = userService.signInCallBackService(dto);

        String token = jwtTokenProvider.generateToken(String.valueOf(memberId));

        // 헤더에 JWT 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // 응답으로 토큰 전달 (필요 시 프론트엔드 리다이렉션 URL 설정)
        headers.setLocation(URI.create("http://localhost:8080?jwt="+token));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * 프로필 조회
     * @param jwtToken
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity userProfile(@RequestHeader("Authorization") String jwtToken) {
        // "Bearer " 접두어 제거
        jwtToken = jwtToken.replace("Bearer ", "");

        // 토큰 검증
        if (!jwtTokenProvider.validateToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        int memberId = Integer.parseInt(jwtTokenProvider.getMemberId(jwtToken));

        body.put("data", userService.userProfileGet(memberId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 프로필 수정
     * @param dto
     * @return
     */
    @PutMapping("/profile")
    public ResponseEntity userProfile(@SessionAttribute Integer memberId,
                                      @RequestPart(value = "file", required = false) MultipartFile file,
                                      @RequestPart(value = "dto") @Valid UserDto.userProfilePut dto){
        dto.setMemberId(memberId);
        dto.setProfileImage(file);
        if (!file.isEmpty()) dto.setDeletedOldProfileImage(true);
        body.put("data",userService.userProfilePut(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

}
