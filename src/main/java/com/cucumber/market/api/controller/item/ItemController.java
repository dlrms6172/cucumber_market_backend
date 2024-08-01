package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.dto.item.ItemRequestDto;
import com.cucumber.market.api.service.item.ItemService;
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
    public ResponseEntity addItem(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                  @SessionAttribute Long memberId) {  //추후 수정

        Map<String, Object> body = new LinkedHashMap<String, Object>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
                put("itemId", itemService.addItem(memberId, itemRequestDto));
            }
        };

        return new ResponseEntity(body, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity getItem(@PathVariable Long itemId, @SessionAttribute Long memberId) {

        Map<String, Object> body = new LinkedHashMap<String, Object>() {
            {
                put("resultCode", 200);
                put("resultMsg", "success");
                put("itemInfo", itemService.getItem(itemId));
            }
        };

        return new ResponseEntity(body, HttpStatus.OK);
    }
}
