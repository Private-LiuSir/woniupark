package com.woniu.service;

import com.woniu.model.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woniu.vo.OrderVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface OrderService extends IService<Order> {

    public int createOrder(OrderVo orderVo);
}
