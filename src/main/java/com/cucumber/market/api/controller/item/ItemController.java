package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.service.item.ItemService;
import com.cucumber.market.api.service.item.ItemStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private Map<String, Object> body = new LinkedHashMap<>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };


    @PostMapping
    public ResponseEntity addItem(@Valid @RequestBody ItemDto.addItemDto itemDto,
                                  @RequestHeader(name = "memberId") Integer memberId) {

        body.put("resultCode", 201);
        itemDto.setPostDate(LocalDateTime.now());
        body.put("data", itemService.addItem(memberId, itemDto));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @GetMapping("/{item_id}")
    public ResponseEntity getItem(@PathVariable(name = "item_id") Integer itemId) {

        body.put("data", itemService.getItem(itemId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/{item_id}")
    public ResponseEntity modifyItem(@Valid @RequestBody ItemDto.modifyItemDto itemDto,
                                     @PathVariable(name = "item_id") Integer itemId,
                                     @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.modifyItem(memberId, itemId, itemDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/{item_id}/status")
    public ResponseEntity modifyItemStatus(@Valid @RequestBody ItemDto.modifyItemStatusDto dto,
                                           @PathVariable(name = "item_id") Integer itemId,
                                           @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.modifyItemStatus(memberId, itemId, dto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity getItems(@RequestHeader(name = "memberId") int memberId, @Valid UserDto.userProfileGet dto,
                                   @Nullable @RequestParam("name") String itemName,
                                   @Nullable @RequestParam("status") ItemStatus itemStatus) {

        dto.setMemberId(memberId);
        body.put("data", itemService.getItems(dto, itemName, itemStatus));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/{item_id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item_id") Integer itemId,
                                     @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.deleteItem(memberId, itemId));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    @PostMapping("/{item_id}/like")
    public ResponseEntity addLike(@PathVariable(name = "item_id") Integer itemId,
                                  @RequestHeader(name = "memberId") Integer memberId) {

        body.put("resultCode", 201);
        body.put("data", itemService.addLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @DeleteMapping("/{item_id}/like")
    public ResponseEntity deleteLike(@PathVariable(name = "item_id") Integer itemId,
                                     @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.deleteLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/{item_id}/review")
    public ResponseEntity modifyReview(@PathVariable(name = "item_id") Integer itemId,
                                       @RequestHeader(name = "memberId") Integer memberId,
                                       @RequestBody ItemDto.reviewDto reviewDto) {

        body.put("data", itemService.modifyReview(itemId, memberId, reviewDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }

    @DeleteMapping("/{item_id}/review")
    public ResponseEntity deleteReview(@PathVariable(name = "item_id") Integer itemId,
                                       @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.deleteReview(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }
}
