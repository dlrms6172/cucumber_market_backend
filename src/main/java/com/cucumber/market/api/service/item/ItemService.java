package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.mapper.user.UserMapper;
import com.cucumber.market.api.service.user.ProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemService {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final ItemImageService itemImageService;
    private final ProfileImageService profileImageService;

    public Map addItem(Integer memberId, ItemDto.addItemDto itemDto, List<MultipartFile> files) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.insertItem(memberId, itemDto);
        List<String> imageUrls = itemImageService.addImages(itemDto.getItemId(), files);

        result.put("item", itemDto);
        result.put("imageUrls", imageUrls);

        return result;
    }


    public Map getItem(Integer itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.updateViewCount(itemId);  //조회 수 증가
        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);  //상품 조회
        Integer sellerMemberId = (Integer) item.get("memberId");

        Map itemSeller = itemMapper.selectUserMainInfo(sellerMemberId).orElseThrow(IllegalArgumentException::new);  //판매자 조회

        //응답 값 생성
        item.remove("memberId");
        item.put("itemImageUrls", itemImageService.getImageUrls(itemId));
        result.put("item", item);
        itemSeller.put("profileImageUrl", profileImageService.getImageUrl(sellerMemberId));
        result.put("itemSeller", itemSeller);

        return result;
    }


    public Map modifyItem(Integer memberId, Integer itemId, ItemDto.modifyItemDto itemDto, List<MultipartFile> files) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId)) {  //상품 수정 권한 확인

            itemMapper.updateItem(itemId, itemDto);
            List<String> imageUrls = itemImageService.updateImages(itemId, itemDto.getImageIndexList(), files);

            //응답 값 생성
            itemDto.setItemId(itemId);
            itemDto.setImageIndexList(null);
            result.put("item", itemDto);
            result.put("imageUrls", imageUrls);
        } else {
            throw new IllegalArgumentException("상품 수정 권한이 없습니다.");
        }

        return result;
    }


    public Map modifyItemStatus(Integer memberId, Integer itemId, ItemDto.modifyItemStatusDto itemDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        ItemStatus itemStatus = itemDto.getItemStatus();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);

        if (item.get("memberId").equals(memberId)) {  //상품 상태 수정 권한 확인

            if (item.get("itemStatusId").equals(ItemStatus.CLOSED.getDbCode())) {  //기존 거래완료 상태인 상품일 경우
                itemMapper.deleteBuyerReview(itemId);  //기존 구매자 리뷰 삭제
                itemMapper.deleteSellerReview(itemId);  //기존 판매자 리뷰 삭제
            }

            if (itemStatus == ItemStatus.CLOSED) {  //상품 상태를 거래완료로 변경할 경우
                itemMapper.insertBuyerReview(itemId, itemDto.getClientId(), null);  //구매자 후기 테이블에 등록
                itemMapper.insertSellerReview(itemId, null);  //판매자 후기 테이블에 등록

                incMannersTemperature(memberId,itemDto);//매너온도 증가 로직
            }

            itemMapper.updateItemStatus(itemId, itemStatus);

            result.put("itemId", itemId);
            result.put("itemStatus", itemStatus);
            if (itemDto.getClientId() != null) {
                result.put("clientId", itemDto.getClientId());
            }

        } else {
            throw new IllegalArgumentException("상품 상태 수정 권한이 없습니다.");
        }

        return result;
    }


    @Transactional(readOnly = true)
    public Map getItems(Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map userInfo = userMapper.selectUserInfo(memberId);
        Integer regionId = (Integer) userInfo.get("regionId");
        List<Map> items = itemMapper.selectAllItems(regionId);

        putItemRepImages(items);  //상품 대표 이미지(섬네일)
        result.put("items", items);

        return result;
    }


    @Transactional(readOnly = true)
    public Map searchItems(Integer memberId, String itemName, ItemStatus itemStatus) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map userInfo = userMapper.selectUserInfo(memberId);
        Integer regionId = (Integer) userInfo.get("regionId");
        List<Map> items = itemMapper.selectItems(regionId, itemName, itemStatus);

        putItemRepImages(items);  //상품 대표 이미지(섬네일)
        result.put("items", items);

        return result;
    }


    public Map deleteItem(Integer memberId, Integer itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId)) {  //상품 삭제 권한 확인

            itemImageService.deleteAllImages(itemId);
            itemMapper.deleteItem(itemId);
            result.put("itemId", itemId);

        } else {
            throw new IllegalArgumentException("상품 삭제 권한이 없습니다.");
        }

        return result;
    }


    public Map modifyReview(Integer itemId, Integer memberId, ItemDto.reviewDto reviewDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);

        if (item.get("memberId").equals(memberId)) {  //판매자 여부 판별

            itemMapper.updateSellerReview(itemId, reviewDto);
        } else {
            Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow(IllegalArgumentException::new);

            if (!buyerReview.get("memberId").equals(memberId)) {   //구매자 여부 판별
                throw new IllegalArgumentException("상품 후기 수정 권한이 없습니다.");
            }
            itemMapper.updateBuyerReview(itemId, reviewDto);

        }

        result.put("itemId", itemId);
        result.put("review", reviewDto.getReview());

        return result;
    }


    public Map deleteReview(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);

        if (item.get("memberId").equals(memberId)) {  //판매자 여부 판별

            itemMapper.deleteSellerReview(itemId);
        } else {
            Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow(IllegalArgumentException::new);

            if (!buyerReview.get("memberId").equals(memberId)) {   //구매자 여부 판별
                throw new IllegalArgumentException("상품 후기 삭제 권한이 없습니다.");
            }
            itemMapper.deleteBuyerReview(itemId);

        }

        result.put("itemId", itemId);

        return result;
    }


    //매너온도 증가 로직
    public void incMannersTemperature(Integer memberId, ItemDto.modifyItemStatusDto itemDto){

        BigDecimal maxMannersTemperature = new BigDecimal("99");
        BigDecimal stdManTemp = new BigDecimal("0.2"); //매너온도 증가 기준값

        BigDecimal sellerMannersTemperature = (BigDecimal) userMapper.selectSellerMannersTemperature(memberId).get("mannersTemperature"); //판매자:매너온도
        sellerMannersTemperature = sellerMannersTemperature.add(stdManTemp); //판매자 매너온도 + 매너온도 증가 기준값

        BigDecimal buyerMannersTemperature = (BigDecimal) userMapper.selectBuyerMannersTemperature(itemDto).get("mannersTemperature"); //구매자:매너온도
        buyerMannersTemperature = buyerMannersTemperature.add(stdManTemp); //구매자 매너온도 + 매너온도 증가 기준값

        //증가 기준값을 더한 판매자 매너온도가 최대 매너온도보다 클 경우
        if(sellerMannersTemperature.compareTo(maxMannersTemperature) == 1){
            sellerMannersTemperature = new BigDecimal("99");
        }

        //증가 기준값을 더한 구매자 매너온도가 최대 매너온도보다 클 경우
        if(buyerMannersTemperature.compareTo(maxMannersTemperature) == 1){
            buyerMannersTemperature = new BigDecimal("99");
        }

        itemDto.setIncSellerMannersTemperature(sellerMannersTemperature); //판매자:증가된 매너온도 셋팅
        userMapper.updateSellerMannersTemperature(memberId, itemDto); //판매자:매너온도 업데이트

        itemDto.setIncBuyerMannersTemperature(buyerMannersTemperature); //구매자:증가된 매너온도 셋팅
        userMapper.updateBuyerMannersTemperature(itemDto); //구매자:매너온도 업데이트
    }


    private void putItemRepImages(List<Map> items) {
        for (Map item : items) {
            Integer itemId = (Integer) item.get("itemId");
            String repImageUrl = itemImageService.getRepImageUrl(itemId);
            item.put("itemRepImage", repImageUrl);
        }
    }


}


