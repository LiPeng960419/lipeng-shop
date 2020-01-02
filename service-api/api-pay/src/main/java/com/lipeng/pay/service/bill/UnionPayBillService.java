package com.lipeng.pay.service.bill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:13
 */
public interface UnionPayBillService {

    void downloadBill(HttpServletRequest request, HttpServletResponse response);

}