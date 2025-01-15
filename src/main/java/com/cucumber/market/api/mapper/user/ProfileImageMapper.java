package com.cucumber.market.api.mapper.user;

import com.cucumber.market.api.dto.request.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Mapper
public interface ProfileImageMapper {

    void insertImage(UserDto.ProfileImageDto imageDto);

    Optional<String> selectImageKeyName(Integer memberId);

    void deleteImage(Integer memberId);

}
