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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private Map<String, Object> body = new LinkedHashMap<>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };


    @PostMapping("/items")
    public ResponseEntity addItem(@RequestPart(value = "files") List<MultipartFile> files,
                                  @RequestPart(value = "addItemRequest") ItemDto.addItemDto itemDto,
                                  @RequestHeader(name = "memberId") int memberId) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };

        itemDto.setPostDate(LocalDateTime.now());
        body.put("data", itemService.addItem(memberId, itemDto, files));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @GetMapping("/items/{itemId}")
    public ResponseEntity getItem(@PathVariable(name = "itemId") int itemId) {

        body.put("data", itemService.getItem(itemId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    /**
     * 상품 수정
     * @param files - 사용자가 새로 추가한 이미지들
     * @param itemDto
     * @param itemId
     * @param memberId
     * @return
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity modifyItem(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @RequestPart(value = "modifyItemRequest") ItemDto.modifyItemDto itemDto,
                                     @PathVariable(name = "itemId") int itemId,
                                     @RequestHeader(name = "memberId") int memberId) {

        itemDto.setUpdateDate(LocalDateTime.now());
        body.put("data", itemService.modifyItem(memberId, itemId, itemDto, files));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/items/{itemId}/status")
    public ResponseEntity modifyItemStatus(@Valid @RequestBody ItemDto.modifyItemStatusDto dto,
                                           @PathVariable(name = "itemId") int itemId,
                                           @RequestHeader(name = "memberId") int memberId) {

        body.put("data", itemService.modifyItemStatus(memberId, itemId, dto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @GetMapping("/items")
    public ResponseEntity getItems(@RequestHeader(name = "memberId") int memberId) {

        UserDto.userProfileGet userDto = new UserDto.userProfileGet();
        userDto.setMemberId(memberId);
        body.put("data", itemService.getItems(userDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestHeader(name = "memberId") int memberId,
                                      @RequestParam("name") String itemName,
                                      @Nullable @RequestParam("status") ItemStatus itemStatus) {

        UserDto.userProfileGet userDto = new UserDto.userProfileGet();
        userDto.setMemberId(memberId);
        body.put("data", itemService.searchItems(userDto, itemName, itemStatus));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable(name = "itemId") int itemId,
                                     @RequestHeader(name = "memberId") int memberId) {

        body.put("data", itemService.deleteItem(memberId, itemId));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    @PostMapping("/items/{itemId}/like")
    public ResponseEntity addLike(@PathVariable(name = "itemId") int itemId,
                                  @RequestHeader(name = "memberId") int memberId) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };

        body.put("data", itemService.addLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @DeleteMapping("/items/{itemId}/like")
    public ResponseEntity deleteLike(@PathVariable(name = "itemId") int itemId,
                                     @RequestHeader(name = "memberId") int memberId) {

        body.put("data", itemService.deleteLike(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/items/{itemId}/review")
    public ResponseEntity modifyReview(@PathVariable(name = "itemId") int itemId,
                                       @RequestHeader(name = "memberId") int memberId,
                                       @RequestBody ItemDto.reviewDto reviewDto) {

        body.put("data", itemService.modifyReview(itemId, memberId, reviewDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/items/{itemId}/review")
    public ResponseEntity deleteReview(@PathVariable(name = "itemId") int itemId,
                                       @RequestHeader(name = "memberId") int memberId) {

        body.put("data", itemService.deleteReview(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PostMapping("/items/{itemId}/order")
    public ResponseEntity addOrder(@PathVariable(name = "itemId") int itemId,
                                   @RequestHeader(name = "memberId") int memberId) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };

        body.put("data", itemService.addOrder(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @GetMapping("/items/{itemId}/orders")
    public ResponseEntity getOrders(@PathVariable(name = "itemId") int itemId,
                                    @RequestHeader(name = "memberId") int memberId) {

        body.put("data", itemService.getOrders(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/items/{itemId}/order")
    public ResponseEntity deleteOrder(@PathVariable(name = "itemId") int itemId,
                                      @RequestHeader(name = "memberId") int memberId) {

        body.put("data", itemService.deleteOrder(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }
}
