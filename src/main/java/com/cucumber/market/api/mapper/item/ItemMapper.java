package com.cucumber.market.api.mapper.item;

import com.cucumber.market.api.dto.request.ItemDto;
import com.cucumber.market.api.dto.response.Item;
import com.cucumber.market.api.dto.response.ItemDetail;
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

    int insertItem(@Param("memberId") Integer memberId, @Param("itemDto") ItemDto.AddItemDto itemDto);

    Optional<ItemDetail> selectItem(Integer itemId);

    Optional<Map> selectUserMainInfo(Integer memberId);

    void updateViewCount(Integer itemId);

    int updateItem(@Param("itemId") Integer itemId, @Param("itemDto") ItemDto.ModifyItemDto itemDto);

    void updateItemStatus(@Param("itemId") Integer itemId, @Param("itemStatus") ItemStatus itemStatus);

    List<Item> selectAllItems(@Param("regionId") Integer regionId);

    List<Item> selectItems(@Param("regionId") Integer regionId, @Param("itemName") String itemName, @Param("itemStatus") ItemStatus itemStatus);

    void deleteItem(Integer itemId);

    int insertBuyerReview(@Param("itemId") Integer itemId, @Param("memberId") Integer memberId, @Param("reviewDto") ItemDto.ReviewDto reviewDto);

    Optional<Map> selectBuyerReview(Integer itemId);

    List<Map> selectBuyerReviews(Integer memberId);

    void updateBuyerReview(@Param("itemId") Integer itemId, @Param("reviewDto") ItemDto.ReviewDto reviewDto);

    void deleteBuyerReview(Integer itemId);

    int insertSellerReview(@Param("itemId") Integer itemId, @Param("reviewDto") ItemDto.ReviewDto reviewDto);

    Optional<Map> selectSellerReview(Integer itemId);

    List<Map> selectSellerReviews(Integer memberId);

    void updateSellerReview(@Param("itemId") Integer itemId, @Param("reviewDto") ItemDto.ReviewDto reviewDto);

    void deleteSellerReview(Integer itemId);

}
