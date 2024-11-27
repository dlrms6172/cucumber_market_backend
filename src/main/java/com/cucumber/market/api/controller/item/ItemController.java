package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.dto.item.ItemDto;
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
                                  @SessionAttribute Integer memberId) {

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
     * @return
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity modifyItem(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @RequestPart(value = "modifyItemRequest") ItemDto.modifyItemDto itemDto,
                                     @PathVariable(name = "itemId") int itemId,
                                     @SessionAttribute Integer memberId) {
        itemDto.setUpdateDate(LocalDateTime.now());
        body.put("data", itemService.modifyItem(memberId, itemId, itemDto, files));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @PutMapping("/items/{itemId}/status")
    public ResponseEntity modifyItemStatus(@Valid @RequestBody ItemDto.modifyItemStatusDto dto,
                                           @PathVariable(name = "itemId") int itemId,
                                           @SessionAttribute Integer memberId) {
        body.put("data", itemService.modifyItemStatus(memberId, itemId, dto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @GetMapping("/items")
    public ResponseEntity getItems(@SessionAttribute Integer memberId) {
        body.put("data", itemService.getItems(memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestParam("name") String itemName,
                                      @Nullable @RequestParam("status") ItemStatus itemStatus,
                                      @SessionAttribute Integer memberId) {
        body.put("data", itemService.searchItems(memberId, itemName, itemStatus));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable(name = "itemId") int itemId,
                                     @SessionAttribute Integer memberId) {
        body.put("data", itemService.deleteItem(memberId, itemId));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    @PutMapping("/items/{itemId}/review")
    public ResponseEntity modifyReview(@PathVariable(name = "itemId") int itemId,
                                       @RequestBody ItemDto.reviewDto reviewDto,
                                       @SessionAttribute Integer memberId) {
        body.put("data", itemService.modifyReview(itemId, memberId, reviewDto));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/items/{itemId}/review")
    public ResponseEntity deleteReview(@PathVariable(name = "itemId") int itemId,
                                       @SessionAttribute Integer memberId) {
        body.put("data", itemService.deleteReview(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }

}
