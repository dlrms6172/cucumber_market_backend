package com.cucumber.market.api.mapper.user;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.dto.user.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface UserMapper {

    Map selectCheckUserInfo(UserDto.signInCallBackDto dto);

    int insertUserInfo(UserDto.signInCallBackDto dto);

    Map selectUserInfo(Integer memberId);

    int updateUserInfo(UserDto.userProfilePut dto);

    Map selectSellerMannersTemperature(Integer memberId);

    int updateSellerMannersTemperature(@Param("memberId") Integer memberId, @Param("dto") ItemDto.modifyItemStatusDto dto);

    Map selectBuyerMannersTemperature(@Param("dto") ItemDto.modifyItemStatusDto dto);

    int updateBuyerMannersTemperature(@Param("dto") ItemDto.modifyItemStatusDto dto);
}
