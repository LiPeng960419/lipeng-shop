package com.lipeng.member.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.constants.Constants;
import com.lipeng.core.token.GenerateToken;
import com.lipeng.member.mapper.UserMapper;
import com.lipeng.member.mapper.entity.UserDo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeiBoAuthoriServiceImpl extends BaseApiService<JSONObject> implements
        WeiboAuthoriService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> findByOpenId(String weiboOpenId) {
        // 1.根据qqOpenId查询用户信息
        if (StringUtils.isEmpty(weiboOpenId)) {
            return setResultError("weiboOpenId不能为空!");
        }
        // 2.如果没有查询到 直接返回状态码203
        UserDo userDo = userMapper.findByWeixinOpenId(weiboOpenId);
        if (userDo == null) {
            return setResultError(Constants.HTTP_RES_CODE_NOTUSER_203, "根据weiboOpenId没有查询到用户信息");
        }
        // 3.如果能够查询到用户信息的话 返回对应用户信息token
        String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + Constants.MEMBER_LOGIN_TYPE_WEIBO;
        Long userId = userDo.getUserId();
        String userToken = generateToken.createToken(keyPrefix, String.valueOf(userId));
        JSONObject data = new JSONObject();
        data.put(Constants.TOKEN, userToken);
        return setResultSuccess(data);
    }

}