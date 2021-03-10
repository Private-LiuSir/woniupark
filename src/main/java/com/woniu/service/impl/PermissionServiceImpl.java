package com.woniu.service.impl;

import com.woniu.model.Permission;
import com.woniu.mapper.PermissionMapper;
import com.woniu.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionInfo(Integer uid) {

        List<Permission> permission = permissionMapper.getPermissionByUid(uid);
        //创建集合保存1级权限列表
        ArrayList<Permission> fPermission = new ArrayList<>();
        //遍历权限权限菜单
        permission.forEach(p->{
            //判断是否为1级目录  是就为器创建子级列表
            if(p.getPId()==0){
                fPermission.add(p);
                ArrayList<Permission> permissions = new ArrayList<>();
                //创建一个集合用于保存子级菜单
                p.setPermission(permissions);
                //往新增集合中添加子级
                permission.forEach(p2->{
                    if(p2.getPId()==p.getPermissionId()){
                        permissions.add(p2);
                    }
                });
            }
        });
        System.out.println(fPermission);
        return fPermission;
    }
}
