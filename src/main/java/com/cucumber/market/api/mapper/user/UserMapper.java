package com.cucumber.market.api.mapper.user;

import com.cucumber.market.api.dto.request.ItemDto;
import com.cucumber.market.api.dto.request.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface UserMapper {

    Map selectCheckUserInfo(UserDto.SignInCallBackDto dto);

    int insertUserInfo(UserDto.SignInCallBackDto dto);

    Map selectUserInfo(Integer memberId);

    int updateUserInfo(UserDto.UserProfilePutDto dto);

    Map selectSellerMannersTemperature(Integer memberId);

    int updateSellerMannersTemperature(@Param("memberId") Integer memberId, @Param("dto") ItemDto.ModifyItemStatusDto dto);

    Map selectBuyerMannersTemperature(@Param("dto") ItemDto.ModifyItemStatusDto dto);

    int updateBuyerMannersTemperature(@Param("dto") ItemDto.ModifyItemStatusDto dto);

    int insertRefreshToken(Integer memberId, String refreshToken);

    String selectRefreshToken(String refreshToken);

    String selectMemberIdRefreshToken(String refreshToken);
}
