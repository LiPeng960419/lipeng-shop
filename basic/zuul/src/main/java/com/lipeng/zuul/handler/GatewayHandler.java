package com.lipeng.zuul.handler;

import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GatewayHandler {

    /**
     * 每一个Handler执行的方法
     */
    void service(RequestContext ctx, HttpServletRequest req, HttpServletResponse response);

    /**
     * 指向下一个Handler
     */
    void setNextHandler(GatewayHandler gatewayHandler);

}