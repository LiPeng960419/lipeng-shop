package com.lipeng.zuul.mapper.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class GatewayHandlerEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = -4785629506074021461L;
    /**
     * 主键ID
     */
    @Id
    private Integer id;
    /**
     * handler名称
     */
    private String handlerName;
    /**
     * handler主键id
     */
    private String handlerId;

    /**
     * 上一个handler
     */
    private String prevhandlerId;
    /**
     * 下一个handler
     */
    private String nextHandlerId;

    /**
     * 是否打开
     */
    private Integer isOpen;
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

}
