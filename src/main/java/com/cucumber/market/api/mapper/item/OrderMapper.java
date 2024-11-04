package com.cucumber.market.api.mapper.item;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Mapper
public interface OrderMapper {

    int insertOrder(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    Optional<Map> selectOrder(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    List<Map> selectOrders(Integer itemId);

    void deleteOrder(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);
}
