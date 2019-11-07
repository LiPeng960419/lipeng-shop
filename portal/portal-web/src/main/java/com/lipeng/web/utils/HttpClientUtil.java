package com.lipeng.web.utils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpClientUtil {

    private static final String UTF_8 = "UTF-8";
    private static final int SUCCESS_CODE = 200;
    private static final int SOCKET_TIME_OUT = 5000;
    private static final int CONNECT_TIME_OUT = 5000;
    private static final int CONNECT_REQUEST_TIME_OUT = 5000;

    private static CloseableHttpClient getHttpClient() {
        // 创建Httpclient对象
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        return httpClient;
    }

    public static String doGet(String url, Map<String, String> param) {

        CloseableHttpClient httpClient = getHttpClient();

        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            log.info("doGet uri:" + uri.toString());
            // 执行请求
            response = httpClient.execute(httpGet);

            String resultString = EntityUtils.toString(response.getEntity(), UTF_8);
            // 判断返回状态是否为SUCCESS_CODE
            if (response.getStatusLine().getStatusCode() == SUCCESS_CODE) {
                return resultString;
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error("error", e);
            }
        }
        return null;
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();

        CloseableHttpResponse response = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, UTF_8);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);

            String resultString = EntityUtils.toString(response.getEntity(), UTF_8);
            // 判断返回状态是否为SUCCESS_CODE
            if (response.getStatusLine().getStatusCode() == SUCCESS_CODE) {
                return resultString;
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        return null;
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();

        CloseableHttpResponse response = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);

            String resultString = EntityUtils.toString(response.getEntity(), UTF_8);
            // 判断返回状态是否为SUCCESS_CODE
            if (response.getStatusLine().getStatusCode() == SUCCESS_CODE) {
                return resultString;
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("error", e);
            }
        }
        return null;
    }

}