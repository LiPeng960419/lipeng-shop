package com.lipeng.portalpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.dto.PaymentChannelDTO;
import com.lipeng.portalpay.feign.PayContextFeign;
import com.lipeng.portalpay.feign.PayMentTransacInfoFeign;
import com.lipeng.portalpay.feign.PaymentChannelFeign;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PayController extends BaseWebController {

    @Autowired
    private PayMentTransacInfoFeign payMentTransacInfoFeign;
    @Autowired
    private PaymentChannelFeign paymentChannelFeign;
    @Autowired
    private PayContextFeign payContextFeign;

    @RequestMapping("/pay")
    public String pay(String payToken, Model model) {
        // 1.验证payToken参数
        if (StringUtils.isEmpty(payToken)) {
            setErrorMsg(model, "支付令牌不能为空!");
            return ERROR_500_FTL;
        }
        // 2.使用payToken查询支付信息
        BaseResponse<PayMentTransacDTO> tokenByPayMentTransac = payMentTransacInfoFeign.tokenByPayMentTransac(payToken);
        if (!isSuccess(tokenByPayMentTransac)) {
            setErrorMsg(model, tokenByPayMentTransac.getMsg());
            return ERROR_500_FTL;
        }
        // 3.查询支付信息
        PayMentTransacDTO data = tokenByPayMentTransac.getData();
        model.addAttribute("data", data);
        // 4.查询渠道信息
        List<PaymentChannelDTO> paymentChanneList = paymentChannelFeign.selectAll();
        model.addAttribute("paymentChanneList", paymentChanneList);
        model.addAttribute("payToken", payToken);
        return "index";
    }

    /**
     *
     */
    @RequestMapping("/payHtml")
    public void payHtml(String channelId, String payToken, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html; charset=utf-8");
        BaseResponse<JSONObject> payHtmlData = payContextFeign.toPayHtml(channelId, payToken);
        if (isSuccess(payHtmlData)) {
            JSONObject data = payHtmlData.getData();
            String payHtml = data.getString("payHtml");
            response.getWriter().print(payHtml);
        }

    }

}
