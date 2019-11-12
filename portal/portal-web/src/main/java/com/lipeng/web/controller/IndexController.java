package com.lipeng.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.constants.Constants;
import com.lipeng.core.utils.CookieUtils;
import com.lipeng.core.utils.UrlUtil;
import com.lipeng.member.dto.UserLoginInpDTO;
import com.lipeng.member.dto.UserOutDTO;
import com.lipeng.web.constants.WebConstants;
import com.lipeng.web.member.controller.LoginController;
import com.lipeng.web.member.feign.MemberLoginServiceFeign;
import com.lipeng.web.member.feign.MemberServiceFeign;
import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.login.SsoWebLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController extends BaseWebController {

    /**
     * 跳转到index页面
     */
    private static final String INDEX_FTL = "index";

    @Value("${xxl.sso.server}")
    private String xxlSsoServer;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 1.从cookie 中 获取 会员token
        String token = CookieUtils.getCookieValue(request, WebConstants.LOGIN_TOKEN_COOKIENAME, true);
        show(model, token);
        autoLogin(request, response, model);
        return INDEX_FTL;
    }

    @RequestMapping("/exit")
    public String exit(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieUtils.deleteCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME);
        request.getSession().invalidate();
        model.addAttribute("desensMobile", null);
        String requestUrl = request.getRequestURL().toString();
        StringBuilder redirctUrl = new StringBuilder(xxlSsoServer).append(Conf.SSO_LOGOUT)
                .append("?").append(Conf.REDIRECT_URL).append("=")
                .append(requestUrl.substring(0, requestUrl.lastIndexOf("/") + 1));
        return REDIRECT + redirctUrl;
    }

    private String autoLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);
        if (Objects.isNull(xxlUser)) {
            return INDEX_FTL;
        }
        UserLoginInpDTO userLoginInpDTO = new UserLoginInpDTO();
        userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String mobile = UrlUtil.deCryptAndDecode(xxlUser.getPlugininfo().get("mobile"));
        String pass = UrlUtil.deCryptAndDecode(xxlUser.getPlugininfo().get("password"));
        userLoginInpDTO.setMobile(mobile);
        userLoginInpDTO.setPassword(pass);
        String info = webBrowserInfo(request);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
        if (!isSuccess(login)) {
            log.error("error mobile phone or password, login failed");
            return LoginController.MB_LOGIN_FTL;
        }
        JSONObject data = login.getData();
        String token = data.getString(Constants.TOKEN);
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        show(model, token);
        return BaseWebController.REDIRECT_INDEX;
    }

    private void show(Model model, String token) {
        if (!StringUtils.isEmpty(token)) {
            //调用会员服务接口,查询会员用户信息
            BaseResponse<UserOutDTO> userInfo = memberServiceFeign.getInfo(token);
            if (isSuccess(userInfo)) {
                UserOutDTO data = userInfo.getData();
                if (data != null) {
                    String mobile = data.getMobile();
                    // 对手机号码实现脱敏
                    String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    model.addAttribute("desensMobile", desensMobile);
                }
            }
        }
    }

}