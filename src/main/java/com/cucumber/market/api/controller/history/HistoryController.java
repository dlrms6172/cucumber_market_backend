package com.cucumber.market.api.controller.history;

import com.cucumber.market.api.common.handler.MemberSessionHandler;
import com.cucumber.market.api.service.history.HistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private HttpHeaders headers;

    private Map<String, Object> body = new LinkedHashMap<String, Object>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };

    @Autowired
    HistoryService historyService;
    @Autowired
    MemberSessionHandler memberSessionHandler;

    @GetMapping("/sales")
    public ResponseEntity sales(HttpSession session, @RequestParam(required = false) Integer itemStatusId){
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data",historyService.sales(memberId, itemStatusId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/purchases")
    public ResponseEntity purchases(HttpSession session){
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data",historyService.purchases(memberId));

        return new ResponseEntity(body,headers,HttpStatus.OK);
    }

    @GetMapping("/interests")
    public ResponseEntity interests(HttpSession session){
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data",historyService.interests(memberId));

        return new ResponseEntity(body, headers,HttpStatus.OK);
    }

    @GetMapping("/itemStatus")
    public ResponseEntity itemStatus(HttpSession session){
        int memberId = memberSessionHandler.getMemberIdFromSession(session);
        body.put("data",historyService.itemStatus(memberId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
