package com.cucumber.market.api.mapper.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.service.item.ItemStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Mapper
public interface ItemMapper {

    int insertItem(@Param("memberId") Long memberId, @Param("itemDto") ItemDto.addItemDto itemDto);

    Optional<Map> selectItem(Long itemId);

    void updateViewCount(Long itemId);

    int updateItem(@Param("itemId") Long itemId, @Param("itemDto") ItemDto.modifyItemDto itemDto);

    void updateItemStatus(@Param("itemId") Long itemId, @Param("itemStatus") ItemStatus itemStatus);

    List<Map> selectItems(@Param("itemName") String itemName, @Param("itemStatus") ItemStatus itemStatus);

    void deleteItem(Long itemId);
    
    int insertLike(@Param("itemId") Long itemId, @Param("memberId") Long memberId);

    void deleteLike(@Param("itemId") Long itemId, @Param("memberId") Long memberId);

    int insertBuyerReview(@Param("itemId") Long itemId, @Param("memberId") Long memberId, @Param("reviewDto") ItemDto.reviewDto reviewDto);

    int insertSellerReview(@Param("itemId") Long itemId, @Param("reviewDto") ItemDto.reviewDto reviewDto);

    Optional<Map> selectBuyerReview(Long itemId);

    void deleteBuyerReview(Long itemId);

    void updateBuyerReview(@Param("itemId") Long itemId, @Param("reviewDto") ItemDto.reviewDto reviewDto);
}
