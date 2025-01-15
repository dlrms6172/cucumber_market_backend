package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.request.ItemDto;
import com.cucumber.market.api.dto.response.Item;
import com.cucumber.market.api.dto.response.ItemDetail;
import com.cucumber.market.api.dto.response.ItemSeller;
import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.mapper.user.UserMapper;
import com.cucumber.market.api.service.user.ProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

    public Integer addItem(Integer memberId, ItemDto.AddItemDto itemDto, List<MultipartFile> itemImgFiles) {
        itemMapper.insertItem(memberId, itemDto);
        itemImageService.addImages(itemDto.getItemId(), itemImgFiles);
        return itemDto.getItemId();
    }

    public ItemDetail getItem(Integer itemId) {
        itemMapper.updateViewCount(itemId);  //조회 수 증가
        ItemDetail item = itemMapper.selectItem(itemId).orElseThrow();
        List<String> itemImgUrls = itemImageService.getImageUrls(itemId);
        item.setItemImageUrls(itemImgUrls);

        ItemSeller.Info member = item.getMember();
        String profileImgUrl = profileImageService.getImageUrl(member.getMemberId());
        member.setProfileImageUrl(profileImgUrl);
        return item;
    }

    public void modifyItem(Integer memberId, Integer itemId, ItemDto.ModifyItemDto itemDto, List<MultipartFile> itemImgFiles) {
        ItemDetail item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        Integer itemSellerId = item.getMember().getMemberId();
        validateItemSeller(memberId, itemSellerId);

        itemMapper.updateItem(itemId, itemDto);
        itemImageService.updateImages(itemId, itemDto.getImageIndexList(), itemImgFiles);
    }

    public void modifyItemStatus(Integer memberId, Integer itemId, ItemDto.ModifyItemStatusDto itemDto) {
        ItemDetail item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        Integer itemSellerId = item.getMember().getMemberId();
        ItemStatus itemStatus = itemDto.getItemStatus();
        validateItemSeller(memberId, itemSellerId);

        if (item.getItemStatusId().equals(ItemStatus.CLOSED.getDbCode())) {  //기존 거래완료 상태인 상품일 경우
            itemMapper.deleteBuyerReview(itemId);  //기존 구매자 리뷰 삭제
            itemMapper.deleteSellerReview(itemId);  //기존 판매자 리뷰 삭제
        }

        if (itemStatus == ItemStatus.CLOSED) {  //상품 상태를 거래완료로 변경할 경우
            itemMapper.insertBuyerReview(itemId, itemDto.getClientId(), null);  //구매자 후기 테이블에 등록
            itemMapper.insertSellerReview(itemId, null);  //판매자 후기 테이블에 등록

            incMannersTemperature(memberId,itemDto);//매너온도 증가 로직
        }

        itemMapper.updateItemStatus(itemId, itemStatus);
    }

    @Transactional(readOnly = true)
    public List<Item> getItems(Integer memberId) {
        Map userInfo = userMapper.selectUserInfo(memberId);
        Integer regionId = (Integer) userInfo.get("regionId");
        List<Item> items = itemMapper.selectAllItems(regionId);

        fetchItemRepImgUrl(items);  //상품 대표 이미지(섬네일)
        return items;
    }

    @Transactional(readOnly = true)
    public List<Item> searchItems(Integer memberId, String itemName, ItemStatus itemStatus) {
        Map userInfo = userMapper.selectUserInfo(memberId);
        Integer regionId = (Integer) userInfo.get("regionId");
        List<Item> items = itemMapper.selectItems(regionId, itemName, itemStatus);

        fetchItemRepImgUrl(items);  //상품 대표 이미지(섬네일)
        return items;
    }

    public void deleteItem(Integer memberId, Integer itemId) {
        ItemDetail item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        Integer itemSellerId = item.getMember().getMemberId();
        validateItemSeller(memberId, itemSellerId);

        itemImageService.deleteAllImages(itemId);
        itemMapper.deleteItem(itemId);
    }

    //매너온도 증가 로직
    private void incMannersTemperature(Integer memberId, ItemDto.ModifyItemStatusDto itemDto){

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

    private void fetchItemRepImgUrl(List<Item> items) {
        for (Item item : items) {
            Integer itemId = item.getItemId();
            String repImageUrl = itemImageService.getRepImageUrl(itemId);
            item.setRepImgUrl(repImageUrl);
        }
    }

    private static void validateItemSeller(Integer memberId, Integer itemSellerId) {
        if (!memberId.equals(itemSellerId)) {
            throw new RuntimeException("권한이 없습니다");
        }
    }


}


