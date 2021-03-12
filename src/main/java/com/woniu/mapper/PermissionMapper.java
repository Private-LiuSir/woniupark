package com.woniu.mapper;

import com.woniu.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


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


    @Select("select p.*\n" +
            "FROM t_role_permission rp\n" +
            "join t_permission p\n" +
            "on p.id=rp.pid\n" +
            "where rp.rid=#{rid}")
    public Set<Permission> queryPermissionByRid(Integer rid);

    @Select("DELETE from t_role_permission\n" +
            " where rid=#{rid}")
    public Integer deletePermissionAndRoleByRid(Integer rid);

    @Select("insert into t_role_permission(rid,pid) VALUES(#{rid},#{pid})")
    public Integer insertRoleAndPermission(@RequestParam Integer rid, @RequestParam Integer pid);


    @Select("SELECT p.*\n" +
            "from t_user u\n" +
            "join t_user_role ur\n" +
            "on u.id=ur.uid\n" +
            "join t_role r\n" +
            "on r.role_id=ur.rid\n" +
            "join t_role_permission rp\n" +
            "on rp.rid=r.role_id\n" +
            "join t_permission p\n" +
            "on p.id=rp.pid\n" +
            "where u.username=#{username}")
    public List<Permission> getPermissions(String username);


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
