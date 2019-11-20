package com.lipeng.zuul.handler.impl;

import com.lipeng.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiAuthorityHandler extends BaseHandler implements GatewayHandler {

    @Override
    public void service(RequestContext ctx, HttpServletRequest req, HttpServletResponse response) {
        log.info(">>>>>>>api验证TokenHandler执行>>>>");
    }

}
