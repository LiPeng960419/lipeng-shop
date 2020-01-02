package com.lipeng.pay.strategy.impl;

import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.pay.utils.UnionPayUtil;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.LogUtil;
import com.lipeng.unionpay.acp.sdk.SDKConfig;
import com.lipeng.unionpay.acp.sdk.UnionPayBase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/*
申请二维码
 */
@Slf4j
public class UnionF2FPayStrategy implements PayStrategy {

	@Override
	public String toPayHtml(PaymentChannelEntity paymentChannel, PayMentTransacDTO payMentTransacDTO) {
		log.info(">>>>>>>>银联二维码支付组装参数开始<<<<<<<<<<<<");

		Map<String, String> requestData = new HashMap<String, String>();

		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		requestData.put("version", UnionPayBase.version); // 版本号，全渠道默认值
		requestData.put("encoding", UnionPayBase.encoding); // 字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); // 签名方法
		requestData.put("txnType", "01"); //交易类型 01:消费
		requestData.put("txnSubType", "07");//交易子类 07：申请消费二维码
		requestData.put("bizType", "000000"); //填写000000
		requestData.put("channelType", "08"); //渠道类型 08手机

		/*** 商户接入参数 ***/
		String merchantId = paymentChannel.getMerchantId();
		requestData.put("merId", merchantId); // 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("accessType", "0"); //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		String paymentId = payMentTransacDTO.getPaymentId();
		// 在微服务电商项目中 订单系统(orderId)   支付系统 支付id
		requestData.put("orderId", paymentId); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		requestData.put("txnTime", UnionPayUtil.formatTxnTime(payMentTransacDTO.getCreatedTime())); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156"); // 交易币种（境内商户一般是156 人民币）
		Long payAmount = payMentTransacDTO.getPayAmount();
		requestData.put("txnAmt", String.valueOf(payAmount)); // 交易金额，单位分，不要带小数点
		// requestData.put("reqReserved", "透传字段");
		// //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。

		//商品名称 或者商品描述
		requestData.put("riskRateInfo", "{commodityName=测试银联扫码商品名称}");

		//后台返回商户结果时使用
		//如果不需要发后台通知，可以固定上送 http://www.specialUrl.com
		//requestData.put("backUrl", "http://www.specialUrl.com");
		String asynUrl = paymentChannel.getAsynUrl();
		requestData.put("backUrl", asynUrl);

		// 订单超时时间。
		// 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。
		// 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
		// 此时间建议取支付时的北京时间加15分钟。
		// 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
		requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));

		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = AcpService.sign(requestData, UnionPayBase.encoding); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestAppUrl = SDKConfig.getConfig().getBackRequestUrl();
		Map<String, String> rspData = AcpService.post(reqData,requestAppUrl, UnionPayBase.encoding);
		if (!rspData.isEmpty()) {
			if (AcpService.validate(rspData, UnionPayBase.encoding)) {
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode");
				if (("00").equals(respCode)) {
					//成功,获取tn号
					//String tn = resmap.get("tn");
					String qrCode = rspData.get("qrCode");
					if (StringUtils.isNotEmpty(qrCode)) {
						log.info("UnionF2FPayStrategy get qrCode: " + qrCode);
						return qrCode;
					} else {
						LogUtil.writeErrorLog("获取验证码失败");
					}
				} else {
					//其他应答码为失败请排查原因或做失败处理
					LogUtil.writeErrorLog("接口调用返回异常 respCode:" + respCode);
				}
			} else {
				LogUtil.writeErrorLog("验证签名失败");
			}
		} else {
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
		String reqMessage = UnionPayBase.genHtmlResult(reqData);
		String rspMessage = UnionPayBase.genHtmlResult(rspData);
		LogUtil.writeLog("请求报文:<br/>" + reqMessage + "<br/>" + "应答报文:</br>" + rspMessage + "");
		return null;
	}

}