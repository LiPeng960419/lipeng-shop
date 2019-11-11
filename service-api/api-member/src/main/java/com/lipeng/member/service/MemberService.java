package com.lipeng.member.service;

import com.lipeng.base.BaseResponse;
import com.lipeng.member.dto.UserLoginInpDTO;
import com.lipeng.member.dto.UserOutDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberService {

    /**
     * 根据手机号码查询是否已经存在,如果存在返回当前用户信息
     */
    @ApiOperation(value = "根据手机号码查询是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", required = true, value = "用户手机号码"),})
    @PostMapping("/existMobile")
    BaseResponse<UserOutDTO> existMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据token查询用户信息
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "/getUserInfo")
    BaseResponse<UserOutDTO> getInfo(@RequestParam("token") String token);

    /**
     * SSO认证系统登陆接口
     */
    @PostMapping("/ssoLogin")
    @ApiOperation(value = "/ssoLogin")
    BaseResponse<UserOutDTO> ssoLogin(@RequestBody UserLoginInpDTO userLoginInpDTO);

}