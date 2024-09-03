package com.cucumber.market.api.service.user;

import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

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

    @Value("${google.client.userinfo-request-url}")
    private String googleUserInfoRequestUrl;

    @Autowired
    UserMapper userMapper;

    public Map signInService(UserDto.signInDto dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

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

    public Map signInCallBackService(UserDto.signInCallBackDto dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

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

            if(responseEntity.getStatusCode() == HttpStatus.OK){

                String accessToken = responseEntity.getBody().get("access_token").toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization","Bearer " + accessToken);
                HttpEntity<Map<String, String>> request = new HttpEntity(headers);

                // 사용자 정보 호출
                ResponseEntity<Map> response=restTemplate.exchange(googleUserInfoRequestUrl, HttpMethod.GET, request, Map.class);

                // 사용자 정보가 정상적으로 호출되면
                if(response.getStatusCode() == HttpStatus.OK){

                    dto.setSnsId(1);
                    dto.setSnsValue(response.getBody().get("id").toString());
                    dto.setEmail(response.getBody().get("email").toString());

                    Map userInfo = userCheck(dto);

                    result.put("userInfo",userInfo);
                }

            }
        }else if(dto.getPlatform().equals("kakao")){

        }

        return result;
    }

    /**
     * signInCallBackService 시 필요한 서비스
     * @param dto
     * @return
     */
    public Map userCheck(UserDto.signInCallBackDto dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        Map selectCheckUserInfo = userMapper.selectCheckUserInfo(dto);

        // 유저 존재
        if(selectCheckUserInfo != null){
            result.put("userInfo",selectCheckUserInfo);
        }else { // 유저 미존재
            userMapper.insertUserInfo(dto);
            selectCheckUserInfo = userMapper.selectCheckUserInfo(dto);

            result = (LinkedHashMap<String, Object>) selectCheckUserInfo;
        }

        return result;
    }

    public Map userProfileGet(UserDto.userProfileGet dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        result = (LinkedHashMap)userMapper.selectUserInfo(dto);

        return result;
    }

    public Map userProfilePut(UserDto.userProfilePut dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        int updateUserInfo = userMapper.updateUserInfo(dto);
        result.put("result",updateUserInfo);

        return result;
    }


}
