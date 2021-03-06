package com.lipeng.member.mapper;

import com.lipeng.member.mapper.entity.UserDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

public interface UserMapper {

    @Insert("INSERT INTO `meite_user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, now(), null, 1, null, null, null, null);")
    int register(UserDo userDo);

    @Select("SELECT * FROM meite_user WHERE MOBILE=#{mobile};")
    UserDo existMobile(@Param("mobile") String mobile);

    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USERNAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,UPDATE_TIME AS UPDATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID,WB_OPENID AS wbOpenid "
            + "  FROM meite_user  WHERE MOBILE=#{mobile} and password=#{password};")
    UserDo login(@Param("mobile") String mobile, @Param("password") String password);

    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USERNAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,UPDATE_TIME AS UPDATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID,WB_OPENID AS wbOpenid"
            + " FROM meite_user WHERE user_Id=#{userId}")
    UserDo findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM meite_user WHERE QQ_OPENID=#{qqOpenId};")
    UserDo findByQQOpenId(@Param("qqOpenId") String qqOpenId);

    @Select("SELECT * FROM meite_user WHERE WX_OPENID=#{weixinOpenId};")
    UserDo findByWeixinOpenId(@Param("weixinOpenId") String weixinOpenId);

    @Select("SELECT * FROM meite_user WHERE WB_OPENID=#{weiboOpenId};")
    UserDo findByWeiBoOpenId(@Param("weiboOpenId") String weiboOpenId);

    @Update("update meite_user set QQ_OPENID =#{qqOpenId}, UPDATE_TIME=now() WHERE USER_ID=#{userId}")
    void updateUserQQOpenId(@Param("qqOpenId") String qqOpenId, @Param("userId") Long userId);

    @Update("update meite_user set WX_OPENID =#{weixinOpenId}, UPDATE_TIME=now() WHERE USER_ID=#{userId}")
    void updateUserWeixinOpenId(@Param("weixinOpenId") String weixinOpenId, @Param("userId") Long userId);

    @Update("update meite_user set WB_OPENID =#{weiboOpenId}, UPDATE_TIME=now() WHERE USER_ID=#{userId}")
    void updateUserWeiboOpenId(@Param("weiboOpenId") String weiboOpenId, @Param("userId") Long userId);

    int updateUserInfo(UserDo userDo);

}