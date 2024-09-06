package com.cucumber.market.api.service.region;

import com.cucumber.market.api.dto.region.RegionDto;
import com.cucumber.market.api.mapper.region.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegionService {

    @Autowired
    RegionMapper regionMapper;

    public Map level1() {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<String> selectLevel1 = regionMapper.selectLevel1();

        result.put("level1",selectLevel1);

        return result;
    }

    public Map level2(RegionDto.level2 dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<String> selectLevel2 = regionMapper.selectLevel2(dto);

        result.put("level2",selectLevel2);

        return result;
    }

    public Map level3(RegionDto.level3 dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<String> selectLevel3 = regionMapper.selectLevel3(dto);

        result.put("level3",selectLevel3);

        return result;
    }

    public Map level4(RegionDto.level4 dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<String> selectLevel4 = regionMapper.selectLevel4(dto);

        result.put("level4",selectLevel4);

        return result;
    }

    public Map level5(RegionDto.level5 dto) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<String> selectLevel5 = regionMapper.selectLevel5(dto);

        result.put("level5",selectLevel5);

        return result;
    }
    
    public Map id(RegionDto.id dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        Map selectRegionId = regionMapper.selectRegionId(dto);

        result.put("id",selectRegionId.get("regionId"));

        return result;
    }
}
