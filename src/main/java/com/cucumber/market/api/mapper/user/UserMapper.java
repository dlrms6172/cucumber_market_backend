package com.cucumber.market.api.mapper.user;

import com.cucumber.market.api.dto.user.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface UserMapper {

    Map selectCheckUserInfo(UserDto.signInCallBackDto dto);

    int insertUserInfo(UserDto.signInCallBackDto dto);
}
