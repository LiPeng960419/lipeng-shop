package com.lipeng.spike.service.mapper.entity;

import java.util.Date;
import lombok.Data;

/**
 * 商品库存
 */
@Data
public class SeckillEntity {

    /**
     * 库存id
     */
    private Long seckillId;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 库存数量
     */
    private Integer inventory;

    /**
     * 开启时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 秒杀抢购
     */
    private Long version;

}