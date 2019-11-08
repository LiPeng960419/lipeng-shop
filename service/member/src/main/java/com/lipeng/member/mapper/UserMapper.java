package com.lipeng.member.mapper;

import com.lipeng.member.mapper.entity.UserDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

public interface UserMapper {

    @Insert("INSERT INTO `meite_user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, null, '1', null, null, null);")
    int register(UserDo userDo);

    @Select("SELECT * FROM meite_user WHERE MOBILE=#{mobile};")
    UserDo existMobile(@Param("mobile") String mobile);

    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID "
            + "  FROM meite_user  WHERE MOBILE=#{mobile} and password=#{password};")
    UserDo login(@Param("mobile") String mobile, @Param("password") String password);

    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID"
            + " FROM meite_user WHERE user_Id=#{userId}")
    UserDo findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM meite_user WHERE QQ_OPENID=#{qqOpenId};")
    UserDo findByQQOpenId(@Param("qqOpenId") String qqOpenId);

    @Select("SELECT * FROM meite_user WHERE WX_OPENID=#{weixinOpenId};")
    UserDo findByWeixinOpenId(@Param("weixinOpenId") String weixinOpenId);

    @Update("update meite_user set QQ_OPENID =#{qqOpenId} WHERE USER_ID=#{userId}")
    void updateUserQQOpenId(@Param("qqOpenId") String qqOpenId, @Param("userId") Long userId);

    @Update("update meite_user set WX_OPENID =#{weixinOpenId} WHERE USER_ID=#{userId}")
    void updateUserWeixinOpenId(@Param("weixinOpenId") String weixinOpenId, @Param("userId") Long userId);

    int updateUserInfo(UserDo userDo);

}