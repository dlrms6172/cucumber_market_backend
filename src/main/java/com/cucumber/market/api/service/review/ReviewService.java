package com.cucumber.market.api.service.review;

import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.service.user.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ItemMapper itemMapper;
    private final ProfileImageService profileImageService;

    public Map getReviewsOfMe(Integer memberId, ReviewSender reviewSender) {  //내가 받은 후기
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        if (reviewSender == ReviewSender.ALL) {  //내가 받은 후기 전체
            result.put("sellerReviews", getSellerReviews(memberId));
            result.put("buyerReviews", getBuyerReviews(memberId));

        } else if (reviewSender == ReviewSender.SELLER) {  //나에게 물건을 판 판매자가 보낸 후기
            result.put("sellerReviews", getSellerReviews(memberId));

        } else if (reviewSender == ReviewSender.BUYER) {  //나에게 물건을 산 구매자가 보낸 후기
            result.put("buyerReviews", getBuyerReviews(memberId));

        }

        return result;
    }


    private List<Map> getBuyerReviews(Integer memberId) {
        List<Map> buyerReviews = itemMapper.selectBuyerReviews(memberId);
        putReviewWriterInfo(buyerReviews);
        return buyerReviews;
    }


    private List<Map> getSellerReviews(Integer memberId) {
        List<Map> sellerReviews = itemMapper.selectSellerReviews(memberId);
        putReviewWriterInfo(sellerReviews);
        return sellerReviews;
    }


    private void putReviewWriterInfo(List<Map> reviews) {
        for (Map review : reviews) {
            Integer memberId = (Integer) review.get("memberId");
            Map member = itemMapper.selectUserMainInfo(memberId).orElseThrow();
            member.put("profileImageUrl", profileImageService.getImageUrl(memberId));

            //응답 값 생성
            review.remove("memberId");
            review.put("reviewWriter", member);
        }
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

        Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow();
        Integer buyerMemberId = (Integer) buyerReview.get("memberId");

        boolean amIBuyer = buyerMemberId.equals(memberId);
        boolean isFromMe = reviewSort.getBoolean();

        String counterpartName = getCounterpartName(itemId, buyerMemberId, amIBuyer);

        if (amIBuyer == isFromMe) {
            buyerReview.remove("memberId");
            result.put("review", buyerReview);
        } else {
            result.put("review", itemMapper.selectSellerReview(itemId));
        }
        result.put("counterpartName", counterpartName);

        return result;
    }


    /**
     * 거래 상대자 이름 조회
     * @param itemId
     * @param buyerMemberId
     * @param amIBuyer
     * @return counterpartName
     */
    private String getCounterpartName(Integer itemId, Integer buyerMemberId, boolean amIBuyer) {
        Map counterpartInfo;

        if (amIBuyer) {
            Map item  = itemMapper.selectItem(itemId).orElseThrow();
            Integer sellerId = (Integer) item.get("memberId");
            counterpartInfo = itemMapper.selectUserMainInfo(sellerId).orElseThrow();
        } else {
            counterpartInfo = itemMapper.selectUserMainInfo(buyerMemberId).orElseThrow();
        }

        return (String) counterpartInfo.get("name");
    }

}
