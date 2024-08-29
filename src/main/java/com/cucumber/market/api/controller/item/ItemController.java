package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.service.item.ItemService;
import com.cucumber.market.api.service.item.ItemStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity addItem(@Valid @RequestBody ItemDto.addItemDto itemDto,
                                  @SessionAttribute Long memberId) {

        Map<String, Object> body = new LinkedHashMap<String, Object>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
                put("data", itemService.addItem(memberId, itemDto));
            }
        };

        return new ResponseEntity(body, HttpStatus.CREATED);

    }

    @GetMapping("/{item_id}")
    public ResponseEntity getItem(@PathVariable(name = "item_id") Long itemId) {

        Map<String, Object> body = new LinkedHashMap<String, Object>() {
            {
                put("resultCode", 200);
                put("resultMsg", "success");
                put("data", itemService.getItem(itemId));
            }
        };

        return new ResponseEntity(body, HttpStatus.OK);
    }

    @PutMapping("/{item_id}")
    public ResponseEntity modifyItem(@Valid @RequestBody ItemDto.modifyItemDto itemDto,
                                     @PathVariable(name = "item_id") Long itemId,
                                     @SessionAttribute Long memberId) {
        Map<String, Object> body = new LinkedHashMap<String, Object>() {
            {
                put("resultCode", 200);
                put("resultMsg", "success");
                put("data", itemService.modifyItem(memberId, itemId, itemDto));
            }
        };

        return new ResponseEntity(body, HttpStatus.OK);
    }

    @PutMapping("/{item_id}/status")
    public ResponseEntity modifyItemStatus(@Valid @RequestBody Map<String, ItemStatus> itemStatus,
                                           @PathVariable(name = "item_id") Long itemId,
                                           @SessionAttribute Long memberId) {

        Map<String, Object> body = new LinkedHashMap<String, Object>() {
            {
                put("resultCode", 200);
                put("resultMsg", "success");
                put("data", itemService.modifyItemStatus(1L, itemId, itemStatus.get("itemStatus")));
            }
        };

        return new ResponseEntity(body, HttpStatus.OK);
    }
}
