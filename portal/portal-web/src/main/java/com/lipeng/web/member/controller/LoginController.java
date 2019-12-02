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
import com.lipeng.web.utils.SlidingImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class LoginController extends BaseWebController {

    /**
     * 跳转到登陆页面页面
     */
    public static final String MB_LOGIN_FTL = "member/login";
    // 保存对应的位置，以作对比
    public static Map<String, Localtion> localtion = Collections
            .synchronizedMap(new WeakHashMap<>());

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    /**
     * 跳转页面
     */
    @GetMapping("/login")
    public String getLogin(HttpServletRequest request, Model model) {
        String sessionId = request.getSession().getId();

        // 位置需要随机生成，并且需要注意最大值 原图大小 - 滑动大小 还有就是估计得尽量靠右一些
        localtion.put(sessionId, new Localtion(30, 40));

        Map<String, String> images = null;
        try {
            images = SlidingImage.create(30, 40);
        } catch (IOException e) {
            log.error("create image error", e);
        }

        model.addAttribute("backImage", images.get("backImage"));
        model.addAttribute("slidingImage", images.get("slidingImage"));
        return MB_LOGIN_FTL;
    }

    /**
     * 接受请求参数
     */
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model,
            HttpServletRequest request,
            HttpServletResponse response, HttpSession httpSession) {
        //图形码校验或者kapcha校验
        //图形验证码判断
//        String graphicCode = loginVo.getGraphicCode();
//        if (!RandomValidateCodeUtil.checkVerify(graphicCode, httpSession)) {
//            setErrorMsg(model, "图形验证码不正确!");
//            return MB_LOGIN_FTL;
//        }
        //kapcha验证码校验
        String kaptchaReceived = loginVo.getGraphicCode();
        String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) {
            setErrorMsg(model, "图形验证码不正确!");
            return MB_LOGIN_FTL;
        }

        // 2.将vo转换dto调用会员登陆接口
        UserLoginInpDTO userLoginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
        if (!isSuccess(login)) {
            setErrorMsg(model, login.getMsg());
            return MB_LOGIN_FTL;
        }
        // 3.登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
        JSONObject data = login.getData();
        String token = data.getString(Constants.TOKEN);
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        return BaseWebController.REDIRECT_INDEX;
    }

    /**
     * 位置(左上角)
     */
    class Localtion {

        private int x;
        private int y;

        public Localtion() {
        }

        public Localtion(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

}