package com.woniu.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.woniu.model.Permission;
import com.woniu.service.PermissionService;
import com.woniu.util.JWTutil;
import com.woniu.util.Result;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @RequestMapping("/permissionInfo")
    public Result getPermission(HttpServletRequest request){
        Integer uid=-1;
        //从请求中获取后台保存的JWT字符串
        String token = request.getHeader("token");
        //使用token工具类解析  取出uid
        if(StringUtils.hasLength(token)){
            DecodedJWT vertify = JWTutil.vertify(token);
            //取出ID
            uid = Integer.valueOf(vertify.getClaim("uid").asString());
        }
        List<Permission> permissionInfo = permissionService.getPermissionInfo(uid);
        return new Result(permissionInfo);
    }
}

