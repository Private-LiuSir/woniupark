package com.woniu.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.woniu.model.UserInfo;
import com.woniu.service.UserInfoService;
import com.woniu.util.JWTutil;
import com.woniu.util.Result;
import com.woniu.vo.PageVo;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/userinfo")
@CrossOrigin
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @GetMapping("page")
    public Result findpage(PageVo pageVo){

        Page<UserInfo> userInfoPage=new Page<>(pageVo.getCurrent(),pageVo.getSize());
        IPage<UserInfo> page = userInfoService.page(userInfoPage, null);

        return new Result(page);
    }

    @DeleteMapping("delete/{uid}")
    public Result deleteUserInfo(@PathVariable Integer uid){
        userInfoService.removeById(uid);


        return new Result(true);
    }

    @RequestMapping("/get")
    public Result getInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        DecodedJWT vertify = JWTutil.vertify(token);
        //获取用户ID
        String uid = vertify.getClaim("uid").asString();

        Integer integer = Integer.valueOf(uid);

        UserInfo byId = userInfoService.getById(integer);
        return new Result(byId);
    }

}

