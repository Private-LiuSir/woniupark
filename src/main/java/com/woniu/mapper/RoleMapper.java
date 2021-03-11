package com.woniu.mapper;

import com.woniu.model.Role;
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
public interface RoleMapper extends BaseMapper<Role> {
        @Select("SELECT r.* " +
                "from t_user u " +
                "join t_user_role ur " +
                "on u.user_id=ur.u_id " +
                "join t_role r " +
                "on r.role_id=ur.r_id " +
                "WHERE u.user_id=#{id}")
        public List<Role> findRolesByUId(Integer id);

        @Select("SELECT r.* " +
                "from t_user u " +
                "join t_user_role ur " +
                "on u.user_id=ur.u_id " +
                "join t_role r " +
                "on r.role_id=ur.r_id " +
                "WHERE u.username=#{username}")
        public List<Role> getRole(String username);

        List<Role> getRolesByTel(String tel);
}
