package com.cucumber.market.api.service.review;

import com.cucumber.market.api.dto.request.ItemDto;
import com.cucumber.market.api.dto.response.ItemDetail;
import com.cucumber.market.api.dto.response.ItemSeller;
import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.mapper.user.UserMapper;
import com.cucumber.market.api.service.user.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ItemMapper itemMapper;
    private final ProfileImageService profileImageService;
    private final UserMapper userMapper;

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
               ItemDetail item = itemMapper.selectItem(itemId).orElseThrow();
               Integer sellerId = item.getMember().getMemberId();
               counterpartInfo = itemMapper.selectUserMainInfo(sellerId).orElseThrow();
           } else {
               counterpartInfo = itemMapper.selectUserMainInfo(buyerMemberId).orElseThrow();
           }

           return (String) counterpartInfo.get("name");
    }

    public void modifyReview(Integer itemId, Integer memberId, ItemDto.ReviewDto reviewDto) {
        ItemDetail item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        Integer itemSellerId = item.getMember().getMemberId();

        if (checkAuthority(memberId, itemSellerId)) {  //판매자 여부 판별
            itemMapper.updateSellerReview(itemId, reviewDto);
        } else {
            Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow(IllegalArgumentException::new);

            if (!checkAuthority(memberId, Integer.parseInt((String) buyerReview.get("memberId")))) {   //구매자 여부 판별
                throw new IllegalArgumentException("상품 후기 수정 권한이 없습니다.");
            }
            itemMapper.updateBuyerReview(itemId, reviewDto);

        }
    }

    public void deleteReview(Integer itemId, Integer memberId) {
        ItemDetail item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        Integer itemSellerId = item.getMember().getMemberId();

        if (checkAuthority(memberId, itemSellerId)) {  //판매자 여부 판별

            itemMapper.deleteSellerReview(itemId);
        } else {
            Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow(IllegalArgumentException::new);

            if (!checkAuthority(memberId,  Integer.parseInt((String) buyerReview.get("memberId")))) {   //구매자 여부 판별
                throw new IllegalArgumentException("상품 후기 삭제 권한이 없습니다.");
            }
            itemMapper.deleteBuyerReview(itemId);

        }
    }

    private static boolean checkAuthority(Integer memberId, Integer itemSellerId) {
        return memberId.equals(itemSellerId);
    }

}
