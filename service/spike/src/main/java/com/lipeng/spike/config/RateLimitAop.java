package com.lipeng.spike.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.lipeng.base.BaseApiService;
import com.lipeng.constants.Constants;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: lipeng 910138
 * @Date: 2019/10/14 16:14
 */
@Component
@Aspect
@Slf4j
@SuppressWarnings("UnstableApiUsage")
public class RateLimitAop extends BaseApiService<JSONObject> {

    private static ConcurrentHashMap<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<String, RateLimiter>();

    @Pointcut("@annotation(com.lipeng.spike.config.RateLimitAspect)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()&&@annotation(rateLimitAspect)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimitAspect rateLimitAspect) {
        // 获取配置的速率
        double value = rateLimitAspect.value();
        // 获取等待令牌等待时间
        long timeOut = rateLimitAspect.timeOut();

        RateLimiter rateLimiter = getRateLimiter(value);
        boolean flag = rateLimiter.tryAcquire(timeOut, TimeUnit.MILLISECONDS);
        Object obj = null;
        try {
            if (flag) {
                obj = joinPoint.proceed();
            } else {
                String result = JSONObject.toJSONString(setResult(Constants.HTTP_RES_CODE_500, "接口暂时限流", null));
                String methodUrl = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
                log.info("方法:[{}]调用接口暂时限流,返回结果:{}", methodUrl, result);
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                output(result, response);
            }
        } catch (Throwable e) {
            log.error("接口限流异常", e);
        }
        return obj;
    }

    // 获取RateLimiter对象
    private RateLimiter getRateLimiter(double value) {
        // 获取当前URL
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        RateLimiter rateLimiter = null;
        if (!rateLimiterMap.containsKey(requestURI)) {
            // 开启令牌通限流
            rateLimiter = RateLimiter.create(value); // 独立线程
            rateLimiterMap.put(requestURI, rateLimiter);
        } else {
            rateLimiter = rateLimiterMap.get(requestURI);
        }
        return rateLimiter;
    }


    private void output(String result, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(result.getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
            log.error("Respongse写异常", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

}