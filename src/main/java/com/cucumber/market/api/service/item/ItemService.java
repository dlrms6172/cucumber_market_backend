package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Map addItem(Long memberId, ItemDto.addItemDto itemDto) throws DataAccessException {
        LinkedHashMap<String, Long> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 등록 권한 확인
        ItemDto.addItemDto savedItem = itemRepository.insertItem(memberId, itemDto);
        result.put("itemId", savedItem.getItemId());
        return result;
    }

    @Transactional(readOnly = true)
    public Map getItem(Long itemId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        itemRepository.updateViewCount(itemId);  //조회 수 증가
        result.put("item", itemRepository.selectItem(itemId));

        return result;
    }

    @Transactional
    public Map modifyItem(Long memberId, Long itemId, ItemDto.modifyItemDto itemDto) {
        LinkedHashMap<String, Long> result = new LinkedHashMap<>();

        //userMapper.findById(memberId); -> 상품 수정 권한 확인

        itemRepository.updateItem(itemId, itemDto);
        result.put("itemId", itemDto.getItemId());

        return result;
    }
}
