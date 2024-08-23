//package com.cucumber.market.api.service.item;
//
//import com.cucumber.market.api.dto.item.ItemDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class ItemServiceTest {
//
//    @Autowired
//    ItemService itemService;
//
//    @Test
//    @DisplayName("정상 상품 등록")
//    void addItem() {
//
//        //given
//        Long memberId = 1L;
//        ItemDto.addItemDto itemDto = new ItemDto.addItemDto();
//        itemDto.setItemName("복숭아");
//        itemDto.setItemInfo("싱싱한 복숭아입니다.");
//        itemDto.setPrice(20000);
//
//        //when
//        Map result = itemService.addItem(memberId, itemDto);
//
//        //then
//        Map foundItem = itemService.getItem((Long) result.get("itemId"));
//        assertThat(foundItem.get("itemName")).isEqualTo("복숭아");
//    }
//
//    @Test
//    @DisplayName("정상 개별 상품 조회 - 조회수 증가")
//    void getItem() {
//
//        //given
//        Long memberId = 1L;
//        ItemDto.addItemDto itemDto = new ItemDto.addItemDto();
//        itemDto.setItemName("사과");
//        itemDto.setItemInfo("아삭한 사과입니다.");
//        itemDto.setPrice(10000);
//
//        Map result = itemService.addItem(memberId, itemDto);
//
//        //when
//        Map foundItem = itemService.getItem((Long) result.get("itemId"));
//
//        //then
//        assertThat(foundItem.get("viewCount")).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("정상 상품 수정")
//    void modifyItem() {
//
//        //given
//        Long memberId = 1L;
//        Long itemId = 6L;
//        ItemDto.modifyItemDto itemDto = new ItemDto.modifyItemDto();
//        itemDto.setItemName("맛있는 복숭아");
//        itemDto.setItemInfo("아삭한 복숭아입니다.");
//        itemDto.setPrice(8000);
//
//        //when
//        Map result = itemService.getItem(itemId);
//        Object item = result.get("item");
//
//
//        Map modifiedItem = itemService.modifyItem(memberId, itemId, itemDto);
//
//        //then
//        modifiedItem.get("itemId");
//
//        result.get("itemId");
//        ItemDto item = (ItemDto) result.get("item");
//
//
//        Map modifiedItem = itemService.getItem(item.getItemId());
//        assertThat(modifiedItem.get("itemName")).isEqualTo("맛있는 복숭아");
//    }
//}