package com.lipeng.pay.service.bill;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:13
 */
@Controller
@RequestMapping("/bill")
public interface BillService {

    @GetMapping("/downloadBill")
    void downloadBill(@RequestParam("billType") String billType,
            @RequestParam("billDate") String billDate, HttpServletResponse response);

    @GetMapping("/query")
    BaseResponse<JSONObject> queryBill(@RequestParam("billType") String billType,
            @RequestParam("billDate") String billDate);

}