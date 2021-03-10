package com.woniu.service;

import com.woniu.model.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woniu.vo.OrderVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface OrderService extends IService<Order> {

    //创建订单
    public int createOrder(OrderVo orderVo);


}
