package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.common.handler.MemberSessionHandler;
import com.cucumber.market.api.service.item.LikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final MemberSessionHandler memberSessionHandler;

    private Map<String, Object> body = new LinkedHashMap<>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };


    @PostMapping("/{itemId}/like")
    public ResponseEntity addLike(@PathVariable(name = "itemId") int itemId,
                                  HttpSession session) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data", likeService.addLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @DeleteMapping("/{itemId}/like")
    public ResponseEntity deleteLike(@PathVariable(name = "itemId") int itemId,
                                     HttpSession session) {
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data", likeService.deleteLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }

}
