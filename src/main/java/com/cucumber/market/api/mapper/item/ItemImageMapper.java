package com.cucumber.market.api.mapper.item;

import com.cucumber.market.api.dto.item.ItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ItemImageMapper {

    void insertImage(ItemDto.itemImageDto imageDto);

    List<String> selectImageKeyNames(Integer itemId);

    void deleteImage(String keyName);

    void deleteImages(Integer itemId);

    void insertRepImage(Integer imageId);
}
