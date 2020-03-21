package com.lipeng.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户登陆参数")
public class UserLoginInpDTO {

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 登陆类型 PC、Android 、IOS
     */
    @ApiModelProperty(value = "登陆类型")
    private String loginType;
    /**
     * 设备信息
     */
    @ApiModelProperty(value = "设备信息")
    private String deviceInfor;

    /**
     * qq openId
     */
    @ApiModelProperty(value = "QQ openId")
    private String qqOpenId;

    /**
     * weixin openId
     */
    @ApiModelProperty(value = "weixin openId")
    private String weixinOpenId;

    @ApiModelProperty(value = "weibo openId")
    private String weiboOpenId;

}