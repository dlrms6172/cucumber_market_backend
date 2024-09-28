package com.cucumber.market.api.mapper.history;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface HistoryMapper {

    List<Map> selectSales(int memberId, Integer itemStatusId);

    List<Map> selectPurchases(int memberId);

    List<Map> selectInterests(int memberId);

    List<Map> selectItemStatus(int memberId);
}
