package com.cucumber.market.api.mapper.item;

import com.cucumber.market.api.dto.item.ItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ItemImageMapper {

    void insertImage(ItemDto.ItemImageDto imageDto);

    List<String> selectImageKeyNames(Integer itemId);

    String selectRepImageKeyName(Integer itemId);

    void updateImageIndex(@Param("keyName")String keyName, @Param("index") Integer index);

    void deleteImage(String keyName);

    void deleteImages(Integer itemId);
}
