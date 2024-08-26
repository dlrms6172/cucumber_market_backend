package com.cucumber.market.api.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ItemDto {

    @Getter
    @Setter
    public static class addItemDto {
        private Long itemId;
        @NotBlank
        private String itemName;
        @NotBlank
        private String itemInfo;
        private LocalDateTime postDate;
        private int price;
    }

    @Getter
    @Setter
    public static class modifyItemDto {
        private Long itemId;
        @NotBlank
        private String itemName;
        @NotBlank
        private String itemInfo;
        private LocalDateTime updateDate;
        private int price;
    }
}
