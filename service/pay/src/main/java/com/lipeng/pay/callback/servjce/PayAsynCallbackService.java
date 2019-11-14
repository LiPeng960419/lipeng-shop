package com.lipeng.pay.callback.servjce;

import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;
import com.lipeng.pay.callback.template.factory.TemplateFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayAsynCallbackService {

    private static final String UNIONPAYCALLBACK_TEMPLATE = "unionPayCallbackTemplate";

    /**
     * 银联异步回调接口执行代码
     */
    @RequestMapping("/unionPayAsynCallback")
    public String unionPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallbackTemplate(UNIONPAYCALLBACK_TEMPLATE);
        return abstractPayCallbackTemplate.asyncCallBack(req, resp);
    }

}
