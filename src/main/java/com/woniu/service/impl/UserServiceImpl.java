package com.woniu.service.impl;

import com.woniu.model.Permission;
import com.woniu.model.User;
import com.woniu.mapper.UserMapper;
import com.woniu.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    private UserMapper userMapper;

    @Override
    public Set<Permission> findAllPermission(Integer uid) {
        List<Permission> allPermissions = userMapper.findAllPermission(uid);

        Set<Permission> userSinglePermission=new LinkedHashSet<>();

        allPermissions.forEach(first->{
            if(first.getLevel()==1){
                first.setPermission(new LinkedHashSet<Permission>());
                userSinglePermission.add(first);
            }
        });

        allPermissions.forEach(second->{
            userSinglePermission.forEach(first->{
                if(second.getPId()==first.getPermissionId()){
                    first.getPermission().add(second);
                }
            });
        });
        System.out.println("userSinglePermission====>>>>/n"+userSinglePermission);

        return userSinglePermission;
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

