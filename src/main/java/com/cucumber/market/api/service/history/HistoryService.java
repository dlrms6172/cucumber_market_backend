package com.cucumber.market.api.service.history;


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

    public Map sales(int memberId, Integer itemStatusId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectSales = historyMapper.selectSales(memberId, itemStatusId);

        result.put("sales",selectSales);

        return result;
    }

    public Map purchases(int memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectPurchases = historyMapper.selectPurchases(memberId);

        result.put("purchases",selectPurchases);

        return result;
    }

    public Map interests(int memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectInterests = historyMapper.selectInterests(memberId);

        result.put("interests",selectInterests);

        return result;
    }

    public Map itemStatus(int memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectItemStatus = historyMapper.selectItemStatus(memberId);

        result.put("itemStatus",selectItemStatus);

        return result;
    }

}
