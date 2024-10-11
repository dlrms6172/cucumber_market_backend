package com.cucumber.market.api.dto.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private Integer imageId;
    private Integer itemId;
    private String originalName;
    private String keyName;

}
