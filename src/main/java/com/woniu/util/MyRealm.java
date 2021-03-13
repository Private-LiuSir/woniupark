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
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;

public class MyRealm extends AuthorizingRealm {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        //解析出用户id信息
        String uid = JWTutil.vertify(token).getClaim("uid").asString();
        //从先判断redis中有无该用户的角色列表
        Boolean aBoolean = stringRedisTemplate.hasKey("woniupark:roleList:" + uid);
        //redis中有该用户的角色列表
        if(aBoolean){
            System.out.println("从redis获取角色列表");
            List<String> roleName = stringRedisTemplate.opsForList().range("woniupark:roleList:" + uid, 0, -1);
            //往令牌存数据
            roleName.forEach(name->{
                info.addRole(name);
            });
        }else{
            //从数据库获取用户角色列表
            System.out.println("从数据库获取角色列表");
            List<Role> roles = roleMapper.findRolesByUId(Integer.valueOf(uid));
            //遍历  往redis数据库存
            roles.forEach(role ->{
                //往身份令牌存
                info.addRole(role.getRoleName());
                //往redis存
                stringRedisTemplate.opsForList().leftPush("woniupark:roleList:" + uid,role.getRoleName());
            });

        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户登录的电话
        String token = (String)authenticationToken.getPrincipal();
        //获取解密后的对象
        DecodedJWT vertify = JWTutil.vertify(token);
        //获取用户名
        String uid = vertify.getClaim("uid").asString();
        System.out.println("用户ID："+uid);
        if(StringUtils.hasLength(uid)){
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
