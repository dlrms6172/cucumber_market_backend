package com.cucumber.market.api.controller.region;

import com.cucumber.market.api.common.payload.CustomResponse;
import com.cucumber.market.api.dto.request.RegionDto;
import com.cucumber.market.api.service.region.RegionService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/region")
public class RegionController {

    @Autowired
    RegionService regionService;

    @GetMapping("/level1")
    public ResponseEntity level1(@AuthenticationPrincipal Integer memberId) {
        return CustomResponse.ok(regionService.level1());
    }

    @GetMapping("/level2")
    public ResponseEntity level2(@AuthenticationPrincipal Integer memberId, @ParameterObject @Valid RegionDto.Level2Dto dto) {
        return CustomResponse.ok(regionService.level2(dto));
    }

    @GetMapping("/level3")
    public ResponseEntity level3(@AuthenticationPrincipal Integer memberId, @Valid RegionDto.Level3Dto dto){
        return CustomResponse.ok(regionService.level3(dto));
    }

    @GetMapping("/level4")
    public ResponseEntity level4(@AuthenticationPrincipal Integer memberId, @Valid RegionDto.Level4Dto dto){
        return CustomResponse.ok(regionService.level4(dto));
    }

    @GetMapping("/level5")
    public ResponseEntity level5(@AuthenticationPrincipal Integer memberId, @Valid RegionDto.Level5Dto dto){
        return CustomResponse.ok(regionService.level5(dto));

    }

    @GetMapping("/id")
    public ResponseEntity id(@AuthenticationPrincipal Integer memberId, @Valid RegionDto.IdDto dto) {
        return CustomResponse.ok(regionService.id(dto));
    }

}
