package com.cucumber.market.api.mapper.region;

import com.cucumber.market.api.dto.region.RegionDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface RegionMapper {

    int insertRegionAdd(List<RegionDto.regionAdd> dto);

    List<String> selectLevel1();

    List<String> selectLevel2(RegionDto.level2 dto);

    List<String> selectLevel3(RegionDto.level3 dto);

    List<String> selectLevel4(RegionDto.level4 dto);

    List<String> selectLevel5(RegionDto.level5 dto);

    Map selectRegionId(RegionDto.id dto);
}
