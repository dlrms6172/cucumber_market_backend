package com.cucumber.market.api.service.user;

import com.cucumber.market.api.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private LinkedHashMap<String,Object> result = new LinkedHashMap<>();

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

    @Value("${google.client.token-request-url}")
    private String googleTokenRequestUrl;

    /**
     * 로그인 서비스
     * @return
     */
    public Map signInService(UserDto.signInDto dto) {

        // 구글 로그인 서비스
        if(dto.getPlatform().equals("google")){
            Map<String,Object> params = new HashMap<>();
            params.put("scope",googleScope);
            params.put("response_type","code");
            params.put("client_id",googleClientId);
            params.put("redirect_uri",googleCallBackUrl);

            String parameterString=params.entrySet().stream()
                    .map(x->x.getKey()+"="+x.getValue())
                    .collect(Collectors.joining("&"));
            String redirectURL=googleAuthUrl+"?"+parameterString;

            result.put("signInPage",redirectURL);
        }

        return result;
    }

    /**
     * 로그인 콜백 서비스
     */
    public Map signInCallBackService(UserDto.signInCallBackDto dto) {

        // 구글 로그인 서비스
        if(dto.getPlatform().equals("google")){
            // 엑세스 토큰 발급
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> params = new HashMap<>();
            params.put("code", dto.getCode());
            params.put("client_id", googleClientId);
            params.put("client_secret", googleClientPw);
            params.put("redirect_uri", googleCallBackUrl);
            params.put("grant_type", "authorization_code");

            ResponseEntity<Map> responseEntity=restTemplate.postForEntity(googleTokenRequestUrl,
                    params,Map.class);

            // 사용자 정보
            if(responseEntity.getStatusCode() == HttpStatus.OK){

            }
            
        }

        return result;
    }

}
