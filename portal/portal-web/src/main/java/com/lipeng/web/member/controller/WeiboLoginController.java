package com.lipeng.web.member.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.base.BaseWebController;
import com.lipeng.constants.Constants;
import com.lipeng.core.bean.MeiteBeanUtils;
import com.lipeng.core.utils.CookieUtils;
import com.lipeng.member.dto.UserInpDTO;
import com.lipeng.member.dto.UserLoginInpDTO;
import com.lipeng.web.common.WeiBoConfig;
import com.lipeng.web.constants.WebConstants;
import com.lipeng.web.member.controller.req.vo.LoginVo;
import com.lipeng.web.member.feign.MemberLoginServiceFeign;
import com.lipeng.web.member.feign.WeiBoAuthoriFeign;
import com.lipeng.web.utils.HttpClientUtil;
import com.lipeng.web.utils.WeiBoUserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/weibo")
@Slf4j
public class WeiboLoginController extends BaseWebController {

    //微信第三方返回的accessToken
    private static final String ACCESSTOKEN = "access_token";

    /*
    存到session用来登录时修改用户的个性化信息
     */
    private static final String WEIBO_ACCESSTOKEN = "weibo_access_token";

    private static final String OPENID = "uid";

    /**
     * 跳转到微博登录页面
     */
    private static final String MB_WEIBO_LOGIN = "member/weibologin";

    @Autowired
    private WeiBoConfig weiBoConfig;

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    @Autowired
    private WeiBoAuthoriFeign weiBoAuthoriFeign;

    //微博第一次关联成功后之后自动授权 执行第二步回调
    @GetMapping("/login")
    public String auth() {
        String clientId = weiBoConfig.getAppkey();
        String returnUrl = REDIRECT + WeiBoUserInfoUtil.getCode(clientId, weiBoConfig.getReDirectUri());
        log.info("跳转微博登录页面:{}", returnUrl);
        return returnUrl;
    }

    @RequestMapping("/getAccessToken")
    public String getToken(@RequestParam(name = "code") String code, HttpSession httpSession) {
        String WebAccessToken = "";
        String openId = "";
        String tokenUrl = WeiBoUserInfoUtil.getWebAccess();
        log.info("微博请求获取token的url为:" + tokenUrl);
        try {
            //1:通过https方式请求获得web_access_token
            Map<String, String> param = WeiBoUserInfoUtil.getTokenParams(weiBoConfig, code);
            String tokenResponse = HttpClientUtil.doPost(tokenUrl, param);
            if (StringUtils.isEmpty(tokenResponse)) {
                log.error("微博授权登录获取token失败");
                return ERROR_500_FTL;
            }
            JSONObject tokenObject = JSON.parseObject(tokenResponse);
            WebAccessToken = tokenObject.getString(ACCESSTOKEN);
            openId = tokenObject.getString(OPENID);
            if (StringUtils.isEmpty(WebAccessToken) || StringUtils.isEmpty(openId)) {
                log.error("微博授权登录获取token失败");
                return ERROR_500_FTL;
            }
            log.info("获取access_token:{},openId:{}成功:", WebAccessToken, openId);
            // 使用openid 查询数据库是否已经关联账号信息
            BaseResponse<JSONObject> findByOpenId = weiBoAuthoriFeign.findByOpenId(openId);
            if (!isSuccess(findByOpenId)) {
                log.error("微信根据openId获取用户信息异常!");
                return ERROR_500_FTL;
            }
            // 如果调用接口返回203 ,跳转到关联账号页面
            //不存在用户 跳转到关联页面
            if (Constants.HTTP_RES_CODE_NOTUSER_203.equals(findByOpenId.getCode())) {
                // 需要将openid存入在session中
                httpSession.setAttribute(WebConstants.LOGIN_WEIBO_OPENID, openId);
                httpSession.setAttribute(WEIBO_ACCESSTOKEN, WebAccessToken);
                return MB_WEIBO_LOGIN;
            } else if (Constants.HTTP_RES_CODE_200.equals(findByOpenId.getCode())) {
                return REDIRECT_INDEX;
            }
        } catch (Exception e) {
            log.error("微博获取token异常", e);
        }
        return ERROR_500_FTL;
    }

    /*
       第一次登录 openId存放在session里面
    */
    @RequestMapping("/weiboJointLogin")
    public String qqJointLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model,
                               HttpServletRequest request, HttpServletResponse response) {
        //kapcha验证码校验
        String kaptchaReceived = loginVo.getGraphicCode();
        String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) {
            setErrorMsg(model, "图形验证码不正确!");
            return MB_WEIBO_LOGIN;
        }
        // 1.获取用户openid
        String openId = (String) request.getSession().getAttribute(WebConstants.LOGIN_WEIBO_OPENID);
        String weixinSessionToken = (String) request.getSession().getAttribute(WEIBO_ACCESSTOKEN);
        if (StringUtils.isEmpty(openId)) {
            log.error("session中不存在微博openId!");
            return ERROR_500_FTL;
        }
        // 2.将vo转换dto调用会员登陆接口
        UserLoginInpDTO userLoginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        userLoginInpDTO.setWeiboOpenId(openId);
        userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
        if (!isSuccess(login)) {
            setErrorMsg(model, login.getMsg());
            return MB_WEIBO_LOGIN;
        }
        JSONObject data = login.getData();
        //2:通过https方式请求获得用户信息响应
        String userMessageUrl = WeiBoUserInfoUtil.getUserMessage(weixinSessionToken, openId);
        String userResponse = HttpClientUtil.doGet(userMessageUrl);
        if (StringUtils.isNotEmpty(userResponse)) {
            //微博返回的用户信息
            JSONObject userObject = JSON.parseObject(userResponse);
            //登录后返回的用户信息userId
            Long userId = data.getLong("userId");
            UserInpDTO userInpDTO = new UserInpDTO();
            userInpDTO.setUserId(userId);
            userInpDTO.setUserName(userObject.getString("screen_name"));
            if ("m".equals(userObject.getString("gender"))) {
                userInpDTO.setSex(Constants.MAN);
            } else if ("f".equals(userObject.getString("gender"))) {
                userInpDTO.setSex(Constants.FEMALE);
            } else {
                userInpDTO.setSex(Constants.UNKONW);
            }
            BaseResponse<JSONObject> updateUserInfo = memberLoginServiceFeign.updateUserInfo(userInpDTO);
            if (!Constants.HTTP_RES_CODE_200.equals(updateUserInfo.getCode())) {
                log.error(updateUserInfo.getMsg() + ",userId:" + userId);
            } else {
                log.info(updateUserInfo.getMsg() + ",userId:" + userId);
            }
        }
        // 3.登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
        String token = data.getString(Constants.TOKEN);
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        return REDIRECT_INDEX;
    }

}