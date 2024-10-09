package com.cucumber.market.api.dto.item;

import com.cucumber.market.api.service.item.ItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemDto {

    @Getter
    @Setter
    public static class addItemDto {
        private Integer itemId;
        @NotBlank
        private String itemName;
        @NotBlank
        private String itemInfo;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime postDate;
        @Positive
        private int price;
    }

    @Getter
    @Setter
    public static class modifyItemDto {
        private Integer itemId;
        @NotBlank
        private String itemName;
        @NotBlank
        private String itemInfo;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updateDate;
        @Positive
        private int price;
    }

    @Getter
    @Setter
    public static class modifyItemStatusDto {
        @NotNull
        private ItemStatus itemStatus;
        private Integer clientId;
        private BigDecimal incSellerMannersTemperature;
        private BigDecimal incBuyerMannersTemperature;
    }

    @Getter
    @Setter
    public static class reviewDto {
        @NotBlank
        private String review;
    }

}
