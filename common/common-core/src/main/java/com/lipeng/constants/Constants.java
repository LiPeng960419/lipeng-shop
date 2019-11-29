package com.lipeng.constants;

public interface Constants {

    String TOKEN = "token";

    String UTF_8 = "UTF-8";

    // 响应请求成功
    String HTTP_RES_CODE_200_VALUE = "success";

    // 系统错误
    String HTTP_RES_CODE_500_VALUE = "fail";

    // 响应请求成功code
    Integer HTTP_RES_CODE_200 = 200;

    // 系统错误
    Integer HTTP_RES_CODE_500 = 500;

    // 未关联QQ账号
    Integer HTTP_RES_CODE_201 = 201;

    // 发送邮件
    String MSG_EMAIL = "email";

    // 会员token
    String TOKEN_MEMBER = "TOKEN_MEMBER";

    // 用户有效期 90天
    Long TOKEN_MEMBER_TIME = (long) (60 * 60 * 24 * 90);

    int COOKIE_TOKEN_MEMBER_TIME = (60 * 60 * 24 * 90);

    // cookie 会员 totoken 名称
    String COOKIE_MEMBER_TOKEN = "cookie_member_token";

    // 微信注册码存放rediskey
    String WEIXINCODE_KEY = "weixin.code";

    // 微信注册码有效期30分钟
    Long WEIXINCODE_TIMEOUT = 1800L;

    // 用户信息不存在
    Integer HTTP_RES_CODE_EXISTMOBILE_203 = 203;

    // token
    String MEMBER_TOKEN_KEYPREFIX = "mt.mb.login";

    // 安卓的登陆类型
    String MEMBER_LOGIN_TYPE_ANDROID = "Android";
    // IOS的登陆类型
    String MEMBER_LOGIN_TYPE_IOS = "IOS";

    // PC的登陆类型
    String MEMBER_LOGIN_TYPE_PC = "PC";

    // PC的登陆类型
    String MEMBER_LOGIN_TYPE_QQ = "QQ_LOGIN";

    String MEMBER_LOGIN_TYPE_WEIXIN = "WEIXIN_LOGIN";

    // 登陆超时时间 有效期 90天
    Long MEMBRE_LOGIN_TOKEN_TIME = 77776000L;

    // 用户信息不存在
    Integer HTTP_RES_CODE_NOTUSER_203 = 203;

    String WEIXIN_SEX_MAN = "1";

    String WEIXIN_SEX_FEMALE = "2";

    String WEIXIN_SEX_UNKNOW = "0";

    int MAN = 0;

    int FEMALE = 1;

    Long PAY_TOKEN_TIME = 15 * 60L;

    int INTEGRAL_USEABLE = 1;

    int INTEGRAL_UN_USEABLE = 0;

    String SENDING = "0"; //发送中

    String SEND_SUCCESS = "1"; //成功

    String SEND_FAILURE = "2"; //失败

    int INTEGRAL_TIMEOUT = 1; /*分钟超时单位：min*/

}