package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.service.item.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    private Map<String, Object> body = new LinkedHashMap<>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };


    @PostMapping("/{itemId}/like")
    public ResponseEntity addLike(@PathVariable(name = "itemId") int itemId,
                                  @AuthenticationPrincipal Integer memberId) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };
        body.put("data", likeService.addLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @DeleteMapping("/{itemId}/like")
    public ResponseEntity deleteLike(@PathVariable(name = "itemId") int itemId,
                                     @AuthenticationPrincipal Integer memberId) {
        body.put("data", likeService.deleteLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }

}
