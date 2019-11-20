package com.lipeng.zuul.build;

import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GatewayBuild {

    /**
     * 黑名单拦截
     */
    Boolean blackBlock(RequestContext ctx, String ipAddres, HttpServletResponse response);

    /**
     * 参数验证
     */
    Boolean toVerifyMap(RequestContext ctx, String ipAddres, HttpServletRequest request);

    /**
     * api权限控制
     */
    Boolean apiAuthority(RequestContext ctx, HttpServletRequest request);

}