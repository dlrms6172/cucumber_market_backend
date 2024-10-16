package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.mapper.user.UserMapper;
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
    private final ImageService imageService;

    public Map addItem(Integer memberId, ItemDto.addItemDto itemDto, List<MultipartFile> files) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.insertItem(memberId, itemDto);
        List<String> imageUrls = imageService.addImages(itemDto.getItemId(), files);

        result.put("item", itemDto);
        result.put("imageUrls", imageUrls);

        return result;
    }


    public Map getItem(Integer itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.updateViewCount(itemId);  //조회 수 증가
        result.put("item", itemMapper.selectItem(itemId));
        result.put("imageUrls", imageService.getImageUrls(itemId));

        return result;
    }


    public Map modifyItem(Integer memberId, Integer itemId, ItemDto.modifyItemDto itemDto, List<MultipartFile> files) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId)) {  //상품 수정 권한 확인

            itemMapper.updateItem(itemId, itemDto);
            List<String> imageUrls = imageService.updateImages(itemId, itemDto.getUnchangedImageUrls(), files);

            itemDto.setItemId(itemId);
            itemDto.setUnchangedImageUrls(null);
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
    public Map getItems(UserDto.userProfileGet dto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map userInfo = userMapper.selectUserInfo(dto);
        Integer regionId = (Integer) userInfo.get("regionId");
        result.put("items", itemMapper.selectAllItems(regionId));

        return result;
    }


    @Transactional(readOnly = true)
    public Map searchItems(UserDto.userProfileGet dto, String itemName, ItemStatus itemStatus) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map userInfo = userMapper.selectUserInfo(dto);
        Integer regionId = (Integer) userInfo.get("regionId");
        result.put("items", itemMapper.selectItems(regionId, itemName, itemStatus));

        return result;
    }


    public Map deleteItem(Integer memberId, Integer itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId)) {  //상품 삭제 권한 확인

            imageService.deleteImages(itemId);
            itemMapper.deleteItem(itemId);
            result.put("itemId", itemId);
        } else {
            throw new IllegalArgumentException("상품 삭제 권한이 없습니다.");
        }

        return result;
    }


    public Map addLike(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.insertLike(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }


    public Map deleteLike(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map like = itemMapper.selectLike(itemId, memberId).orElseThrow(IllegalArgumentException::new);  //좋아요 존재 및 좋아요 삭제 권한 확인

        itemMapper.deleteLike(itemId, memberId);
        result.put("itemId", itemId);

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


    public Map addOrder(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.insertOrder(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }


    @Transactional(readOnly = true)
    public Map getOrders(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);

        if (item.get("memberId").equals(memberId)) {  //판매자 여부 판별

            result.put("orders", itemMapper.selectOrders(itemId));
        } else {
            throw new IllegalArgumentException("구매를 신청한 사용자들 조회 권한이 없습니다.");

        }

        return result;
    }


    public Map deleteOrder(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map order = itemMapper.selectOrder(itemId, memberId).orElseThrow(IllegalArgumentException::new);  //구매자 신청 존재 및 구매자 신청 삭제 권한 확인

        itemMapper.deleteOrder(itemId, memberId);
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

}


