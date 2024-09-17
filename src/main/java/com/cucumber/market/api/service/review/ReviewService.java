package com.cucumber.market.api.service.review;

import com.cucumber.market.api.mapper.item.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ItemMapper itemMapper;

    public Map getReviewsOfMe(Integer memberId, ReviewSender reviewSender) {  //내가 받은 후기
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        if (reviewSender == null) {  //내가 받은 후기 전체
            result.put("sellerReviews", itemMapper.selectSellerReviews(memberId));
            result.put("buyerReviews", itemMapper.selectBuyerReviews(memberId));

        } else if (reviewSender == ReviewSender.SELLER) {  //나에게 물건을 판 판매자가 보낸 후기
            result.put("sellerReviews", itemMapper.selectSellerReviews(memberId));

        } else if (reviewSender == ReviewSender.BUYER) {  //나에게 물건을 산 구매자가 보낸 후기
            result.put("buyerReviews", itemMapper.selectBuyerReviews(memberId));

        }

        return result;
    }


    public Map getReview(Integer memberId, Integer itemId, ReviewSort reviewSort) {

        /*
        상품 별 내가 받은 후기, 내가 보낸 후기 개별 조회

        1. 내가 구매자, 내가 보낸 후기 -> 구매자 후기     amIBuyer(true) == isFromMe(true)
        2. 내가 구매자, 내가 받은 후기 -> 판매자 후기     amIBuyer(true) != isFromMe(false)
        3. 내가 판매자, 내가 보낸 후기 -> 판매자 후기     amIBuyer(false) != isFromMe(true)
        4. 내가 판매자, 내가 받은 후기 -> 구매자 후기     amIBuyer(false) == isFromMe(false)
         */

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow(IllegalArgumentException::new);

        boolean amIBuyer = buyerReview.get("memberId").equals(memberId);
        boolean isFromMe = reviewSort.getBoolean();

        if (amIBuyer == isFromMe) {
            result.put("review", buyerReview);
        } else {
            result.put("review", itemMapper.selectSellerReview(itemId));
        }

        return result;
    }

}
