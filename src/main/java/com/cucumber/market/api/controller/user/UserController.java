package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Value("${google.client.auth-url}")
    private String googleAuthUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.pw}")
    private String googleClientPw;

    @Value("${google.client.callback-url}")
    private String googleCallBackUrl;

    @Value("${google.client.scope}")
    private String googleScope;

    @GetMapping(value="/signin")
    public ResponseEntity signInController(@Valid UserDto.signInDto dto){

        body.put("data",userService.signInService(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/singin/callback/{platform}")
    public ResponseEntity signInCallBackController(@Valid UserDto.signInCallBackDto dto){


        body.put("data",userService.signInCallBackService(dto));

        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        RestTemplate restTemplate=new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", dto.getCode());
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientPw);
        params.put("redirect_uri", googleCallBackUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<Map> responseEntity=restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params,Map.class);


        if(responseEntity.getStatusCode() == HttpStatus.OK){
            String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";
            String accessToken = responseEntity.getBody().get("access_token").toString();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer "+accessToken);

            //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
            HttpEntity<Map<String, String>> request = new HttpEntity(headers);
            ResponseEntity<String> response=restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);
            System.out.println("response.getBody() = " + response.getBody());
        }


        return new ResponseEntity(body, headers, HttpStatus.OK);

    }

}
