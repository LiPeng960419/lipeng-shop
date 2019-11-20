package com.lipeng.zuul.mapper;

import com.lipeng.zuul.mapper.entity.GatewayHandlerEntity;
import org.apache.ibatis.annotations.Select;


public interface GatewayHandlerMapper {

    /**
     * 获取第一个GatewayHandler
     */
    @Select("SELECT handler_name AS handlerName, HANDLERID AS handlerId, PREHANDLERID AS prevhandlerId, NEXTHANDLERID AS nextHandlerId, ISOPEN AS isOpen FROM gateway_handler WHERE ISOPEN = 1 and PREHANDLERID is null;")
    GatewayHandlerEntity getFirstGatewayHandler();

    @Select("SELECT handler_name AS handlerName, HANDLERID AS handlerId, PREHANDLERID AS prevhandlerId, NEXTHANDLERID AS nextHandlerId, ISOPEN AS isOpen FROM gateway_handler WHERE ISOPEN = 1 and HANDLERID = #{handlerId}")
    GatewayHandlerEntity getByHandler(String handlerId);

}