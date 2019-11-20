package com.lipeng.pay.callback.servjce;

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

    private static final String ALIPAYCALLBACK_TEMPLATE = "aliPayCallbackTemplate";

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

}
