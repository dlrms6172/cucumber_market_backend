package com.cucumber.market.api.controller.user;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
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


    @GetMapping(value="/google")
    public String loginUrlGoogle(){
        Map<String,Object> params = new HashMap<>();
        params.put("scope",googleScope);
        params.put("response_type","code");
        params.put("client_id",googleClientId);
        params.put("redirect_uri",googleCallBackUrl);

        String parameterString=params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL=googleAuthUrl+"?"+parameterString;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
    }

    @GetMapping("/singin/google/callback")
    public String test(String code){

        System.out.println(code);

        return code;
    }

}
