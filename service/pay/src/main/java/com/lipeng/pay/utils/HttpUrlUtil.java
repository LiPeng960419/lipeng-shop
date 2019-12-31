package com.lipeng.pay.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/31 15:00
 */
public class HttpUrlUtil {

    private HttpUrlUtil() {
    }

    public static HttpURLConnection getConnection(String urlPath, String method)
            throws IOException {
        // 统一资源
        URL url = new URL(urlPath);
        // 连接类的父类，抽象类
        URLConnection urlConnection = url.openConnection();
        // http的连接类
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        //设置超时
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        //设置请求方式，默认是GET
        httpURLConnection.setRequestMethod(method);
        // 设置字符编码
        httpURLConnection.setRequestProperty("Charset", "UTF-8");

        return httpURLConnection;
    }

}
