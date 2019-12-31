package com.lipeng.pay.impl.bill;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.pay.service.bill.BillService;
import com.lipeng.pay.utils.BillUtil;
import com.lipeng.pay.utils.PayUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:16
 */
@Slf4j
@RestController
public class BillServiceImpl extends BaseApiService<JSONObject>
        implements BillService {

    @Override
    public void downloadBill(String billType, String billDate, HttpServletResponse response) {
        BaseResponse<JSONObject> result = queryBill(billType, billDate);
        if (Objects.isNull(result)) {
            throw new RuntimeException("获取账单地址失败，请稍后重试!");
        }
        String fileName = null;
        try {
            fileName = new String((URLEncoder.encode("账单信息", "UTF-8") + billDate).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("downloadBill encode fileName error", e);
        }
        String downloadUrl = result.getData().getString("billDownloadUrl");
        BillUtil.downloadBill(downloadUrl, response, fileName);
    }

    /*
        为方便商户快速查账，支持商户通过本接口获取商户离线账单下载地址
        参数示例 trade 2016-04-05
         */
    @Override
    public BaseResponse<JSONObject> queryBill(String billType, String billDate) {
        AlipayClient alipayClient = PayUtil.getAlipayClient();
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
        model.setBillType(billType);
        model.setBillDate(billDate);
        request.setBizModel(model);
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess() && StringUtils.isNotEmpty(response.getBillDownloadUrl())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("billDownloadUrl", response.getBillDownloadUrl());
                return setResultSuccess(jsonObject);
            } else {
                log.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("queryBill error", e);
        }
        return null;
    }

}