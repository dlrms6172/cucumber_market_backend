package com.cucumber.market.api.controller.user;

import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TempUserController {

    private final UserService userService;

    /**
     * 프론트 로컬로 memberId 쿼리 파라미터로 반환하는 컨트롤러 (임시)
     * 회원가입 or 로그인
     * @param platform
     * @param dto
     * @return
     */
    @GetMapping("/user/singin/callback/{platform}")
    public String signInCallBack(@PathVariable String platform, @ModelAttribute @Valid UserDto.signInCallBackDto dto,
                                 RedirectAttributes redirectAttributes){
        dto.setPlatform(platform);

        Map<String, Map> map = userService.signInCallBackService(dto);
        Map userInfo = (Map) map.get("userInfo").get("userInfo");
        Integer memberId = (Integer) userInfo.get("memberId");
        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:http://localhost:8080/";
    }

}
