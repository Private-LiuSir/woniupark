package com.woniu.service;

import com.woniu.model.Role;
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
public interface RoleService extends IService<Role> {
    public List<Role> findRolesByUId(Integer id);

//    public boolean deleteRoleAndUserByUid(Integer uid);
//
//    public boolean insertRoleAndUser(Integer uid, Integer rid);
}
