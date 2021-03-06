package com.woniu.util;

import com.auth0.jwt.interfaces.DecodedJWT;

import com.woniu.component.JwtToken;
import com.woniu.mapper.PermissionMapper;
import com.woniu.mapper.RoleMapper;
import com.woniu.mapper.UserMapper;
import com.woniu.model.Role;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

public class MyRealm extends AuthorizingRealm {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private UserMapper userMapper;


    //表明支持的是自定义token
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("开始鉴定权限");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取令牌
        String token = (String)principalCollection.getPrimaryPrincipal();
        //解析出用户电话信息
        String tel = JWTutil.vertify(token).getClaim("tel").asString();
        //添加用户角色信息
        List<Role> roles = roleMapper.getRolesByTel(tel);
        System.out.println(roles);
        roles.forEach(role ->{
            info.addRole(role.getRoleName());
        });
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户登录的电话
        String token = (String)authenticationToken.getPrincipal();
        //获取解密后的对象
        DecodedJWT vertify = JWTutil.vertify(token);
        //获取用户名
        String tel = vertify.getClaim("tel").asString();
        System.out.println("电话："+tel);
        if(StringUtils.hasLength(tel)){
            return new SimpleAuthenticationInfo(
                    token,
                    token,
                    this.getName()
            );
        }else{
            throw new AuthenticationException("用户未登录");
        }
    }
}
