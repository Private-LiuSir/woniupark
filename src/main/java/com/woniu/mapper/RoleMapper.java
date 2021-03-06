package com.woniu.mapper;

import com.woniu.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface RoleMapper extends BaseMapper<Role> {
        List<Role> getRolesByTel(String tel);
}
