package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemRequestDto;
import com.cucumber.market.api.mapper.item.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;

    public Long addItem(Long memberId, ItemRequestDto itemRequestDto) {
        //userMapper.findById(memberId); -> 상품 등록 권한 확인
        itemMapper.insertItem(memberId, itemRequestDto);

        return itemRequestDto.getItemId();
    }

    public Map getItem(Long itemId) {
        itemMapper.updateViewCount(itemId);
        return itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
    }
}
