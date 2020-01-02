package com.lipeng.pay.utils;

import com.lipeng.pay.common.exception.CommonPayException;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.LogUtil;
import com.lipeng.unionpay.acp.sdk.UnionPayBase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;

/**
 * @Author: lipeng 910138
 * @Date: 2020/1/2 15:28
 */
public class UnionPayUtil {

    private UnionPayUtil() {
    }

    public static String formatTxnTime(Date timeDate) {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(timeDate);
        return date;
    }

    /**
     * 获取请求参数中所有的信息 当商户上送frontUrl或backUrl地址中带有参数信息的时候， 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，
     * 这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    // System.out.println("======为空的字段名===="+en);
                    res.remove(en);
                }
            }
        }
        return res;
    }

    /**
     * 获取请求参数中所有的信息。 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
     * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。 理论应该可以调整struts配置使不影响，但请自己去研究。
     * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
     */
    public static Map<String, String> getAllRequestParamStream(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        try {
            String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()),
                    UnionPayBase.encoding);
            LogUtil.writeLog("收到通知报文：" + notifyStr);
            String[] kvs = notifyStr.split("&");
            for (String kv : kvs) {
                String[] tmp = kv.split("=");
                if (tmp.length >= 2) {
                    String key = tmp[0];
                    String value = URLDecoder.decode(tmp[1], UnionPayBase.encoding);
                    res.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeLog(
                    "getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass()
                            + ":"
                            + e.getMessage());
        } catch (IOException e) {
            LogUtil.writeLog("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e
                    .getMessage());
        }
        return res;
    }

    public static Map<String, String> checkResponse(final Map<String, String> rspData,
            final String encoding) {
        if (!rspData.isEmpty()) {
            String encode = encoding == null ? UnionPayBase.encoding : encoding;
            if (AcpService.validate(rspData, encode)) {
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode");
                String respMsg = rspData.get("respMsg");
                if (("00").equals(respCode)) {
                    return rspData;
                } else {
                    LogUtil.writeErrorLog("接口调用返回异常 respCode:" + respCode + ",respMsg:" + respMsg);
                    throw new CommonPayException("接口调用返回异常 respCode:" + respCode + ",respMsg:" + respMsg);
                }
            } else {
                LogUtil.writeErrorLog("验证签名失败");
                throw new CommonPayException("验证签名失败");
            }
        } else {
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            throw new CommonPayException("未获取到返回报文或返回http状态码非200");
        }
    }

    public static Map<String, String> checkResponse(final Map<String, String> rspData)
            throws CommonPayException {
        return checkResponse(rspData, null);
    }

    public static void setChannelType(String payChannel, Map<String, String> requestData) {
        if (PayStrategy.UNION_PAY_CHANNEL_ID.equals(payChannel)) {
            requestData.put("channelType", "07");//渠道类型，07-PC，08-手机
        } else if (PayStrategy.UNION_F2F_PAY_CHANNEL_ID.equals(payChannel)) {
            requestData.put("channelType", "08");//渠道类型，07-PC，08-手机
        } else {
            throw new CommonPayException("不支持的银联支付类型");
        }
    }

}