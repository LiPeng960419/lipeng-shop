package com.lipeng.zuul.build.impl;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.constants.Constants;
import com.lipeng.core.sign.SignUtil;
import com.lipeng.zuul.build.GatewayBuild;
import com.lipeng.zuul.feign.AuthorizationServiceFeign;
import com.lipeng.zuul.mapper.BlacklistMapper;
import com.lipeng.zuul.mapper.entity.MeiteBlacklist;
import com.netflix.zuul.context.RequestContext;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VerificationBuild implements GatewayBuild {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Autowired
    private AuthorizationServiceFeign verificaCodeServiceFeign;

    @Override
    public Boolean blackBlock(RequestContext ctx, String ipAddres, HttpServletResponse response) {
        MeiteBlacklist meiteBlacklist = blacklistMapper.findBlacklist(ipAddres);
        if (meiteBlacklist != null) {
            resultError(ctx, "ip:" + ipAddres + ",Insufficient access rights");
            return false;
        }
        return true;
    }

    @Override
    public Boolean toVerifyMap(RequestContext ctx, String ipAddres, HttpServletRequest request) {
        // 4.验证签名拦截
        Map<String, String> verifyMap = SignUtil.toVerifyMap(request.getParameterMap(), false);
        if (!SignUtil.verify(verifyMap)) {
            resultError(ctx, "ip:" + ipAddres + ",Sign fail");
            return false;
        }
        return true;
    }

    private void resultError(RequestContext ctx, String errorMsg) {
        ctx.setResponseStatusCode(401);
        // 网关响应为false 不会转发服务
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(errorMsg);
    }

    @Override
    public Boolean apiAuthority(RequestContext ctx, HttpServletRequest request) {
        String accessToken = request.getParameter("accessToken");
        log.info(">>>>>accessToken验证:" + accessToken);
        if (StringUtils.isEmpty(accessToken)) {
            resultError(ctx, "AccessToken cannot be empty");
            return false;
        }
        // 调用接口验证accessToken是否失效
        BaseResponse<JSONObject> appInfo = verificaCodeServiceFeign.getAppInfo(accessToken);
        log.info(">>>>>>data:" + appInfo.toString());
        if (!isSuccess(appInfo)) {
            resultError(ctx, appInfo.getMsg());
            return false;
        }
        return true;
    }

    public Boolean isSuccess(BaseResponse<?> baseResp) {
        if (baseResp == null) {
            return false;
        }
        return Constants.HTTP_RES_CODE_200.equals(baseResp.getCode());
    }

}