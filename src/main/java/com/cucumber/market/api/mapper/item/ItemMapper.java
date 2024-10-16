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

    int insertItem(@Param("memberId") Integer memberId, @Param("itemDto") ItemDto.addItemDto itemDto);

    Optional<Map> selectItem(Integer itemId);

    void updateViewCount(Integer itemId);

    int updateItem(@Param("itemId") Integer itemId, @Param("itemDto") ItemDto.modifyItemDto itemDto);

    void updateItemStatus(@Param("itemId") Integer itemId, @Param("itemStatus") ItemStatus itemStatus);

    List<Map> selectAllItems(@Param("regionId") Integer regionId);

    List<Map> selectItems(@Param("regionId") Integer regionId, @Param("itemName") String itemName, @Param("itemStatus") ItemStatus itemStatus);

    void deleteItem(Integer itemId);
    
    int insertLike(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    Optional<Map> selectLike(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    void deleteLike(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    int insertBuyerReview(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId, @Param("reviewDto") ItemDto.reviewDto reviewDto);

    Optional<Map> selectBuyerReview(Integer itemId);

    List<Map> selectBuyerReviews(Integer memberId);

    void updateBuyerReview(@Param("itemId") Integer itemId, @Param("reviewDto") ItemDto.reviewDto reviewDto);

    void deleteBuyerReview(Integer itemId);

    int insertSellerReview(@Param("itemId") Integer itemId, @Param("reviewDto") ItemDto.reviewDto reviewDto);

    Optional<Map> selectSellerReview(Integer itemId);

    List<Map> selectSellerReviews(Integer memberId);

    void updateSellerReview(@Param("itemId") Integer itemId, @Param("reviewDto") ItemDto.reviewDto reviewDto);

    void deleteSellerReview(Integer itemId);

    int insertOrder(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    Optional<Map> selectOrder(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

    List<Map> selectOrders(Integer itemId);

    void deleteOrder(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId);

}
