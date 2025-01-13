package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.service.item.ItemService;
import com.cucumber.market.api.service.item.ItemStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity addItem(@RequestPart(value = "files") List<MultipartFile> files,
                                  @RequestPart(value = "addItemRequest") ItemDto.AddItemDto itemDto,
                                  @AuthenticationPrincipal Integer memberId) {
        itemDto.setPostDate(LocalDateTime.now());
        return CustomResponse.created(itemService.addItem(memberId, itemDto, files));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity getItem(@PathVariable(name = "itemId") int itemId) {
        return CustomResponse.ok(itemService.getItem(itemId));
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
                                     @RequestPart(value = "modifyItemRequest") ItemDto.ModifyItemDto itemDto,
                                     @PathVariable(name = "itemId") int itemId,
                                     @AuthenticationPrincipal Integer memberId) {
        itemDto.setUpdateDate(LocalDateTime.now());
        itemService.modifyItem(memberId, itemId, itemDto, files);
        return CustomResponse.ok();
    }

    @PutMapping("/items/{itemId}/status")
    public ResponseEntity modifyItemStatus(@Valid @RequestBody ItemDto.ModifyItemStatusDto dto,
                                           @PathVariable(name = "itemId") int itemId,
                                           @AuthenticationPrincipal Integer memberId) {
        itemService.modifyItemStatus(memberId, itemId, dto);
        return CustomResponse.ok();
    }


    @GetMapping("/items")
    public ResponseEntity getItems(@AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(itemService.getItems(memberId));
    }


    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestParam("name") String itemName,
                                      @Nullable @RequestParam("status") ItemStatus itemStatus,
                                      @AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(itemService.searchItems(memberId, itemName, itemStatus));
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable(name = "itemId") int itemId,
                                     @AuthenticationPrincipal Integer memberId) {
        itemService.deleteItem(memberId, itemId);
        return CustomResponse.ok();
    }


    @PutMapping("/items/{itemId}/review")
    public ResponseEntity modifyReview(@PathVariable(name = "itemId") int itemId,
                                       @RequestBody ItemDto.ReviewDto reviewDto,
                                       @AuthenticationPrincipal Integer memberId) {
        itemService.modifyReview(itemId, memberId, reviewDto);
        return CustomResponse.ok();
    }


    @DeleteMapping("/items/{itemId}/review")
    public ResponseEntity deleteReview(@PathVariable(name = "itemId") int itemId,
                                       @AuthenticationPrincipal Integer memberId) {
        itemService.deleteReview(itemId, memberId);
        return CustomResponse.ok();
    }

}
