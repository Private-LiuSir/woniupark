package com.woniu.mapper;

import com.woniu.model.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniu.vo.OrderVo;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface OrderMapper extends BaseMapper<Order> {

    //根据订单ID获取ordervo的其他字段的方法
    //前台需求字段
    @Select("SELECT  " +
            "s.address as address, " +
            "o.gmt_create as createTime, " +
            "pl.plot_name AS plotName, " +
            "p.tel as plotTel, " +
            "l.tel AS letterTel, " +
            "s.parking_lot_no AS parkingLotNo " +
            "FROM t_order as o " +
            "JOIN t_stall as s " +
            "ON o.stall_id = s.stall_id " +
            "JOIN t_user as l " +
            "on o.letter_id=l.user_id " +
            "JOIN t_user as p " +
            "on o.plot_id=p.user_id " +
            "JOIN t_plot AS pl " +
            "ON o.plot_id=pl.plot_id " +
            "where  o.order_id=#{orderId}")
    public OrderVo selectInfoByOrderId(Integer orderId);


}
