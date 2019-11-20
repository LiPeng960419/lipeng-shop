package com.lipeng.zuul.handler;

import com.lipeng.core.utils.SpringContextUtil;
import com.lipeng.zuul.mapper.GatewayHandlerMapper;
import com.lipeng.zuul.mapper.entity.GatewayHandlerEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponsibilityClient {

    @Autowired
    private GatewayHandlerMapper gatewayHandlerMapper;

    private GatewayHandler firstGatewayHandler;

    public GatewayHandler getHandler() {
        if (this.firstGatewayHandler != null) {
            return this.firstGatewayHandler;
        }
        log.info(">>>>>从数据库中获取最新的Handler开始<<<<<<<<<<<<<<<<<<");
        // 1.数据库查询第一个Handler
        GatewayHandlerEntity firstGatewayHandlerEntity = gatewayHandlerMapper
                .getFirstGatewayHandler();
        if (firstGatewayHandlerEntity == null) {
            return null;
        }
        // 2.获取第一个handlerId springbean注入id
        String firstHandlerId = firstGatewayHandlerEntity.getHandlerId();
        if (StringUtils.isEmpty(firstHandlerId)) {
            return null;
        }
        // 3.从spring容器中加载第一个handler
        GatewayHandler firstGatewayHandler = (GatewayHandler) SpringContextUtil
                .getBean(firstHandlerId);
        // 4.获取下一个Handlerid
        String nextHandlerId = firstGatewayHandlerEntity.getNextHandlerId();
        // 5.记录每一次循环控制的hanlder
        GatewayHandler tempGatewayHandler = firstGatewayHandler;
        while (!StringUtils.isEmpty(nextHandlerId)) {
            // 3.查询数据库下一个Handler信息
            GatewayHandlerEntity nextGatewayHandlerEntity = gatewayHandlerMapper
                    .getByHandler(nextHandlerId);
            if (nextGatewayHandlerEntity == null) {
                break;
            }
            // 4.从springboot中获取下一个handlerbean细细
            String handlerId = nextGatewayHandlerEntity.getHandlerId();
            GatewayHandler nextGatewayHandler = (GatewayHandler) SpringContextUtil
                    .getBean(handlerId);
            // 5.执行下一个hanlder
            nextHandlerId = nextGatewayHandlerEntity.getNextHandlerId();
            // 设置指向下一个handerl
            tempGatewayHandler.setNextHandler(nextGatewayHandler);
            tempGatewayHandler = nextGatewayHandler;
        }
        // 3.直接把第一个Handler 放入到缓存中..
        log.info(">>>>>从数据库中获取最新的Handler结束<<<<<<<<<<<<<<<<<<");
        this.firstGatewayHandler = firstGatewayHandler;
        return firstGatewayHandler;
    }
}
