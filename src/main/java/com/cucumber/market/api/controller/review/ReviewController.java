package com.cucumber.market.api.controller.review;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.service.review.ReviewSender;
import com.cucumber.market.api.service.review.ReviewService;
import com.cucumber.market.api.service.review.ReviewSort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 내가 받은 후기 전체 조회
     * @param reviewSender
     * @return
     */
    @GetMapping
    public ResponseEntity getReviewsOfMe(@RequestParam(name = "sender") ReviewSender reviewSender,
                                         @AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(reviewService.getReviewsOfMe(memberId, reviewSender));
    }


    /**
     * 상품 별 내가 받은 후기, 내가 보낸 후기 개별 조회
     * @param itemId
     * @param reviewSort
     * @return
     */
    @GetMapping("/{itemId}")
    public ResponseEntity getReview(@PathVariable(name = "itemId") int itemId,
                                    @RequestParam(name = "reviewSort") ReviewSort reviewSort,
                                    @AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(reviewService.getReview(memberId, itemId, reviewSort));
    }

}
