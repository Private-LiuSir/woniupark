package com.woniu.controller;


import com.woniu.service.OrderService;
import com.woniu.util.Result;

import com.woniu.vo.OrderVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;


    @RequestMapping("/createOrder")
    public Result createOrder(@RequestBody OrderVo orderVo){
        System.out.println("前端传入的参数："+orderVo);
        int order = orderService.createOrder(orderVo);
        if(order==0){
            return new Result("该车位已被抢租");
        }
        if(order==1){
            return new Result("下单成功，请前往停放车辆");
        }
        return new Result("网络繁忙，请刷新后再操作");
    }





}

