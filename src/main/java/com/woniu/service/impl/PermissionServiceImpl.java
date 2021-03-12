package com.woniu.service.impl;

import com.woniu.model.Permission;
import com.woniu.mapper.PermissionMapper;
import com.woniu.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public Set<Permission> findAllPermissions() {
        List<Permission> permissions = permissionMapper.selectList(null);
        Set<Permission> needPermissions=new LinkedHashSet<>();

        permissions.forEach(first -> {
            if(first.getLevel()==1){
                first.setPermission(new LinkedHashSet<Permission>());
                needPermissions.add(first);
            }
        });

        permissions.forEach(second->{
            needPermissions.forEach(first->{
                if(second.getPId()==first.getPermissionId()){
                    second.setPermission(new LinkedHashSet<Permission>());
                    first.getPermission().add(second);
                }
            });
        });

        permissions.forEach(third->{
            if(third.getLevel()==3) {
                needPermissions.forEach(second -> {
                    second.getPermission().forEach(root -> {
                        if (third.getPId() == root.getPermissionId()) {
                            root.getPermission().add(third);
                        }
                    });
                });
            }
        });
        return needPermissions;
    }

    @Override
    public Set<Permission> queryPermissionByRid(Integer rid) {
        Set<Permission> needPermissions=new LinkedHashSet<>();
        Set<Permission> permissions = permissionMapper.queryPermissionByRid(rid);
        permissions.forEach(permission -> {
            if (permission.getLevel()==3){
                needPermissions.add(permission);
            }
        });

        return needPermissions;
    }

    @Override
    public void deletePermissionAndRoleByRid(Integer rid) {
        permissionMapper.deletePermissionAndRoleByRid(rid);
    }

    @Override
    public void insertRoleAndPermission(Integer rid, Integer pid) {
        permissionMapper.insertRoleAndPermission(rid,pid);
    }
}
