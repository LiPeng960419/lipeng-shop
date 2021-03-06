package com.lipeng.pay.constant;

/**
 * 支付相关常量数据
 */
public interface PayConstant {

    String RESULT_NAME = "result";
    String RESULT_PAYCODE_201 = "201";
    String RESULT_PAYCODE_200 = "200";
    /**
     * 已经支付成功状态
     */
    Integer PAY_STATUS_SUCCESS = 1;

    /**
     * 支付失败状态
     */
    Integer PAY_STATUS_FAIL = 2;

    /**
     * 退款成功
     */
    Integer PAY_STATUS_REFUND_SUCCESS = 3;

    /**
     * 退款失败
     */
    Integer PAY_STATUS_REFUND_FAIL = 4;
    /**
     * 返回银联通知成功
     */
    String YINLIAN_RESULT_SUCCESS = "ok";
    /**
     * 返回银联失败通知
     */
    String YINLIAN_RESULT_FAIL = "fail";

    /**
     * 返回银联通知成功
     */
    String ALI_RESULT_SUCCESS = "ok";
    /**
     * 返回银联失败通知
     */
    String ALI_RESULT_FAIL = "fail";
}
