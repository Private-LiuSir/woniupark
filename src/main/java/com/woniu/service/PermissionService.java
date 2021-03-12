package com.woniu.service;

import com.woniu.model.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface PermissionService extends IService<Permission> {

    public List<Permission> getPermissionInfo(Integer uid);

}
