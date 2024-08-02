package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    @DisplayName("정상 상품 등록")
    void addItem() {

        //given
        Long memberId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setItemName("복숭아");
        itemRequestDto.setItemInfo("싱싱한 복숭아입니다.");
        itemRequestDto.setPrice(20000);

        //when
        Long savedItemId = itemService.addItem(memberId, itemRequestDto);

        //then
        Map foundItem = itemService.getItem(savedItemId);
        assertThat(foundItem.get("itemName")).isEqualTo("복숭아");


    }

    @Test
    @DisplayName("정상 개별 상품 조회 - 조회수 증가")
    void getItem() {

        //given
        Long memberId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setItemName("사과");
        itemRequestDto.setItemInfo("아삭한 사과입니다.");
        itemRequestDto.setPrice(10000);

        Long savedItemId = itemService.addItem(memberId, itemRequestDto);

        //when
        Map foundItem = itemService.getItem(savedItemId);

        //then
        assertThat(foundItem.get("viewCount")).isEqualTo(1);
    }
}