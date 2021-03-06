package com.woniu.mapper;

import com.woniu.model.Role;
import com.woniu.model.Permission;
import com.woniu.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
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
public interface UserMapper extends BaseMapper<User> {
    @Insert("insert into t_user_role values (null,#{uid},#{rid})")
    //普通用户添加角色
    void role(int uid,int rid);
    //查询角色
    @Select("SELECT r.role_id " +
            "            from t_user u " +
            "            join t_user_role ur " +
            "            on u.user_id=ur.u_id " +
            "            join t_role r " +
            "            on r.role_id=ur.r_id " +
            "            WHERE u.user_id=#{uid} ;")
    Role getRole(int uid);

    //根据id查询小区的ID
    @Select("SELECT pu.plot_id " +
            "from t_user as u " +
            "join t_plot_user as pu " +
            "on u.user_id=pu.user_id " +
            "WHERE u.user_id=#{uid}")
    public int getPlotByUserId(Integer uid);


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
            "where u.id=#{uid}")
    public List<Permission> findAllPermission(Integer uid);




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
            "where u.username=#{username}\n" +
            "and p.pid=#{pid}")
    public List<Permission> getPermissions(String username, Integer pid);
}
