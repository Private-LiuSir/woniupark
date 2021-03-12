package com.woniu.service.impl;

import com.woniu.model.Role;
import com.woniu.mapper.RoleMapper;
import com.woniu.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRolesByUId(Integer id) {
        return roleMapper.findRolesByUId(id);
    }

    @Override
    public boolean deleteRoleAndUserByUid(Integer uid) {
        roleMapper.deleteRoleAndUserByUid(uid);
        return true;

    }

    @Override
    public boolean insertRoleAndUser(Integer uid, Integer rid) {
        roleMapper.insertRoleAndUser(uid, rid);
        return true;
    }
}
