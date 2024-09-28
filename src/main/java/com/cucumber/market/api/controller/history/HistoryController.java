package com.cucumber.market.api.controller.history;

import com.cucumber.market.api.service.history.HistoryService;
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

    @GetMapping("/sales")
    public ResponseEntity sales(@RequestHeader(name = "memberId") int memberId, @RequestParam(required = false) Integer itemStatusId){

        body.put("data",historyService.sales(memberId, itemStatusId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/purchases")
    public ResponseEntity purchases(@RequestHeader(name = "memberId") int memberId){

        body.put("data",historyService.purchases(memberId));

        return new ResponseEntity(body,headers,HttpStatus.OK);
    }

    @GetMapping("/interests")
    public ResponseEntity interests(@RequestHeader(name = "memberId") int memberId){

        body.put("data",historyService.interests(memberId));

        return new ResponseEntity(body, headers,HttpStatus.OK);
    }

    @GetMapping("/itemStatus")
    public ResponseEntity itemStatus(@RequestHeader(name = "memberId") int memberId){

        body.put("data",historyService.itemStatus(memberId));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
