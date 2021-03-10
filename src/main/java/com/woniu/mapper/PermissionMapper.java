package com.woniu.mapper;

import com.woniu.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    //根据角色id查询权限
    @Select("select p.*\n" +
            "FROM t_role_permission rp\n" +
            "join t_permission p\n" +
            "on p.permission_id=rp.p_id\n" +
            "where rp.role_id=#{rid}")
    public Set<Permission> queryPermissionByRid(Integer rid);

    //根据用昵称查询权限
    @Select("SELECT p.*\n" +
            "from t_user u\n" +
            "join t_user_role ur\n" +
            "on u.user_id=ur.u_id\n" +
            "join t_role r\n" +
            "on r.role_id=ur.r_id\n" +
            "join t_role_permission rp\n" +
            "on rp.r_id=r.role_id\n" +
            "join t_permission p\n" +
            "on p.permission_id=rp.p_id\n" +
            "where u.nickname=#{username}")
    public List<Permission> getPermissions(String username);
}
