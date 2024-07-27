package com.cucumber.market.api.service.goods;

import com.cucumber.market.api.dto.goods.GoodsRequestDto;
import com.cucumber.market.api.mapper.goods.GoodsMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoodsServiceTest {

    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    GoodsService goodsService;

    @Test
    @DisplayName("정상 상품 등록")
    void addGoods() {

        //given
        GoodsRequestDto goodsRequestDto = new GoodsRequestDto("복숭아", "싱싱한 복숭아입니다.", 10000);

        //when
        goodsService.addGoods(goodsRequestDto, 1L);

        //then

    }
}