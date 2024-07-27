package com.cucumber.market.api.service.goods;

import com.cucumber.market.api.dto.goods.GoodsRequestDto;
import com.cucumber.market.api.dto.goods.GoodsResponseDto;
import com.cucumber.market.api.mapper.goods.GoodsMapper;
import com.cucumber.market.api.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;
    private final UserMapper userMapper;

    public void addGoods(GoodsRequestDto goodsRequestDto, Long memberId) {
        //userMapper.findById(memberId); -> 상품 등록 권한 확인
        goodsRequestDto.setPostDate(LocalDateTime.now());
        goodsMapper.save(memberId, goodsRequestDto);

        //goodsResponseDto 리턴
    }
}
