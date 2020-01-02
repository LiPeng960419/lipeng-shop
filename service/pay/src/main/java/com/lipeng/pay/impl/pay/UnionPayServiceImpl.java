package com.lipeng.pay.impl.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.pay.mapper.PaymentChannelMapper;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.service.pay.UnionPayService;
import com.lipeng.pay.utils.UnionPayUtil;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.SDKConfig;
import com.lipeng.unionpay.acp.sdk.UnionPayBase;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:16
 */
@Slf4j
@RestController
public class UnionPayServiceImpl extends BaseApiService<JSONObject>
        implements UnionPayService {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Override
    public BaseResponse<JSONObject> queryPayment(Long id) {
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(id);
        PaymentChannelEntity channel = paymentChannelMapper.selectBychannelId(paymentTransaction.getPaymentChannel());
        Map<String, String> requestData = new HashMap<String, String>();
        /*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
        requestData.put("version", UnionPayBase.version); // 版本号，全渠道默认值
        requestData.put("encoding", UnionPayBase.encoding); // 字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("bizType", "000000");
        UnionPayUtil.setChannelType(paymentTransaction.getPaymentChannel(), requestData);
        requestData.put("txnTime", UnionPayUtil.formatTxnTime(paymentTransaction.getCreatedTime()));
        requestData.put("txnType", "00");
        requestData.put("txnSubType", "00");
        requestData.put("accessType", "0");
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); // 签名方法
        requestData.put("merId", channel.getMerchantId());
        requestData.put("orderId", paymentTransaction.getPaymentId());
        Map<String, String> reqData = AcpService.sign(requestData, UnionPayBase.encoding);
        try {
            Map<String, String> checkedResponse = UnionPayUtil.checkResponse(AcpService
                    .post(reqData, SDKConfig.getConfig().getBackRequestUrl(),
                            UnionPayBase.encoding));
            String json = JSON.toJSONString(checkedResponse);//map转String
            return setResultSuccess(JSON.parseObject(json));//String转json
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return setResultError(e.getMessage());
        }
    }

    @Override
    public BaseResponse<JSONObject> refund(Long id) {
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(id);
        PaymentChannelEntity channel = paymentChannelMapper.selectBychannelId(paymentTransaction.getPaymentChannel());
        Map<String, String> requestData = new HashMap<String, String>();
        /*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
        requestData.put("version", UnionPayBase.version); // 版本号，全渠道默认值
        requestData.put("encoding", UnionPayBase.encoding); // 字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("bizType", "000000");
        UnionPayUtil.setChannelType(paymentTransaction.getPaymentChannel(), requestData);
        requestData.put("txnTime", UnionPayUtil.formatTxnTime(paymentTransaction.getCreatedTime()));
        requestData.put("txnType", "04"); //交易类型 04-退货
        requestData.put("txnSubType", "00");
        requestData.put("accessType", "0");
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); // 签名方法
        requestData.put("currencyCode", "156");  //交易币种（境内商户一般是156 人民币）
        requestData.put("merId", channel.getMerchantId());
        requestData.put("orderId", paymentTransaction.getPaymentId());
        // 交易金额，单位分，不要带小数点
        requestData.put("txnAmt", String.valueOf(paymentTransaction.getPayAmount()));
        //原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
        requestData.put("origQryId", paymentTransaction.getPartyPayId());
        Map<String, String> reqData = AcpService.sign(requestData, UnionPayBase.encoding);
        try {
            Map<String, String> checkedResponse = UnionPayUtil.checkResponse(AcpService
                    .post(reqData, SDKConfig.getConfig().getBackRequestUrl(),
                            UnionPayBase.encoding));
            //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
            String json = JSON.toJSONString(checkedResponse);//map转String
            return setResultSuccess(JSON.parseObject(json));//String转json
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return setResultError(e.getMessage());
        }
    }

    @Override
    public BaseResponse<JSONObject> refundQuery(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> cancel(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> close(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> settle(Long id) {
        return null;
    }

}