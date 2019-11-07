package com.lipeng.member.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.member.dto.UserInpDTO;
import com.lipeng.member.dto.UserLoginInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "用户登陆服务接口")
public interface MemberLoginService {

    /**
     * 用户登陆接口
     */
    @PostMapping("/login")
    @ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO);

    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "修改会员用户信息接口")
    BaseResponse<JSONObject> updateUserInfo(@RequestBody UserInpDTO userInpDTO);

}