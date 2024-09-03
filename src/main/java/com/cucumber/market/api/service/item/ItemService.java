package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.mapper.item.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemService {

    private final ItemMapper itemMapper;

    public Map addItem(Long memberId, ItemDto.addItemDto itemDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 등록 권한 확인

        itemMapper.insertItem(memberId, itemDto);
        result.put("item", itemDto);

        return result;
    }


    @Transactional(readOnly = true)
    public Map getItem(Long itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.updateViewCount(itemId);  //조회 수 증가
        result.put("item", itemMapper.selectItem(itemId));

        return result;
    }


    public Map modifyItem(Long memberId, Long itemId, ItemDto.modifyItemDto itemDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId.intValue())) {  //상품 수정 권한 확인

            itemMapper.updateItem(itemId, itemDto);
            result.put("item", itemDto);
        }

        return result;
    }


    public Map modifyItemStatus(Long memberId, Long itemId, ItemDto.modifyItemStatusDto itemDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        ItemStatus itemStatus = itemDto.getItemStatus();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId.intValue())) {  //상품 상태 수정 권한 확인

            itemMapper.deleteBuyerReview(itemId);  //기존 구매자 리뷰 삭제

            if (itemStatus == ItemStatus.CLOSED) {  //거래 완료 시 구매자 후기 테이블에도 등록
                itemMapper.insertBuyerReview(itemId, itemDto.getClientId(), null);
            }

            itemMapper.updateItemStatus(itemId, itemStatus);
            result.put("itemId", itemId);
            result.put("itemStatus", itemStatus);
        }

        return result;
    }


    @Transactional(readOnly = true)
    public Map getItems(String itemName, ItemStatus itemStatus) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put("items", itemMapper.selectItems(itemName, itemStatus));

        return result;
    }


    public Map deleteItem(Long memberId, Long itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId.intValue())) {  //상품 삭제 권한 확인

            itemMapper.deleteItem(itemId);
            result.put("itemId", itemId);
        }

        return result;
    }


    public Map addLike(Long itemId, Long memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 좋아요 등록 권한 확인

        itemMapper.insertLike(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }


    public Map deleteLike(Long itemId, Long memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 좋아요 삭제 권한 확인

        itemMapper.deleteLike(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }


    public Map addReview(Long itemId, Long memberId, ItemDto.reviewDto reviewDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
        if (item.get("memberId").equals(memberId.intValue())) {  //판매자 여부 판별

            itemMapper.insertSellerReview(itemId, reviewDto);
        } else {
            Map buyerReview = itemMapper.selectBuyerReview(itemId).orElseThrow(IllegalArgumentException::new);

            if (!buyerReview.get("memberId").equals(memberId.intValue())) {   //구매자 여부 판별
                //throw Exception
            }
            itemMapper.updateBuyerReview(itemId, reviewDto);

            result.put("itemId", itemId);
            result.put("review", reviewDto.getReview());
        }

        return result;
    }
}

