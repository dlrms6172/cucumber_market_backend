package com.cucumber.market.api.mapper.goods;

import com.cucumber.market.api.dto.goods.GoodsRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsMapper {

    void save(@Param("memberId") Long memberId, @Param("request") GoodsRequestDto goodsRequestDto);


}
