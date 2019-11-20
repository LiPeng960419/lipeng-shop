package com.lipeng.zuul.handler.impl;

import com.lipeng.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BlacklistHandler extends BaseHandler implements GatewayHandler {

    @Override
    public void service(RequestContext ctx, HttpServletRequest req, HttpServletResponse response) {
        log.info(">>>>>>>黑名单Handler执行>>>>");
        nextGatewayHandler.service(ctx, req, response);
    }

    // 有多种 可以使用模版方案设计模式或者 base

}