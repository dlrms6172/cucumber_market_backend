package com.cucumber.market.api.mapper.history;

import com.cucumber.market.api.dto.history.HistoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface HistoryMapper {

    List<Map> selectSales(HistoryDto.sales dto);

    List<Map> selectPurchases(HistoryDto.purchase dto);

    List<Map> selectInterests(HistoryDto.interests dto);

    List<Map> selectItemStatus(HistoryDto.itemStatus dto);
}
