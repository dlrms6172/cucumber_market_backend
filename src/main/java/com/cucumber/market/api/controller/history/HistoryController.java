package com.cucumber.market.api.controller.history;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @GetMapping("/sales")
    public ResponseEntity getSales(@AuthenticationPrincipal Integer memberId, @RequestParam(required = false) Integer itemStatusId){
        return CustomResponse.ok(historyService.getSales(memberId, itemStatusId));
    }

    @GetMapping("/purchases")
    public ResponseEntity getPurchases(@AuthenticationPrincipal Integer memberId){
        return CustomResponse.ok(historyService.getPurchases(memberId));
    }

    @GetMapping("/interests")
    public ResponseEntity getInterests(@AuthenticationPrincipal Integer memberId){
        return CustomResponse.ok(historyService.getInterests(memberId));
    }

    @GetMapping("/itemStatus")
    public ResponseEntity getItemStatus(@AuthenticationPrincipal Integer memberId){
        return CustomResponse.ok(historyService.getItemStatus(memberId));
    }
}
