package com.cucumber.market.api.mapper.item;

import com.cucumber.market.api.dto.image.ImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ImageMapper {

    void insertImage(ImageDto imageDto);

    List<String> selectImageKeyNames(Integer itemId);

    void deleteImage(String keyName);

    void deleteImages(Integer itemId);
}
