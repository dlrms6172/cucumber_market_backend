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
    @Autowired
    ProfileImageService imageService;

    public Map signInService(UserDto.SignInDto dto) {
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

    public Integer signInCallBackService(UserDto.SignInCallBackDto dto) {
        Integer memberId = null;

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

                    memberId = ensureMemberIsJoined(dto);
                }

            }
        }

        return memberId;
    }

    /**
     * signInCallBackService 시 필요한 서비스
     * @param dto
     * @return
     */
    public Integer ensureMemberIsJoined(UserDto.SignInCallBackDto dto) {
        Map selectCheckUserInfo = userMapper.selectCheckUserInfo(dto);

        if (selectCheckUserInfo == null) {  //유저 미존재 시 가입처리
            userMapper.insertUserInfo(dto);
            selectCheckUserInfo = userMapper.selectCheckUserInfo(dto);
        }

        return (Integer) selectCheckUserInfo.get("memberId");
    }

    public void saveRefreshToken(Integer memberId, String refreshToken) {
        int insertRefreshToken = userMapper.insertRefreshToken(memberId,refreshToken);
    }

    public Map userProfileGet(Integer memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        result = (LinkedHashMap)userMapper.selectUserInfo(memberId);
        //세션 만료 시 null 예외처리
        if(result != null){
            result.put("profileImageUrl", imageService.getImageUrl(memberId));
        }

        return result;
    }

    public Map userProfilePut(UserDto.UserProfilePutDto dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        int updateUserInfo = userMapper.updateUserInfo(dto);
        String updateImage = imageService.updateImage(dto);

        result.put("result",updateUserInfo);

        return result;
    }


}
