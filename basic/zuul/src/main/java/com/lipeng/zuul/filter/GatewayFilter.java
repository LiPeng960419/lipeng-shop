package com.lipeng.zuul.filter;

import com.lipeng.zuul.handler.GatewayHandler;
import com.lipeng.zuul.handler.ResponsibilityClient;
import com.lipeng.zuul.mapper.BlacklistMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
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

    /**
     * 过滤参数
     */
    private Map<String, List<String>> filterParameters(HttpServletRequest request,
            RequestContext ctx) {
        Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
        if (requestQueryParams == null) {
            requestQueryParams = new HashMap<>();
        }
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            ArrayList<String> arrayList = new ArrayList<>();
            // 将参数转化为html参数 防止xss攻击
            arrayList.add(StringEscapeUtils.escapeHtml(value));
            requestQueryParams.put(name, arrayList);
        }
        return requestQueryParams;
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