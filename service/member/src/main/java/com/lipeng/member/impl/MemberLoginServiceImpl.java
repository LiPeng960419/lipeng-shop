package com.lipeng.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.constants.Constants;
import com.lipeng.core.token.GenerateToken;
import com.lipeng.core.utils.MD5Util;
import com.lipeng.member.common.transaction.RedisDataSoureceTransaction;
import com.lipeng.member.dto.UserLoginInpDTO;
import com.lipeng.member.mapper.UserMapper;
import com.lipeng.member.mapper.UserTokenMapper;
import com.lipeng.member.mapper.entity.UserDo;
import com.lipeng.member.mapper.entity.UserTokenDo;
import com.lipeng.member.service.MemberLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements
        MemberLoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private UserTokenMapper userTokenMapper;
    @Autowired
    private RedisDataSoureceTransaction redisDataSoureceTransaction;

    public BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        // 1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 判断登陆类型
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        // 目的是限制范围
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType
                .equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }

        // 2.对登陆密码实现加密
        String newPassWord = MD5Util.MD5(password);
        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
        UserDo userDo = userMapper.login(mobile, newPassWord);
        if (userDo == null) {
            return setResultError("用户名称或者密码错误!");
        }
        // 用户登陆Token Session 区别
        // 用户每一个端登陆成功之后，会对应生成一个token令牌（临时且唯一）存放在redis中作为rediskey value userid
        TransactionStatus transactionStatus = null;
        try {
            // 4.获取userid
            Long userId = userDo.getUserId();
            // 5.根据userId+loginType 查询当前登陆类型账号之前是否有登陆过，如果登陆过 清除之前redistoken
            UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
            transactionStatus = redisDataSoureceTransaction.begin();
            UserTokenDo userToken = new UserTokenDo();
            userToken.setUserId(userId);
            userToken.setLoginType(userLoginInpDTO.getLoginType());
            String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + loginType;
            String newToken = generateToken.createToken(keyPrefix, String.valueOf(userId));
            if (userTokenDo != null) {
                // 如果登陆过 清除之前redistoken
                String token = userTokenDo.getToken();
                // 如果开启redis事务的话，删除的时候 方法会返回false
                Boolean removeToken = generateToken.removeToken(token);
                int updateToken = userTokenMapper.updateTokenByUserIdAndLoginType(userId, loginType, newToken);
                if (!toDaoResult(updateToken)) {
                    return setResultError("系统错误!");
                }
            } else {
                // .生成对应用户令牌存放在redis中
                // 1.插入新的token
                userToken.setToken(newToken);
                userToken.setDeviceInfor(deviceInfor);
                int insertResult = userTokenMapper.insertUserToken(userToken);
                if (!toDaoResult(insertResult)) {
                    redisDataSoureceTransaction.rollback(transactionStatus);
                    return setResultError("系统错误!");
                }
            }
            // 如果有传递openid参数，修改到数据中
            String qqOpenId = userLoginInpDTO.getQqOpenId();
            if (!StringUtils.isEmpty(qqOpenId)) {
                userMapper.updateUserOpenId(qqOpenId, userId);
            }
            JSONObject data = new JSONObject();
            data.put("token", newToken);
            redisDataSoureceTransaction.commit(transactionStatus);
            return setResultSuccess(data);
        } catch (Exception e) {
            try {
                redisDataSoureceTransaction.rollback(transactionStatus);
            } catch (Exception e2) {
                //log.error("redisDataSoureceTransaction rollback error", e);
            }
            log.error("error", e);
            return setResultError("系统错误!");
        }

    }

//    public BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO) {
//        // 1.验证参数
//        String mobile = userLoginInpDTO.getMobile();
//        if (StringUtils.isEmpty(mobile)) {
//            return setResultError("手机号码不能为空!");
//        }
//        String password = userLoginInpDTO.getPassword();
//        if (StringUtils.isEmpty(password)) {
//            return setResultError("密码不能为空!");
//        }
//        // 判断登陆类型
//        String loginType = userLoginInpDTO.getLoginType();
//        if (StringUtils.isEmpty(loginType)) {
//            return setResultError("登陆类型不能为空!");
//        }
//        // 目的是限制范围
//        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType
//                .equals(Constants.MEMBER_LOGIN_TYPE_IOS)
//                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
//            return setResultError("登陆类型出现错误!");
//        }
//
//        // 设备信息
//        String deviceInfor = userLoginInpDTO.getDeviceInfor();
//        if (StringUtils.isEmpty(deviceInfor)) {
//            return setResultError("设备信息不能为空!");
//        }
//
//        // 2.对登陆密码实现加密
//        String newPassWord = MD5Util.MD5(password);
//        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
//        UserDo userDo = userMapper.login(mobile, newPassWord);
//        if (userDo == null) {
//            return setResultError("用户名称或者密码错误!");
//        }
//        // 用户登陆Token Session 区别
//        // 用户每一个端登陆成功之后，会对应生成一个token令牌（临时且唯一）存放在redis中作为rediskey value userid
//        TransactionStatus transactionStatus = null;
//        try {
//            // 4.获取userid
//            Long userId = userDo.getUserId();
//            // 5.根据userId+loginType 查询当前登陆类型账号之前是否有登陆过，如果登陆过 清除之前redistoken
//            UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
//            transactionStatus = redisDataSoureceTransaction.begin();
//            if (userTokenDo != null) {
//                // 如果登陆过 清除之前redistoken
//                String token = userTokenDo.getToken();
//                // 如果开启redis事务的话，删除的时候 方法会返回false
//                Boolean removeToken = generateToken.removeToken(token);
//                // 把该token的状态改为1
//                int updateTokenAvailability = userTokenMapper.updateTokenAvailability(token);
//                if (!toDaoResult(updateTokenAvailability)) {
//                    return setResultError("系统错误!");
//                }
//
//            }
//
//            // .生成对应用户令牌存放在redis中
//
//            // 1.插入新的token
//            UserTokenDo userToken = new UserTokenDo();
//            userToken.setUserId(userId);
//            userToken.setLoginType(userLoginInpDTO.getLoginType());
//
//            String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + loginType;
//            String newToken = generateToken.createToken(keyPrefix, String.valueOf(userId));
//
//            userToken.setToken(newToken);
//            userToken.setDeviceInfor(deviceInfor);
//            int insertUserToken = userTokenMapper.insertUserToken(userToken);
//            if (!toDaoResult(insertUserToken)) {
//                redisDataSoureceTransaction.rollback(transactionStatus);
//                return setResultError("系统错误!");
//            }
//            JSONObject data = new JSONObject();
//            data.put("token", newToken);
//            redisDataSoureceTransaction.commit(transactionStatus);
//            return setResultSuccess(data);
//        } catch (Exception e) {
//            try {
//                redisDataSoureceTransaction.rollback(transactionStatus);
//            } catch (Exception e2) {
//                //log.error("redisDataSoureceTransaction rollback error", e);
//            }
//            log.error("error", e);
//            return setResultError("系统错误!");
//        }
//
//    }
    // 查询用户信息的话如何实现？ redis 与数据库如何保证一致问题

    // redis 的值如何与数据库的值保持是一致性问题
    // @Transactional 不能控制redis的事务
    // redis 中是否存在事务 肯定是肯定是存在事务
    // 自定义方法 使用编程事务 begin（既需要控制数据库的事务也需要控制redis） commit

}
