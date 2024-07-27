package com.cucumber.market.api.controller.goods;

import com.cucumber.market.api.dto.goods.GoodsRequestDto;
import com.cucumber.market.api.dto.goods.GoodsResponseDto;
import com.cucumber.market.api.service.goods.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @PostMapping
    public ResponseEntity addGoods(@RequestBody GoodsRequestDto goodsRequestDto,
                                   @SessionAttribute Long memberId) {  //추후 수정
        goodsService.addGoods(goodsRequestDto, memberId);
        //ResponseEntity 에 넣을 goodsResponseDto 생성

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
