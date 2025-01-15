package com.cucumber.market.api.mapper.region;

import com.cucumber.market.api.dto.request.RegionDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface RegionMapper {

    int insertRegionAdd(List<RegionDto.RegionAddDto> dto);

    List<String> selectLevel1();

    List<String> selectLevel2(RegionDto.Level2Dto dto);

    List<String> selectLevel3(RegionDto.Level3Dto dto);

    List<String> selectLevel4(RegionDto.Level4Dto dto);

    List<String> selectLevel5(RegionDto.Level5Dto dto);

    Map selectRegionId(RegionDto.IdDto dto);
}
