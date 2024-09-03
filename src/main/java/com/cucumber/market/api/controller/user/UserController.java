package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value="/signin")
    public ResponseEntity signInController(@Valid UserDto.signInDto dto){

        body.put("data",userService.signInService(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 회원가입 or 로그인
     * @param platform
     * @param dto
     * @return
     */
    @GetMapping("/singin/callback/{platform}")
    public ResponseEntity signInCallBackController(@PathVariable String platform, @Valid UserDto.signInCallBackDto dto){
        dto.setPlatform(platform);

        body.put("data",userService.signInCallBackService(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 프로필 조회
     * @param dto
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity userProfile(@Valid UserDto.userProfileGet dto){

        body.put("data",userService.userProfileGet(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    /**
     * 프로필 수정
     * @param dto
     * @return
     */
    @PutMapping("/profile")
    public ResponseEntity userProfile(@RequestBody @Valid UserDto.userProfilePut dto){

        body.put("data",userService.userProfilePut(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

}
