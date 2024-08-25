package com.cucumber.market.api.repository;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.service.item.ItemStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final ItemMapper itemMapper;

    public ItemDto.addItemDto insertItem(Long memberId, ItemDto.addItemDto itemDto) {
        try {
            itemMapper.insertItem(memberId, itemDto);
            return itemDto;
        } catch (DataAccessException e) {
            log.error(e.getMessage());  //예외 처리
            throw new RuntimeException(e);
        }
    }

    public Map selectItem(Long itemId) {
        return itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);
    }

    public void updateViewCount(Long itemId) {
        try {
            itemMapper.updateViewCount(itemId);
        } catch (DataAccessException e) {

        }
    }

    public void updateItem(Long itemId, ItemDto.modifyItemDto itemDto) {
        try {
            itemMapper.updateItem(itemId, itemDto);
        } catch (DataAccessException e) {

        }
    }
}
