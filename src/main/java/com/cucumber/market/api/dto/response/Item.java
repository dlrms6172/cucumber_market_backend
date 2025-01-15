package com.cucumber.market.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private Integer itemId;
    private String itemName;
    private String itemInfo;
    private Integer donationFlag;
    private Integer price;
    private Integer priceNegotiationYn;
    private Integer itemStatusId;
    private Integer categoryId;
    private Integer viewCount;
    private Integer likeCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateDate;
    private String repImgUrl;
}
