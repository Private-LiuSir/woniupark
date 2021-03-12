package com.woniu.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.woniu.component.JwtToken;
import com.woniu.model.UserInfo;
import com.woniu.service.UserInfoService;
import com.woniu.util.JwtUtils;
import com.woniu.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@RestController
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    //根据用户ID 获取用户信息的方法
    @RequestMapping("userInfo")
    public Result getInfo(HttpServletRequest request){
//        String token = request.getHeader("token");
//        DecodedJWT decodeToken = JwtUtils.getDecodeToken(token);
//        //解密出用户ID
//        String uid = decodeToken.getClaim("uid").asString();
//        Integer integer = Integer.valueOf(uid);
        //获取数库中的信息
        UserInfo byId = userInfoService.getById(5);
        return new Result(byId);
    }
}

