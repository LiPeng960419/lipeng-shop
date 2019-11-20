package com.lipeng.zuul.handler.impl;

import com.lipeng.zuul.handler.GatewayHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ToVerifyMapHandler extends BaseHandler implements GatewayHandler {

	@Override
	public void service(RequestContext ctx, HttpServletRequest req, HttpServletResponse response) {
		log.info(">>>>>>>api验证签名Handler执行>>>>");
		nextGatewayHandler.service(ctx, req, response);
	}

}
