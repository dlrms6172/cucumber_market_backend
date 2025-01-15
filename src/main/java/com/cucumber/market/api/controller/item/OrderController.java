package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.service.item.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{itemId}/order")
    public ResponseEntity addOrder(@PathVariable(name = "itemId") int itemId,
                                   @AuthenticationPrincipal Integer memberId) {
        orderService.addOrder(itemId, memberId);
        return CustomResponse.ok();
    }

    @GetMapping("/{itemId}/orders")
    public ResponseEntity getOrders(@PathVariable(name = "itemId") int itemId,
                                    @AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(orderService.getOrders(itemId, memberId));
    }

    @DeleteMapping("/{itemId}/order")
    public ResponseEntity deleteOrder(@PathVariable(name = "itemId") int itemId,
                                      @AuthenticationPrincipal Integer memberId) {
        orderService.deleteOrder(itemId, memberId);
        return CustomResponse.ok();
    }
}
