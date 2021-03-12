package com.woniu.service;

import com.woniu.model.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface PermissionService extends IService<Permission> {

    public Set<Permission> findAllPermissions();

    public Set<Permission> queryPermissionByRid(Integer rid);

    public void deletePermissionAndRoleByRid(Integer rid);

    public void insertRoleAndPermission(Integer rid, Integer pid);

}
