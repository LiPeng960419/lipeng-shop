package com.lipeng.zuul.handler.impl;

import com.lipeng.zuul.build.GatewayBuild;
import com.lipeng.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiAuthorityHandler extends BaseHandler implements GatewayHandler {

    @Resource(name = "verificationBuild")
    private GatewayBuild gatewayBuild;

    @Override
    public boolean service(RequestContext ctx, HttpServletRequest req, HttpServletResponse response) {
        log.info(">>>>>>>api验证TokenHandler执行>>>>");
        if (gatewayBuild.apiAuthority(ctx, req)) {
            if (Objects.isNull(nextGatewayHandler)) {
                return true;
            }
            return nextGatewayHandler.service(ctx, req, response);
        }
        return false;
    }

}