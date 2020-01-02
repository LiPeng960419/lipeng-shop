package com.lipeng.pay.impl.bill;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.pay.service.bill.UnionPayBillService;
import com.lipeng.pay.utils.UnionPayUtil;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.SDKConfig;
import com.lipeng.unionpay.acp.sdk.UnionPayBase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:16
 */
@Slf4j
@RestController
@RequestMapping("/unionPay/bill")
public class UnionPayBillServiceImpl extends BaseApiService<JSONObject>
        implements UnionPayBillService {

    @Override
    @GetMapping("/downloadBill")
    public void downloadBill(HttpServletRequest req, HttpServletResponse response) {
        String merId = req.getParameter("merId");
        //String settleDate = req.getParameter("settleDate");
        Map<String, String> data = new HashMap<String, String>();
        data.put("version", UnionPayBase.version);               //版本号 全渠道默认值
        data.put("encoding", UnionPayBase.encoding);             //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "76");                           //交易类型 76-对账文件下载
        data.put("txnSubType", "01");                        //交易子类型 01-对账文件下载
        data.put("bizType", "000000");                       //业务类型，固定
        data.put("accessType", "0");                         //接入类型，商户接入填0，不需修改
        data.put("merId", merId);
        // //清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期， 测试环境如果使用700000000000001商户号则固定填写0119
        data.put("settleDate", "0119");
        data.put("txnTime", UnionPayBase.getCurrentTime());
        data.put("fileType", "00");                          //文件类型，一般商户填写00即可
        Map<String, String> reqData = AcpService.sign(data, UnionPayBase.encoding);
        String url = SDKConfig.getConfig().getFileTransUrl();
        try {
            Map<String, String> rspData = UnionPayUtil.checkResponse(AcpService.post(reqData, url, UnionPayBase.encoding));
            String outPutDirectory = "d:\\";
            String zipFilePath = AcpService
                    .deCodeFileContent(rspData, outPutDirectory, UnionPayBase.encoding);
            List<String> fileList = UnionPayBase.unzip(zipFilePath, outPutDirectory);
            for (String file : fileList) {
                if (file.indexOf("ZM_") != -1) {
                    List<Map> ZmDataList = UnionPayBase.parseZMFile(file);
                } else if (file.indexOf("ZME_") != -1) {
                    List<Map> maps = UnionPayBase.parseZMEFile(file);
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}