package com.cucumber.market.api.service.history;


import com.cucumber.market.api.dto.history.HistoryDto;
import com.cucumber.market.api.mapper.history.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryService {

    @Autowired
    HistoryMapper historyMapper;

    public Map sales(HistoryDto.sales dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectSales = historyMapper.selectSales(dto);

        result.put("sales",selectSales);

        return result;
    }

    public Map purchases(HistoryDto.purchase dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectPurchases = historyMapper.selectPurchases(dto);

        result.put("purchases",selectPurchases);

        return result;
    }

    public Map interests(HistoryDto.interests dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectInterests = historyMapper.selectInterests(dto);

        result.put("interests",selectInterests);

        return result;
    }

    public Map itemStatus(HistoryDto.itemStatus dto){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectItemStatus = historyMapper.selectItemStatus(dto);

        result.put("itemStatus",selectItemStatus);

        return result;
    }

}
