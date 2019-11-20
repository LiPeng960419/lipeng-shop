package com.lipeng.zuul.handler.impl;

import com.lipeng.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;

public class BaseHandler {

    public GatewayHandler nextGatewayHandler;

    public void setNextHandler(GatewayHandler nextGatewayHandler) {
        this.nextGatewayHandler = nextGatewayHandler;
    }

    public void resultError(Integer code, RequestContext ctx, String errorMsg) {
        ctx.setResponseStatusCode(code);
        // 网关响应为false 不会转发服务
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(errorMsg);
        ctx.getResponse().setContentType("text/html;charset=UTF-8");

    }

}
