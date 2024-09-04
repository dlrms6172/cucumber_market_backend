package com.cucumber.market.api.mapper.region;

import com.cucumber.market.api.dto.region.RegionDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RegionMapper {

    int insertRegionAdd(List<RegionDto.regionAdd> dto);
}
