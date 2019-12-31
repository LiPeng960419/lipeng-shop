package com.lipeng.pay.service.bill;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.pay.dto.BillSellQueryModel;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:13
 */
public interface BillService {

    void downloadBill(String billType, String billDate, HttpServletResponse response);

    BaseResponse<JSONObject> queryBill(String billType, String billDate);

    BaseResponse<JSONObject> querySell(BillSellQueryModel model);

}