package com.woniu.mapper;

import com.woniu.model.Permission;
import com.woniu.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

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
public interface RoleMapper extends BaseMapper<Role> {
        @Select("SELECT r.*\n" +
                "from t_user u\n" +
                "join t_user_role ur\n" +
                "on u.id=ur.uid\n" +
                "join t_role r\n" +
                "on r.role_id=ur.rid\n" +
                "WHERE u.id=#{id}")
        public List<Role> findRolesByUId(Integer id);

        public List<Role> findRolesByTel(String tel);

        @Select("DELETE \n" +
                "from t_user_role \n" +
                "where uid=#{uid}")
        public Integer deleteRoleAndUserByUid(Integer uid);

        @Select("insert into t_user_role(uid,rid) VALUES(#{uid},#{rid})")
        public Integer insertRoleAndUser(@RequestParam Integer uid, @RequestParam Integer rid);


        @Select("select p.*\n" +
                "from t_role_permission rp\n" +
                "join t_permission p\n" +
                "on p.id=rp.pid\n" +
                "where rp.rid=#{rid}\n")
        public Set<Permission> findPermissions(Integer rid);

        @Select("SELECT r.*\n" +
                "from t_user u\n" +
                "join t_user_role ur\n" +
                "on u.id=ur.uid\n" +
                "join t_role r\n" +
                "on r.role_id=ur.rid\n" +
                "WHERE u.username=#{username}")
        public List<Role> getRole(String username);

}
