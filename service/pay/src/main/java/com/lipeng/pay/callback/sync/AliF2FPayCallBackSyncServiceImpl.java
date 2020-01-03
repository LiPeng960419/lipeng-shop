package com.lipeng.pay.callback.sync;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.lipeng.alipay.config.AlipayConfig;
import com.lipeng.base.BaseApiService;
import com.lipeng.pay.service.pay.AliF2FPayCallBackSyncService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/15 10:22
 */
@Slf4j
@RestController
public class AliF2FPayCallBackSyncServiceImpl extends BaseApiService implements
        AliF2FPayCallBackSyncService {

    @Override
    public String synF2FCallBack(@RequestParam Map<String, String> params) {
        try {
            log.info("####ALIF2F同步回调开始####{}:", params);
            boolean signVerified = AlipaySignature
                    .rsaCheckV1(params, AlipayConfig.alipay_public_key,
                            AlipayConfig.charset, AlipayConfig.sign_type); // 调用SDK验证签名
            // ——请在这里编写您的程序（以下代码仅作参考）——
            if (!signVerified) {
                log.error("验签失败");
                return null;
            }
            // 商户订单号paymentID
            String out_trade_no = params.get("out_trade_no");
            // 支付宝交易号 第三方
            String trade_no = params.get("trade_no");
            // 付款金额
            String total_amount = params.get("total_amount");
            JSONObject data = new JSONObject();
            data.put("out_trade_no", out_trade_no);
            data.put("trade_no", trade_no);
            data.put("total_amount", total_amount);
            Assert.notNull(data.get("out_trade_no"), "缺少参数out_trade_no");
            Assert.notNull(data.get("trade_no"), "缺少参数trade_no");
            Assert.notNull(data.get("total_amount"), "缺少参数total_amount");
            //成功回调的返回
            return "success";
        } catch (Exception e) {
            log.error("######AliF2FPayCallBackSyncServiceImpl synF2FCallBack##ERROR:#####{}", e);
            return null;
        } finally {
            log.info("####ALIF2F同步回调结束####{}:", params);
        }
    }

}