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
    public ResponseEntity sales(@AuthenticationPrincipal Integer memberId, @RequestParam(required = false) Integer itemStatusId){
        return CustomResponse.ok(historyService.sales(memberId, itemStatusId));
    }

    @GetMapping("/purchases")
    public ResponseEntity purchases(@AuthenticationPrincipal Integer memberId){
        return CustomResponse.ok(historyService.purchases(memberId));
    }

    @GetMapping("/interests")
    public ResponseEntity interests(@AuthenticationPrincipal Integer memberId){
        return CustomResponse.ok(historyService.interests(memberId));
    }

    @GetMapping("/itemStatus")
    public ResponseEntity itemStatus(@AuthenticationPrincipal Integer memberId){
        return CustomResponse.ok(historyService.itemStatus(memberId));
    }
}
