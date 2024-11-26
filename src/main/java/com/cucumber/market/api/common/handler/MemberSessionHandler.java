package com.cucumber.market.api.common.handler;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MemberSessionHandler {

    public int getMemberIdFromSession(HttpSession session){
        int memberId = Optional.ofNullable((Map<String, Object>) session.getAttribute("userInfo"))
                .map(userInfo -> (Map<String, Object>) userInfo.get("userInfo"))
                .map(userInfoMap -> (Integer) userInfoMap.get("memberId"))
                .orElseThrow();

        return memberId;
    }
}
