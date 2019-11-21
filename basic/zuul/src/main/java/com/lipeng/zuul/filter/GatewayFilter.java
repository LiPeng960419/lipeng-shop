package com.lipeng.zuul.filter;

import com.lipeng.zuul.handler.GatewayHandler;
import com.lipeng.zuul.handler.ResponsibilityClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GatewayFilter extends ZuulFilter {

    @Autowired
    private ResponsibilityClient responsibilityClient;

    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 1.获取请求对象
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        GatewayHandler handler = responsibilityClient.getHandler();
        handler.service(ctx, request, response);
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 在请求之前实现拦截
     */
    public String filterType() {
        return "pre";
    }

}