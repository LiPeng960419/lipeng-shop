package com.lipeng.pay.impl.bill;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.pay.service.bill.UnionPayBillService;
import lombok.extern.slf4j.Slf4j;
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

}