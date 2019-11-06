package com.lipeng.base;

import com.lipeng.constants.Constants;
import javax.servlet.http.HttpServletRequest;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import org.springframework.ui.Model;

public class BaseWebController {

    /**
     * 500页面
     */
    protected static final String ERROR_500_FTL = "500.ftl";

    /**
     * 重定向到首页
     */
    public static final String REDIRECT_INDEX = "redirect:/";

    // 接口直接返回true 或者false
    public Boolean isSuccess(BaseResponse<?> baseResp) {
        if (baseResp == null) {
            return false;
        }
        return Constants.HTTP_RES_CODE_200.equals(baseResp.getCode());
    }

    /**
     * 获取浏览器信息
     */
    public String webBrowserInfo(HttpServletRequest request) {
        // 获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent"))
                .getBrowser();
        // 获取浏览器版本号
        Version version = browser.getVersion(request.getHeader("User-Agent"));
        String info = browser.getName() + "/" + version.getVersion();
        return info;
    }

    public void setErrorMsg(Model model, String errorMsg) {
        model.addAttribute("error", errorMsg);
    }

}
