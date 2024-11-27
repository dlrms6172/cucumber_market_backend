package com.cucumber.market.api.controller.user;

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
    @GetMapping("/singin/callback/{platform}")
    public ResponseEntity signInCallBack(@PathVariable String platform, @Valid UserDto.signInCallBackDto dto, HttpSession session){
        dto.setPlatform(platform);

        //헤더 로케이션 셋팅
        headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:8080/?sessionId=" + session.getId()));

        // 로그인 후 처리 서비스 호출(DB에 유저 정보 생성)
        Integer memberId = userService.signInCallBackService(dto);

        // 해당 컨트롤러가 호출될 때 세션을 받지 않으므로 세션을 생성해서 DB에 만든 사용자 정보를 세션에 저장(값을 저장하게 되면 세션이 생성됨)
        session.setAttribute("memberId", memberId);

        // 세션 ID를 쿠키로 클라이언트에 전달
        headers.add("Set-Cookie", "OIMARKETSESSIONID=" + session.getId() + "; Path=/");

        return new ResponseEntity(body, headers, HttpStatus.FOUND);
    }

    /**
     * 프로필 조회
     * @param memberId
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity userProfile(@SessionAttribute Integer memberId){
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
        body.put("data",userService.userProfilePut(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

}
