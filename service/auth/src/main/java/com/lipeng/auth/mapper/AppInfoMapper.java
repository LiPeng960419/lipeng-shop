package com.lipeng.auth.mapper;

import com.lipeng.auth.mapper.entity.MeiteAppInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AppInfoMapper {

    @Insert("INSERT INTO meite_app_info VALUES (null,#{appName}, #{appId}, #{appSecret}, 0, null, null, NOW(), null, NOW())")
    int insertAppInfo(MeiteAppInfo meiteAppInfo);

    @Select("SELECT ID AS id, APPID as appId, APPNAME AS appName, APPSECRET as appSecret, AVAILABILITY as availability FROM meite_app_info where APPID=#{appId} and APPSECRET=#{appSecret}")
    MeiteAppInfo selectByAppInfo(@Param("appId") String appId,
            @Param("appSecret") String appSecret);

    @Select("SELECT ID AS id, APPID as appId, APPNAME AS appName, APPSECRET as appSecret, AVAILABILITY as availability, REVISION as revision, CREATED_BY as createdBy, CREATED_TIME as createdTime, UPDATED_BY as updatedBy, UPDATED_TIME as updatedTime FROM meite_app_info where APPID=#{appId}")
    MeiteAppInfo findByAppInfo(@Param("appId") String appId);

}