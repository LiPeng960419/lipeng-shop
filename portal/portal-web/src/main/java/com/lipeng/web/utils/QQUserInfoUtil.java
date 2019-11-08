package com.lipeng.web.utils;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/8 13:55
 */
public class QQUserInfoUtil {

    public static String Authorization = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s";

    public static String AccessToken = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";

    public static String QpenId = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    public static String USERINFO = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    //Step1：获取Authorization Code
    public static String getAuthorization(String APPID, String REDIRECT_URI, String STATE) {
        return String.format(Authorization, APPID, REDIRECT_URI, STATE);
    }

    //Step2: 通过Authorization Code获取Access Token
    public static String getAccessToken(String APPID, String APPKEY, String CODE,
            String REDIRECT_URI) {
        return String.format(AccessToken, APPID, APPKEY, CODE, REDIRECT_URI);
    }

    //Step3: 通过Access Token获取openId
    public static String getOpenId(String ACCESSTOKEN) {
        return String.format(QpenId, ACCESSTOKEN);
    }

    //Step4: 获取USERINFO
    public static String getUserInfo(String ACCESSTOKEN, String APPID, String OPENID) {
        return String.format(USERINFO, ACCESSTOKEN, APPID, OPENID);
    }
}