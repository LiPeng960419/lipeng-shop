package com.lipeng.pay.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/31 16:43
 */
@Data
public class BillAccountlogQueryModel {

    private String alipayOrderNo;

    @NotEmpty
    private String endTime;

    private String merchantOrderNo;

    private String pageNo;

    private String pageSize;

    @NotEmpty
    private String startTime;

}