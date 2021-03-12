package com.woniu.service.impl;

import com.woniu.model.Permission;
import com.woniu.HttpUtils.IdentityAuthentication;
import com.woniu.model.Role;
import com.woniu.model.User;
import com.woniu.mapper.UserMapper;
import com.woniu.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.util.SaltUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RedisTemplate<Object,Object> redisTemplate;
    @Resource
    private UserMapper userMapper;
    @Override
    public void role(int uid, int rid) {
        userMapper.role(uid,rid);
    }
//存推荐码到redis里面
    @Override
    public String verificationCode(int uid) {
        RedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        //随机生成推荐码
        String salt = SaltUtils.getSalt(6);
        System.out.println(salt);
        redisTemplate.opsForValue().set(salt,uid+"");
        //设置有效时间
        redisTemplate.opsForValue().set(salt,uid,1800, TimeUnit.SECONDS);
        return salt;
    }
    //从Redis里面取推荐码数据 根据推荐码取用户id
    @Override
    public int getVerificationCode(String code) {
        Object uid = redisTemplate.opsForValue().get(code);
        if (uid==null){
            return 0;
        }else {
            int userid = Integer.parseInt(uid.toString());
            return userid;
        }


    }

    @Override
    public boolean authentication( String path) {
        RedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
//        //把用户id和存储路径存入redis里面
//        redisTemplate.opsForValue().set(id+"",file);
//        redisTemplate.opsForValue().set(id+"",file,300, TimeUnit.SECONDS);
        //调用身份认证接口
        Boolean aBoolean = IdentityAuthentication.identityCard(path);
        return aBoolean;
    }
    //查询角色
    @Override
    public Role getRole(int uid) {
        Role role = userMapper.getRole(uid);
        return role;
    }
    //验证码存redis
    @Override
    public void noteRedis(String tel, String authCode) {
        RedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        //电话为k 验证码为
        redisTemplate.opsForValue().set(tel,authCode);
        //设置验证码有效时间3分钟
        redisTemplate.opsForValue().set(tel,authCode,180, TimeUnit.SECONDS);
    }

    //获取redis里面的验证码
    @Override
    public String gitNoteRedis(String tel) {
        Object uid = redisTemplate.opsForValue().get(tel);
        System.out.println(uid+"*********************8");
        if (ObjectUtils.isEmpty(uid)){
            //如果没取到验证码 返回1 代表已过期或者没有点击验证码按钮
            return "1";
        }else {
            //取到返回验证码
            return uid.toString();
        }

    }






    @Override
    public List<Permission> getPermissions(String username, Integer pid) {
        List<Permission> permissions = userMapper.getPermissions(username,pid);
        ArrayList<Permission> needPermission=new ArrayList<Permission>();
        permissions.forEach(permission -> {
            if(permission.getLevel()==3){
                needPermission.add(permission);
            }
        });
        return needPermission;
    }
    }

