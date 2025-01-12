package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.service.item.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{itemId}/like")
    public ResponseEntity addLike(@PathVariable(name = "itemId") int itemId,
                                  @AuthenticationPrincipal Integer memberId) {
        return CustomResponse.created(likeService.addLike(itemId, memberId));
    }

    @DeleteMapping("/{itemId}/like")
    public ResponseEntity deleteLike(@PathVariable(name = "itemId") int itemId,
                                     @AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(likeService.deleteLike(itemId, memberId));
    }

}
