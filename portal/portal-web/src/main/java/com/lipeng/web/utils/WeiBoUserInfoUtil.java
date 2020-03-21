package com.lipeng.web.utils;

import com.lipeng.web.common.WeiBoConfig;

import java.util.HashMap;
import java.util.Map;

public class WeiBoUserInfoUtil {

    //获取code的请求地址
    public static String Get_Code = "https://api.weibo.com/oauth2/authorize?client_id=%s&redirect_uri=%s";
    //获取Web_access_tokenhttps的请求地址
    public static String Web_access_tokenhttps = "https://api.weibo.com/oauth2/access_token";
    //拉取用户信息的请求地址
    public static String User_Message = "https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s";

    //替换字符串
    public static String getCode(String CLIENT_ID, String REDIRECT_URI) {
        return String.format(Get_Code, CLIENT_ID, REDIRECT_URI);
    }

    //替换字符串
    public static String getWebAccess() {
        return Web_access_tokenhttps;
    }

    //替换字符串
    public static String getUserMessage(String access_token, String openid) {
        return String.format(User_Message, access_token, openid);
    }

    public static Map<String, String> getTokenParams(WeiBoConfig weiBoConfig, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", weiBoConfig.getAppkey());
        params.put("client_secret", weiBoConfig.getSecret());
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("redirect_uri", weiBoConfig.getReDirectUri());
        return params;
    }

}