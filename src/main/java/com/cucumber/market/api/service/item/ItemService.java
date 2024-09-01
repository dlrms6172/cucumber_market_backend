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
public class ItemService {

    private final ItemMapper itemMapper;

    @Transactional
    public Map addItem(Long memberId, ItemDto.addItemDto itemDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 등록 권한 확인

        itemMapper.insertItem(memberId, itemDto);
        result.put("item", itemDto);

        return result;
    }

    @Transactional
    public Map getItem(Long itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemMapper.updateViewCount(itemId);  //조회 수 증가
        result.put("item", itemMapper.selectItem(itemId));

        return result;
    }

    @Transactional
    public Map modifyItem(Long memberId, Long itemId, ItemDto.modifyItemDto itemDto) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 수정 권한 확인

        itemMapper.updateItem(itemId, itemDto);
        result.put("item", itemDto);

        return result;
    }

    public Map modifyItemStatus(Long memberId, Long itemId, ItemStatus itemStatus) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 상태 수정 권한 확인

        itemMapper.updateItemStatus(itemId, itemStatus);
        result.put("itemId", itemId);
        result.put("itemStatus", itemStatus);

        return  result;
    }

    public Map deleteItem(Long memberId, Long itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 삭제 권한 확인

        itemMapper.deleteItem(itemId);
        result.put("itemId", itemId);

        return result;
    }
 }
