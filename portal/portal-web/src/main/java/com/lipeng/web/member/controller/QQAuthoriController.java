package com.lipeng.web.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.constants.Constants;
import com.lipeng.core.bean.MeiteBeanUtils;
import com.lipeng.core.utils.CookieUtils;
import com.lipeng.member.dto.UserLoginInpDTO;
import com.lipeng.web.constants.WebConstants;
import com.lipeng.web.member.controller.req.vo.LoginVo;
import com.lipeng.web.member.feign.MemberLoginServiceFeign;
import com.lipeng.web.member.feign.QQAuthoriFeign;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private static final String MB_QQ_QQLOGIN = "member/qqlogin";

    @Autowired
    private QQAuthoriFeign qqAuthoriFeign;

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    /**
     * 生成授权链接
     */
    @RequestMapping("/qqAuth")
    public String qqAuth(HttpServletRequest request) {
        try {
            String authorizeURL = new Oauth().getAuthorizeURL(request);
            log.info("QQ生成授权链接:{}", authorizeURL);
            return "redirect:" + authorizeURL;
        } catch (Exception e) {
            return ERROR_500_FTL;
        }
    }

    /*
     授权返回本地链接
     */
    @RequestMapping("/qqLoginBack")
    public String qqLoginBack(String code, HttpServletRequest request, HttpServletResponse response,
            HttpSession httpSession) throws QQConnectException {
        try {
            // 1.获取授权码COde
            // 2.使用授权码Code获取accessToken
            AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
            if (accessTokenObj == null || StringUtils.isEmpty(accessTokenObj.getAccessToken())) {
                log.info("QQ登录本地回调未获取到AccessToken!");
                return ERROR_500_FTL;
            }
            // 使用accessToken获取用户openid
            String accessToken = accessTokenObj.getAccessToken();
            OpenID openIDObj = new OpenID(accessToken);
            String openId = openIDObj.getUserOpenID();
            if (StringUtils.isEmpty(openId)) {
                log.error("QQ登录本地回调未获取到openId!");
                return ERROR_500_FTL;
            }
            // 使用openid 查询数据库是否已经关联账号信息
            BaseResponse<JSONObject> findByOpenId = qqAuthoriFeign.findByOpenId(openId);
            if (!isSuccess(findByOpenId)) {
                log.error("根据openId获取用户信息异常!");
                return ERROR_500_FTL;
            }
            // 如果调用接口返回203 ,跳转到关联账号页面
            //不存在用户 跳转到关联页面
            if (Constants.HTTP_RES_CODE_NOTUSER_203.equals(findByOpenId.getCode())) {
                // 页面需要展示 QQ头像
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openId);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean == null) {
                    log.error("根据accessToken,openId获取QQ用户信息异常!");
                    return ERROR_500_FTL;
                }
                // 用户的QQ头像
                String avatarURL100 = userInfoBean.getAvatar().getAvatarURL100();
                request.setAttribute("avatarURL100", avatarURL100);
                // 需要将openid存入在session中
                httpSession.setAttribute(WebConstants.LOGIN_QQ_OPENID, openId);
                return MB_QQ_QQLOGIN;
            }
            // 如果能够查询到用户信息的话,直接自动登陆
            JSONObject data = findByOpenId.getData();
            String token = data.getString("token");
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
        // 1.获取用户openid
        String qqOpenId = (String) request.getSession().getAttribute(WebConstants.LOGIN_QQ_OPENID);
        if (StringUtils.isEmpty(qqOpenId)) {
            log.error("session中不存在openId!");
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
            return MB_QQ_QQLOGIN;
        }
        // 3.登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
        JSONObject data = login.getData();
        String token = data.getString("token");
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        return REDIRECT_INDEX;
    }

}