package com.lipeng.zuul.build;

import com.netflix.zuul.context.RequestContext;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class GatewayDirector {

    @Resource(name = "verificationBuild")
    private GatewayBuild gatewayBuild;

    // 责任链 装饰 外观 如何在实际项目中灵活运营设计模式

    /**
     * 连接建造者
     */
    public void direcot(RequestContext ctx, String ipAddres, HttpServletResponse response,
            HttpServletRequest request) {
        // 1.黑名单
        Boolean blackBlock = gatewayBuild.blackBlock(ctx, ipAddres, response);
        if (!blackBlock) {
            return;
        }
//		// // 2.参数验证
//		Boolean verifyMap = gatewayBuild.toVerifyMap(ctx, ipAddres, request);
//		if (!verifyMap) {
//			return;
//		}
        // 3.验证accessToken
        Boolean apiAuthority = gatewayBuild.apiAuthority(ctx, request);
        if (!apiAuthority) {
            return;
        }

    }
    // 如何重构思路；装饰 、责任量 将每不操作存放到抽象工厂中，遍历工厂即可。 遍历遍历所有的实现

}