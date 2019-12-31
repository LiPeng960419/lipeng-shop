package com.lipeng.pay.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/31 15:49
 */
@Data
public class BillSellQueryModel {

    private String alipayOrderNo;

    @NotEmpty
    private String endTime;

    private String merchantOrderNo;

    private String pageNo;

    private String pageSize;

    @NotEmpty
    private String startTime;

    private String storeNo;

}