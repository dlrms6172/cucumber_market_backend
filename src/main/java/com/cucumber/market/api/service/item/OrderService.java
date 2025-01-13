package com.cucumber.market.api.service.item;

import com.cucumber.market.api.mapper.item.ItemMapper;
import com.cucumber.market.api.mapper.item.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    public Map addOrder(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        orderMapper.insertOrder(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }


    @Transactional(readOnly = true)
    public Map getOrders(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map item = itemMapper.selectItem(itemId).orElseThrow(IllegalArgumentException::new);

        if (!item.get("memberId").equals(memberId)) {
            throw new IllegalArgumentException("구매를 신청한 사용자들 조회 권한이 없습니다.");
        }

        result.put("orders", orderMapper.selectOrders(itemId));
        return result;
    }


    public void deleteOrder(Integer itemId, Integer memberId) {
        orderMapper.selectOrder(itemId, memberId).orElseThrow(IllegalArgumentException::new);  //구매자 신청 존재 및 구매자 신청 삭제 권한 확인
        orderMapper.deleteOrder(itemId, memberId);
    }

}
