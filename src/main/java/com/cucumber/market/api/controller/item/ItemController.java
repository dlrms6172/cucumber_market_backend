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

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };

        itemDto.setPostDate(LocalDateTime.now());
        body.put("data", itemService.addItem(memberId, itemDto));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity getItem(@PathVariable(name = "itemId") Integer itemId) {

        body.put("data", itemService.getItem(itemId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/{itemId}")
    public ResponseEntity modifyItem(@Valid @RequestBody ItemDto.modifyItemDto itemDto,
                                     @PathVariable(name = "itemId") Integer itemId,
                                     @RequestHeader(name = "memberId") Integer memberId) {

        itemDto.setUpdateDate(LocalDateTime.now());
        itemDto.setItemId(itemId);
        body.put("data", itemService.modifyItem(memberId, itemId, itemDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/{itemId}/status")
    public ResponseEntity modifyItemStatus(@Valid @RequestBody ItemDto.modifyItemStatusDto dto,
                                           @PathVariable(name = "itemId") Integer itemId,
                                           @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.modifyItemStatus(memberId, itemId, dto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity getItems(@RequestHeader(name = "memberId") int memberId,
                                   @Nullable @RequestParam("name") String itemName,
                                   @Nullable @RequestParam("status") ItemStatus itemStatus) {

        UserDto.userProfileGet userDto = new UserDto.userProfileGet();
        userDto.setMemberId(memberId);
        body.put("data", itemService.getItems(userDto, itemName, itemStatus));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteItem(@PathVariable(name = "itemId") Integer itemId,
                                     @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.deleteItem(memberId, itemId));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    @PostMapping("/{itemId}/like")
    public ResponseEntity addLike(@PathVariable(name = "itemId") Integer itemId,
                                  @RequestHeader(name = "memberId") Integer memberId) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };

        body.put("data", itemService.addLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @DeleteMapping("/{itemId}/like")
    public ResponseEntity deleteLike(@PathVariable(name = "itemId") Integer itemId,
                                     @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.deleteLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/{itemId}/review")
    public ResponseEntity modifyReview(@PathVariable(name = "itemId") Integer itemId,
                                       @RequestHeader(name = "memberId") Integer memberId,
                                       @RequestBody ItemDto.reviewDto reviewDto) {

        body.put("data", itemService.modifyReview(itemId, memberId, reviewDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}/review")
    public ResponseEntity deleteReview(@PathVariable(name = "itemId") Integer itemId,
                                       @RequestHeader(name = "memberId") Integer memberId) {

        body.put("data", itemService.deleteReview(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }
}
