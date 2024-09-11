package com.cucumber.market.api.controller.history;

import com.cucumber.market.api.dto.history.HistoryDto;
import com.cucumber.market.api.service.history.HistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity sales(@Valid HistoryDto.sales dto){

        body.put("data",historyService.sales(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/purchases")
    public ResponseEntity purchases(@Valid HistoryDto.purchase dto){

        body.put("data",historyService.purchases(dto));

        return new ResponseEntity(body,headers,HttpStatus.OK);
    }

    @GetMapping("/interests")
    public ResponseEntity interests(@Valid HistoryDto.interests dto){

        body.put("data",historyService.interests(dto));

        return new ResponseEntity(body, headers,HttpStatus.OK);
    }

    @GetMapping("/itemStatus")
    public ResponseEntity itemStatus(@Valid HistoryDto.itemStatus dto){

        body.put("data",historyService.itemStatus(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
