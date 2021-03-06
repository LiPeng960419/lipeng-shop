package com.lipeng.member.mapper;

import com.lipeng.member.mapper.entity.UserTokenDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserTokenMapper {

    /**
     * 根据userid+loginType +is_availability=0 进行查询
     */
    @Select("SELECT id as id, token as token, login_type as loginType, device_infor as deviceInfor, is_availability as isAvailability, user_id as userId"
            + ", create_time as createTime, update_time as updateTime FROM meite_user_token WHERE user_id=#{userId} AND login_type=#{loginType} and is_availability = 0;")
    UserTokenDo selectByUserIdAndLoginType(@Param("userId") Long userId,
            @Param("loginType") String loginType);

    /**
     * 根据userId+loginType token的状态修改为不可用
     */
    @Update("Update meite_user_token set is_availability = 1, update_time=now() where token=#{token};")
    int updateTokenAvailability(@Param("token") String token);

    /**
     * token记录表中插入一条记录
     */
    @Insert("INSERT INTO `meite_user_token` VALUES (null, #{token},#{loginType}, #{deviceInfor}, 0, #{userId}, now(), null);")
    int insertUserToken(UserTokenDo userTokenDo);

    @Update("Update meite_user_token set token=#{newToken}, update_time=now() where user_id=#{userId} AND login_type=#{loginType};")
    int updateTokenByUserIdAndLoginType(@Param("userId") Long userId,
            @Param("loginType") String loginType, @Param("newToken") String newToken);

}