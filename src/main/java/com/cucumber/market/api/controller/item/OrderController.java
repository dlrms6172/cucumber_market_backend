package com.cucumber.market.api.controller.item;

import com.cucumber.market.api.service.item.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private Map<String, Object> body = new LinkedHashMap<>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };


    @PostMapping("/{itemId}/order")
    public ResponseEntity addOrder(@PathVariable(name = "itemId") int itemId,
                                   @RequestHeader(name = "memberId") int memberId) {

        Map<String, Object> body = new LinkedHashMap<>() {
            {
                put("resultCode", 201);
                put("resultMsg", "success");
            }
        };

        body.put("data", orderService.addOrder(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @GetMapping("/{itemId}/orders")
    public ResponseEntity getOrders(@PathVariable(name = "itemId") int itemId,
                                    @RequestHeader(name = "memberId") int memberId) {

        body.put("data", orderService.getOrders(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }


    @DeleteMapping("/{itemId}/order")
    public ResponseEntity deleteOrder(@PathVariable(name = "itemId") int itemId,
                                      @RequestHeader(name = "memberId") int memberId) {

        body.put("data", orderService.deleteOrder(itemId, memberId));

        return new ResponseEntity(body, HttpStatus.OK);
    }
}
