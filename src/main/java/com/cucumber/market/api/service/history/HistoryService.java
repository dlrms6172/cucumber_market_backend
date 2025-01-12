package com.cucumber.market.api.service.history;

import com.cucumber.market.api.mapper.history.HistoryMapper;
import com.cucumber.market.api.service.item.ItemImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryService {

    @Autowired
    HistoryMapper historyMapper;
    @Autowired
    ItemImageService itemImageService;

    public Map getSales(int memberId, Integer itemStatusId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectSales = historyMapper.selectSales(memberId, itemStatusId);
        putItemRepImages(selectSales);

        result.put("sales",selectSales);

        return result;
    }

    public Map getPurchases(int memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectPurchases = historyMapper.selectPurchases(memberId);
        putItemRepImages(selectPurchases);

        result.put("purchases",selectPurchases);

        return result;
    }

    public Map getInterests(int memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectInterests = historyMapper.selectInterests(memberId);
        putItemRepImages(selectInterests);

        result.put("interests",selectInterests);

        return result;
    }

    public Map getItemStatus(int memberId){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();

        List<Map> selectItemStatus = historyMapper.selectItemStatus(memberId);

        result.put("itemStatus",selectItemStatus);

        return result;
    }

    private void putItemRepImages(List<Map> items) {
        //상품 대표 이미지(섬네일)
        for (Map item : items) {
            Integer itemId = (Integer) item.get("itemId");
            String repImageUrl = itemImageService.getRepImageUrl(itemId);
            item.put("itemRepImage", repImageUrl);
        }
    }

}
