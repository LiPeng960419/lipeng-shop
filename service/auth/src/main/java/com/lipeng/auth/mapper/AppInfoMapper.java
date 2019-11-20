package com.lipeng.auth.mapper;

import com.lipeng.auth.mapper.entity.MeiteAppInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AppInfoMapper {

    @Insert("INSERT INTO `meite_app_info` VALUES (null,#{appName}, #{appId}, #{appSecret}, '0', null, null, null, null, null);")
    int insertAppInfo(MeiteAppInfo meiteAppInfo);

    @Select("SELECT ID AS ID ,app_id as appId, app_name AS appName ,app_secret as appSecret  FROM meite_app_info where app_id=#{appId} and app_secret=#{appSecret}; ")
    MeiteAppInfo selectByAppInfo(@Param("appId") String appId,
            @Param("appSecret") String appSecret);

    @Select("SELECT ID AS ID ,app_id as appId, app_name AS appName ,app_secret as appSecret  FROM meite_app_info where app_id=#{appId}  ")
    MeiteAppInfo findByAppInfo(@Param("appId") String appId);

}