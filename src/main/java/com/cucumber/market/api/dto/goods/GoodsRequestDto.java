package com.cucumber.market.api.dto.goods;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsRequestDto {

    private String goodsName;
    private String goodsInfo;
    private LocalDateTime postDate;
    private LocalDateTime updateDate;
    private int price;

    public GoodsRequestDto(String goodsName, String goodsInfo, int price) {
        this.goodsName = goodsName;
        this.goodsInfo = goodsInfo;
        this.price = price;
    }
}
