package com.cucumber.market.api.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequestDto {

    private Long itemId;
    @NotBlank
    private String itemName;
    @NotBlank
    private String itemInfo;
    private LocalDateTime postDate;
    private LocalDateTime updateDate;
    @NotBlank
    private int price;
}
