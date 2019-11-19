package com.lipeng.pay.mapper.entity;

import java.util.Date;
import lombok.Data;

/**
 * 日志存储
 */
@Data
public class PaymentTransactionLogEntity {

    /**
     * 主键ID
     */

    private Integer id;
    /**
     * 同步回调日志
     */
    private String synchLog;
    /**
     * 异步回调日志
     */
    private String asyncLog;
    /**
     * 支付渠道ID
     */
    private Integer channelId;
    /**
     * 支付交易ID
     */
    private String transactionId;
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     *
     */
    private String untitled;
}
