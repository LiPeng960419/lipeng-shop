package com.lipeng.base;

import java.util.Date;
import lombok.Data;

@Data
public class BaseDo {

    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * id
     */
    private Long id;

    /**
     * 是否可用 0可用 1不可用
     */
    private Long isAvailability;

}