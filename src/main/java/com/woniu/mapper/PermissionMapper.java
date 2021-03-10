package com.woniu.mapper;

import com.woniu.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    //根据ID获取权限列表
    @Select("SELECT p.*  " +
            "FROM t_user AS u " +
            "Join t_user_role AS tr " +
            "ON u.user_id = tr.u_id " +
            "JOIN t_role AS r " +
            "ON tr.r_id=r.role_id " +
            "join t_role_permission AS rp " +
            "on r.role_id=rp.r_id " +
            "join t_permission AS p " +
            "on rp.p_id=p.permission_id " +
            "WHERE u.user_id=#{uid}")
    public List<Permission> getPermissionByUid(Integer uid);

}
