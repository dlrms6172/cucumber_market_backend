package com.cucumber.market.api.mapper.item;

import com.cucumber.market.api.dto.item.ItemRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@Mapper
public interface ItemMapper {

    int insertItem(@Param("memberId") Long memberId, @Param("request") ItemRequestDto itemRequestDto);

    Optional<Map> selectItem(Long itemId);

    void updateViewCount(Long itemId);

}
