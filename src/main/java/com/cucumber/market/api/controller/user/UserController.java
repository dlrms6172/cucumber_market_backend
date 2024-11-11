package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 로그인 페이지 리턴
     * @param dto
     * @return
     */
    @GetMapping("/signin")
    public ResponseEntity signIn(@ModelAttribute @Valid UserDto.signInDto dto){

        body.put("data",userService.signInService(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

/*
    *//**
     * 회원가입 or 로그인
     * @param platform
     * @param dto
     * @return
     *//*
    @GetMapping("/singin/callback/{platform}")
    public ResponseEntity signInCallBack(@PathVariable String platform, @ModelAttribute @Valid UserDto.signInCallBackDto dto){
        dto.setPlatform(platform);

        //헤더 로케이션 셋팅
        headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:8080/"));

        body.put("data",userService.signInCallBackService(dto));

        return new ResponseEntity(body, headers, HttpStatus.PERMANENT_REDIRECT);
    }
    */

    /**
     * 프로필 조회
     * @param memberId
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity userProfile(@RequestHeader(name = "memberId") int memberId){

        body.put("data", userService.userProfileGet(memberId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 프로필 수정
     * @param dto
     * @return
     */
    @PutMapping("/profile")
    public ResponseEntity userProfile(@RequestHeader(name = "memberId") int memberId,
                                      @RequestPart(value = "file", required = false) MultipartFile file,
                                      @RequestPart(value = "dto") @Valid UserDto.userProfilePut dto){
        dto.setMemberId(memberId);
        dto.setProfileImage(file);
        body.put("data",userService.userProfilePut(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

}
