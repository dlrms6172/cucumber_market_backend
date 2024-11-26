package com.cucumber.market.api.controller.review;

import com.cucumber.market.api.common.handler.MemberSessionHandler;
import com.cucumber.market.api.service.review.ReviewSender;
import com.cucumber.market.api.service.review.ReviewService;
import com.cucumber.market.api.service.review.ReviewSort;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberSessionHandler memberSessionHandler;

    private Map<String, Object> body = new LinkedHashMap<>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };

    /**
     * 내가 받은 후기 전체 조회
     * @param reviewSender
     * @return
     */
    @GetMapping
    public ResponseEntity getReviewsOfMe(@RequestParam(name = "sender") ReviewSender reviewSender,
                                         HttpSession session) {
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data", reviewService.getReviewsOfMe(memberId, reviewSender));

        return new ResponseEntity<>(body, HttpStatus.OK);
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
                                    HttpSession session) {
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data", reviewService.getReview(memberId, itemId, reviewSort));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
