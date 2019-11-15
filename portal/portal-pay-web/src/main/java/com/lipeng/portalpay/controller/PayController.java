package com.lipeng.portalpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.dto.PaymentChannelDTO;
import com.lipeng.portalpay.feign.PayCallBackFeign;
import com.lipeng.portalpay.feign.PayContextFeign;
import com.lipeng.portalpay.feign.PayMentTransacInfoFeign;
import com.lipeng.portalpay.feign.PaymentChannelFeign;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class PayController extends BaseWebController {

    @Autowired
    private PayMentTransacInfoFeign payMentTransacInfoFeign;

    @Autowired
    private PaymentChannelFeign paymentChannelFeign;

    @Autowired
    private PayContextFeign payContextFeign;

    @Autowired
    private PayCallBackFeign payCallBackFeign;

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

    // 同步回调,解决隐藏参数
    @RequestMapping(value = "/alipay/callBack/synSuccessPage", method = RequestMethod.POST)
    public String synSuccessPage(HttpServletRequest request, String outTradeNo, String tradeNo,
            String totalAmount) {
        log.info(
                "alipay callBack synSuccessPage post method,params>>>outTradeNo:{},tradeNo:{},totalAmount{}",
                outTradeNo, tradeNo, totalAmount);
        request.setAttribute("outTradeNo", outTradeNo);
        request.setAttribute("tradeNo", tradeNo);
        request.setAttribute("totalAmount", totalAmount);
        return PAY_SUCCESS;
    }

    @RequestMapping(value = "/alipay/callBack/synSuccessPage", method = RequestMethod.GET)
    public String hello(HttpServletRequest request, String outTradeNo, String tradeNo,
            String totalAmount) {
        log.info(
                "alipay callBack synSuccessPage get method,params>>>outTradeNo:{},tradeNo:{},totalAmount{}",
                outTradeNo, tradeNo, totalAmount);
        request.setAttribute("outTradeNo", outTradeNo);
        request.setAttribute("tradeNo", tradeNo);
        request.setAttribute("totalAmount", totalAmount);
        return PAY_SUCCESS;
    }

    @RequestMapping("/alipay/callBack/synCallBack")
    public void synCallBack(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("alipay parse params error", e);
                return;
            }
            params.put(name, valueStr);
        }
        String html = payCallBackFeign.synCallBack(params);
        if (StringUtils.isNotEmpty(html)){
            writer.println(html);
            writer.close();
        }
    }

}
