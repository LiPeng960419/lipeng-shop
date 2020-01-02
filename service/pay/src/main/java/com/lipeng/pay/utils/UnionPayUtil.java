package com.lipeng.pay.utils;

import com.lipeng.pay.common.exception.CommonPayException;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.LogUtil;
import com.lipeng.unionpay.acp.sdk.UnionPayBase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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

    public static Map<String, String> checkResponse(final Map<String, String> rspData)
            throws CommonPayException {
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, UnionPayBase.encoding)) {
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode");
                if (("00").equals(respCode)) {
                    return rspData;
                } else {
                    LogUtil.writeErrorLog("接口调用返回异常 respCode:" + respCode);
                    throw new CommonPayException("接口调用返回异常 respCode:" + respCode);
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