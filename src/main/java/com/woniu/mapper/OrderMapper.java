package com.woniu.mapper;

import com.woniu.model.Order;
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
public interface OrderMapper extends BaseMapper<Order> {

    //根据用户id查询角色
    @Select("SELECT o.*\n" +
            "from t_order o\n" +
            "join t_user u\n" +
            "on o.user_id=u.user_id\n" +
            "WHERE u.user_id=#{id}")
    public List<Order> findRolesByUId(Integer id);

}
