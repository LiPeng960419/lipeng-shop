package com.lipeng.zuul.handler.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.lipeng.core.token.GenerateToken;
import com.lipeng.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@SuppressWarnings("UnstableApiUsage")
public class CurrentLimitHandler extends BaseHandler implements GatewayHandler {

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Autowired
    private GenerateToken generateToken;

    @Override
    public boolean service(RequestContext ctx, HttpServletRequest req, HttpServletResponse response) {
        if (StringUtils.isEmpty(req.getParameter("seckillId"))){
            if (Objects.isNull(nextGatewayHandler)) {
                return true;
            }
            return nextGatewayHandler.service(ctx, req, response);
        }
        // 1.用户限流频率设置 每秒中限制1个请求
        boolean tryAcquire = rateLimiter.tryAcquire(0, TimeUnit.SECONDS);
        if (!tryAcquire) {
            resultError(500, ctx, "现在抢购的人数过多，请稍等一下下哦！");
            return false;
        }
        // 2.使用redis限制用户访问频率
        String seckillId = req.getParameter("seckillId");
        String seckillToken = generateToken.getListKeyToken(seckillId);
        if (StringUtils.isEmpty(seckillToken)) {
            log.info(">>>seckillId:{}, 亲，该秒杀已经售空，请下次再来!", seckillId);
            resultError(500, ctx, "亲，该秒杀已经售空，请下次再来!");
            return false;
        }
        // 3.执行修改库存操作
        if (Objects.isNull(nextGatewayHandler)) {
            return true;
        }
        return nextGatewayHandler.service(ctx, req, response);
    }

}
