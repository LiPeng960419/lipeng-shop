package com.lipeng.web.member.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.constants.Constants;
import com.lipeng.core.bean.MeiteBeanUtils;
import com.lipeng.core.utils.CookieUtils;
import com.lipeng.member.dto.UserLoginInpDTO;
import com.lipeng.web.common.QQConfig;
import com.lipeng.web.common.QQUserInfo;
import com.lipeng.web.constants.WebConstants;
import com.lipeng.web.member.controller.req.vo.LoginVo;
import com.lipeng.web.member.feign.MemberLoginServiceFeign;
import com.lipeng.web.member.feign.QQAuthoriFeign;
import com.lipeng.web.utils.HttpClientUtil;
import com.lipeng.web.utils.QQUserInfoUtil;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/6 15:37
 */
@Slf4j
@Controller
public class QQAuthoriController extends BaseWebController {

    /**
     * 跳转到qq登录页面
     */
    private static final String MB_QQ_LOGIN = "member/qqlogin";

    private static final String QQ_LOGIN_STATE = "qq_login_state";

    @Autowired
    private QQAuthoriFeign qqAuthoriFeign;

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    @Autowired
    private QQConfig qqConfig;

    /**
     * 生成授权链接
     */
    @RequestMapping("/qqAuth")
    public String qqAuth() {
        try {
            String url = QQUserInfoUtil.getAuthorization(qqConfig.getAppid(),
                    URLEncoder.encode(qqConfig.getReDirectUri(), "UTF-8"), QQ_LOGIN_STATE);
            log.info("QQ生成授权链接:{}", url);
            return "redirect:" + url;
        } catch (Exception e) {
            return ERROR_500_FTL;
        }
    }

    /*
     授权返回本地链接
     */
    @RequestMapping("/qqLoginBack")
    public String qqLoginBack(String code, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 获取授权码COde 2.使用授权码Code获取accessToken
            Map<String, Object> qqProperties = getToken(code);
            if (Objects.isNull(qqProperties) || StringUtils
                    .isEmpty((String) qqProperties.get("accessToken"))) {
                log.info("QQ登录本地回调未获取到AccessToken!");
                return ERROR_500_FTL;
            }
            String accessToken = (String) qqProperties.get("accessToken");
            // 使用accessToken获取用户openid
            String openId = getOpenId(accessToken);
            if (StringUtils.isEmpty(openId)) {
                log.error("QQ登录本地回调未获取到openId!");
                return ERROR_500_FTL;
            }
            // 使用openid 查询数据库是否已经关联账号信息 没有返回203
            BaseResponse<JSONObject> findByOpenId = qqAuthoriFeign.findByOpenId(openId);
            if (!isSuccess(findByOpenId)) {
                log.error("QQ根据openId获取用户信息异常!");
                return ERROR_500_FTL;
            }
            // 不存在用户接口返回203 ,跳转到关联账号页面
            if (Constants.HTTP_RES_CODE_NOTUSER_203.equals(findByOpenId.getCode())) {
                qqProperties.put("openId", openId);
                QQUserInfo userInfo = getUserInfo(qqProperties);
                if (Objects.nonNull(userInfo)) {
                    // 用户的QQ头像  大小为100×100像素的QQ空间头像URL。
                    String avatarURL100 = userInfo.getFigureurl_2();
                    request.setAttribute("avatarURL100", avatarURL100);
                } else {
                    request.setAttribute("avatarURL100", qqConfig.getDefaultPicUrl());
                    log.error("根据accessToken{},openId{}获取QQ用户信息异常!", accessToken, openId);
                }
                // 需要将openid存入在session中
                request.getSession().setAttribute(WebConstants.LOGIN_QQ_OPENID, openId);
                return MB_QQ_LOGIN;
            }
            // 如果能够查询到用户信息的话,直接自动登陆
            JSONObject data = findByOpenId.getData();
            String token = data.getString(Constants.TOKEN);
            CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
            return REDIRECT_INDEX;
        } catch (Exception e) {
            log.error("error", e);
            return ERROR_500_FTL;
        }
    }

    /*
    第一次登录 openId存放在session里面
     */
    @RequestMapping("/qqJointLogin")
    public String qqJointLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        //kapcha验证码校验
        String kaptchaReceived = loginVo.getGraphicCode();
        String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) {
            setErrorMsg(model, "图形验证码不正确!");
            return MB_QQ_LOGIN;
        }
        // 1.获取用户openid
        String qqOpenId = (String) request.getSession().getAttribute(WebConstants.LOGIN_QQ_OPENID);
        if (StringUtils.isEmpty(qqOpenId)) {
            log.error("session中不存在QQopenId!");
            return ERROR_500_FTL;
        }
        // 2.将vo转换dto调用会员登陆接口
        UserLoginInpDTO userLoginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        userLoginInpDTO.setQqOpenId(qqOpenId);
        userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
        if (!isSuccess(login)) {
            setErrorMsg(model, login.getMsg());
            return MB_QQ_LOGIN;
        }
        // 3.登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
        JSONObject data = login.getData();
        String token = data.getString(Constants.TOKEN);
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        return REDIRECT_INDEX;
    }

    public Map<String, Object> getToken(String code) throws Exception {
        String url = QQUserInfoUtil.getAccessToken(qqConfig.getAppid(), qqConfig.getAppkey(), code,
                URLEncoder.encode(qqConfig.getReDirectUri(), "UTF-8"));
        // 获得token
        String result = HttpClientUtil.doGet(url);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        //token信息
        Map<String, Object> qqProperties = new HashMap<String, Object>();
        qqProperties.put("accessToken", accessToken);
        qqProperties.put("expiresIn", String.valueOf(expiresIn));
        qqProperties.put("refreshToken", refreshToken);
        return qqProperties;
    }

    /*
    获取用户openId（根据token）
     */
    public String getOpenId(String accessToken) {
        String url = QQUserInfoUtil.getOpenId(accessToken);
        String result = HttpClientUtil.doGet(url);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        String openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
        return openId;
    }

    /**
     * 根据token,openId获取用户信息
     */
    public QQUserInfo getUserInfo(Map<String, Object> qqProperties) {
        try {
            // 取token
            String accessToken = (String) qqProperties.get("accessToken");
            String openId = (String) qqProperties.get("openId");
            if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openId)) {
                return null;
            }
            String url = QQUserInfoUtil.getUserInfo(accessToken, qqConfig.getAppid(), openId);
            // 获取qq相关数据
            String result = HttpClientUtil.doGet(url);
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            QQUserInfo userInfo = JSON.parseObject(result, QQUserInfo.class);
            return userInfo;
        } catch (Exception e) {
            log.error("get QQUserInfo error", e);
        }
        return null;
    }

}