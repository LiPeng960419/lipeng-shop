package com.lipeng.portalpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.dto.PaymentChannelDTO;
import com.lipeng.portalpay.feign.AliMobilePayCallBackFeign;
import com.lipeng.portalpay.feign.PayCallBackFeign;
import com.lipeng.portalpay.feign.PayContextFeign;
import com.lipeng.portalpay.feign.PayMentTransacInfoFeign;
import com.lipeng.portalpay.feign.PaymentChannelFeign;
import com.lipeng.portalpay.utils.PayUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
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

//    public static final String ALI_PAY_CHANNEL_ID = "alipay";
//
//    public static final String UNION_PAY_CHANNEL_ID = "yinlian_pay";
//
//    public static final String ALI_MOBILE_PAY_CHANNEL_ID = "ali_mobile_pay";

    public static final String ALI_F2F_PAY_CHANNEL_ID = "ali_f2f_pay";


    @Autowired
    private PayMentTransacInfoFeign payMentTransacInfoFeign;

    @Autowired
    private PaymentChannelFeign paymentChannelFeign;

    @Autowired
    private PayContextFeign payContextFeign;

    @Autowired
    private PayCallBackFeign payCallBackFeign;

    @Autowired
    private AliMobilePayCallBackFeign aliMobilePayCallBackFeign;

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

    @RequestMapping("/payHtml")
    public void payHtml(String channelId, String payToken, HttpServletResponse response) {
        try {
            response.setContentType("text/html; charset=utf-8");
            BaseResponse<JSONObject> payHtmlData = payContextFeign.toPayHtml(channelId, payToken);
            if (isSuccess(payHtmlData)) {
                if (ALI_F2F_PAY_CHANNEL_ID.equals(channelId)) {
                    BufferedImage image = PayUtil.getQRCodeImge(payHtmlData.getData().getString("payHtml"));
                    response.setContentType("image/jpeg");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Cache-Control", "no-cache");
                    response.setIntHeader("Expires", -1);
                    ImageIO.write(image, "JPEG", response.getOutputStream());
                } else {
                    JSONObject data = payHtmlData.getData();
                    String payHtml = data.getString("payHtml");
                    response.getWriter().print(payHtml);
                }
            }
        }catch (Exception e){
            log.error("to payHtml error", e);
        }
    }

    // 同步回调,解决隐藏参数
    @RequestMapping(value = "/alipay/callBack/synSuccessPage", method = RequestMethod.POST)
    public String synSuccessPagePost(HttpServletRequest request, String outTradeNo, String tradeNo,
            String totalAmount) {
        log.info("alipay callBack synSuccessPage post method,params>>>outTradeNo:{},tradeNo:{},totalAmount{}",
                outTradeNo, tradeNo, totalAmount);
        request.setAttribute("outTradeNo", outTradeNo);
        request.setAttribute("tradeNo", tradeNo);
        request.setAttribute("totalAmount", totalAmount);
        return PAY_SUCCESS;
    }

    @RequestMapping(value = "/alipay/callBack/synSuccessPage", method = RequestMethod.GET)
    public String synSuccessPageGet(HttpServletRequest request, String outTradeNo, String tradeNo,
            String totalAmount) {
        log.info("alipay callBack synSuccessPage get method,params>>>outTradeNo:{},tradeNo:{},totalAmount{}",
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
        String html = payCallBackFeign.synCallBack(verifyRequest(request));
        if (StringUtils.isNotEmpty(html)) {
            writer.println(html);
            writer.close();
        }
    }


    // 同步回调,解决隐藏参数
    @RequestMapping(value = "/alipay/callBack/synMobileSuccessPage", method = RequestMethod.POST)
    public String synMobileSuccessPagePost(HttpServletRequest request, String outTradeNo, String tradeNo,
            String totalAmount) {
        log.info("alipay callBack synSuccessPage post method,params>>>outTradeNo:{},tradeNo:{},totalAmount{}",
                outTradeNo, tradeNo, totalAmount);
        request.setAttribute("outTradeNo", outTradeNo);
        request.setAttribute("tradeNo", tradeNo);
        request.setAttribute("totalAmount", totalAmount);
        return PAY_SUCCESS;
    }

    @RequestMapping(value = "/alipay/callBack/synMobileSuccessPage", method = RequestMethod.GET)
    public String synMobileSuccessPageGet(HttpServletRequest request, String outTradeNo, String tradeNo,
            String totalAmount) {
        log.info("alipay callBack synSuccessPage get method,params>>>outTradeNo:{},tradeNo:{},totalAmount{}",
                outTradeNo, tradeNo, totalAmount);
        request.setAttribute("outTradeNo", outTradeNo);
        request.setAttribute("tradeNo", tradeNo);
        request.setAttribute("totalAmount", totalAmount);
        return PAY_SUCCESS;
    }

    @RequestMapping("/alipay/callBack/synMobileCallBack")
    public void synMobileCallBack(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String html = aliMobilePayCallBackFeign.synMobileCallBack(verifyRequest(request));
        if (StringUtils.isNotEmpty(html)) {
            writer.println(html);
            writer.close();
        }
    }

    private Map<String, String> verifyRequest(HttpServletRequest request) {
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
            }
            params.put(name, valueStr);
        }
        return params;
    }

}