package com.woniu.service;

import com.woniu.model.Permission;
import com.woniu.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface UserService extends IService<User> {

    public Set<Permission> findAllPermission(Integer uid);

    public List<Permission> getPermissions(String username, Integer pid);

}
