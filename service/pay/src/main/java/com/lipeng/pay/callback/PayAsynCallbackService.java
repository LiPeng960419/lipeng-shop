package com.lipeng.pay.callback;

import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;
import com.lipeng.pay.callback.template.factory.TemplateFactory;
import com.lipeng.pay.strategy.PayStrategy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PayAsynCallbackService {

    private static final String UNIONPAYCALLBACK_TEMPLATE = "unionPayCallbackTemplate";

    private static final String UNIONF2FPAYCALLBACK_TEMPLATE = "unionF2FPayCallbackTemplate";

    private static final String ALIPAYCALLBACK_TEMPLATE = "aliPayCallbackTemplate";

    private static final String ALIMOBILEPAYCALLBACK_TEMPLATE = "aliMobilePayCallbackTemplate";

    private static final String ALIF2FPAYCALLBACK_TEMPLATE = "aliF2FPayCallbackTemplate";

    /**
     * 银联异步回调接口执行代码
     */
    @RequestMapping("/unionPayAsynCallback")
    public String unionPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallbackTemplate(UNIONPAYCALLBACK_TEMPLATE);
        try {
            return abstractPayCallbackTemplate.asyncCallBack(req, resp, PayStrategy.UNION_PAY_CHANNEL_ID);
        } catch (Exception e) {
            log.error("unionPayAsynCallback Exception", e);
        }
        return null;
    }

    /**
     * 银联二维码扫码异步回调接口执行代码
     */
    @RequestMapping("/unionF2FPayAsynCallback")
    public String unionF2FPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallbackTemplate(UNIONF2FPAYCALLBACK_TEMPLATE);
        try {
            return abstractPayCallbackTemplate.asyncCallBack(req, resp, PayStrategy.UNION_F2F_PAY_CHANNEL_ID);
        } catch (Exception e) {
            log.error("unionF2FPayAsynCallback Exception", e);
        }
        return null;
    }

    /**
     * 支付宝异步回调接口执行代码
     */
    @RequestMapping("/aliPayAsynCallback")
    public String aliPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallbackTemplate(ALIPAYCALLBACK_TEMPLATE);
        try {
            return abstractPayCallbackTemplate.asyncCallBack(req, resp, PayStrategy.ALI_PAY_CHANNEL_ID);
        } catch (Exception e) {
            log.error("aliPayAsynCallback Exception", e);
        }
        return null;
    }

    /**
     * 支付宝MOBILE异步回调接口执行代码
     */
    @RequestMapping("/aliMobilePayAsynCallback")
    public String aliMobilePayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallbackTemplate(ALIMOBILEPAYCALLBACK_TEMPLATE);
        try {
            return abstractPayCallbackTemplate.asyncCallBack(req, resp, PayStrategy.ALI_MOBILE_PAY_CHANNEL_ID);
        } catch (Exception e) {
            log.error("aliMobilePayAsynCallback Exception", e);
        }
        return null;
    }

    /**
     * 支付宝F2F异步回调接口执行代码
     */
    @RequestMapping("/aliPayF2FAsynCallback")
    public String aliPayF2FAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallbackTemplate(ALIF2FPAYCALLBACK_TEMPLATE);
        try {
            return abstractPayCallbackTemplate.asyncCallBack(req, resp, PayStrategy.ALI_F2F_PAY_CHANNEL_ID);
        } catch (Exception e) {
            log.error("aliMobilePayAsynCallback Exception", e);
        }
        return null;
    }

}