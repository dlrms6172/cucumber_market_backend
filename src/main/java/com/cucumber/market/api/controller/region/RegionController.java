package com.cucumber.market.api.controller.region;

import com.cucumber.market.api.dto.region.RegionDto;
import com.cucumber.market.api.service.region.RegionService;
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
@RequestMapping("/region")
public class RegionController {

    private HttpHeaders headers;

    private Map<String, Object> body = new LinkedHashMap<String, Object>() {
        {
            put("resultCode", 200);
            put("resultMsg", "success");
        }
    };

    @Autowired
    RegionService regionService;

    @GetMapping("/level1")
    public ResponseEntity level1() {

        body.put("data",regionService.level1());

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/level2")
    public ResponseEntity level2(@Valid RegionDto.level2 dto) {

        body.put("data",regionService.level2(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/level3")
    public ResponseEntity level3(@Valid RegionDto.level3 dto){

        body.put("data",regionService.level3(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/level4")
    public ResponseEntity level4(@Valid RegionDto.level4 dto){

        body.put("data",regionService.level4(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/level5")
    public ResponseEntity level5(@Valid RegionDto.level5 dto){

        body.put("data",regionService.level5(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity id(@Valid RegionDto.id dto) {

        body.put("data",regionService.id(dto));

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

}
