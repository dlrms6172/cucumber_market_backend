package com.cucumber.market.api.mapper.item;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@Mapper
public interface LikeMapper {

    int insertLike(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    Optional<Map> selectLike(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    void deleteLike(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);
}
