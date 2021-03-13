package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.woniu.mapper.UserMapper;
import com.woniu.model.Order;
import com.woniu.service.OrderService;
import com.woniu.util.Result;
import com.woniu.vo.OrderVo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import com.woniu.service.OrderService;
import com.woniu.util.Result;
import com.woniu.vo.OrderVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Resource
    private UserMapper userMapper;




    @RequiresRoles(value ="普通用户",logical = Logical.OR)
    @RequestMapping("/createOrder")
    public Result createOrder(@RequestBody OrderVo orderVo){
        System.out.println("前端传入的参数："+orderVo);
        int order = orderService.createOrder(orderVo);
        if(order==0){
            return new Result("该车位已被抢租",false);
        }
        if(order==1){
            return new Result("下单成功，请前往停放车辆",true);
        }
        return new Result("网络繁忙，请刷新后再操作",false);
    }

    //跟据订单ID获取订单的详情信息  包括 小区的地址  小区名 小区联系方式  用户昵称 下单时间  等
    @RequestMapping("/orderInfo")
    public Result getOrderInfo(@RequestBody OrderVo orderVo){
        OrderVo orderInfo = orderService.getOrderInfo(orderVo.getOrderId());
       //将集合传回前端  因为前端使用表格展示  所有需要集合装数据
        ArrayList<OrderVo> orderVos = new ArrayList<>();
        orderVos.add(orderInfo);
        Date createTime = orderInfo.getCreateTime();
        System.out.println(createTime);
        return new Result(orderVos);
    }

    //根据条件获取订单列表  可以通过判别传入的参数查询对应订单集合
    @RequestMapping("/orderList")
    public Result getOrderList(@RequestBody OrderVo orderVo){
        //创建条件构造器对象
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        //判断请求的是否为用户
        if(!ObjectUtils.isEmpty(orderVo.getUserId())){
            //用户ID不为null  添加查询条件
            queryWrapper.eq("user_id",orderVo.getUserId());
        }
        //判断请求参数是否有出租客ID
        if(!ObjectUtils.isEmpty(orderVo.getLetterId())){
            //出租客ID不为空  添加查询条件
            queryWrapper.eq("letter_id",orderVo.getLetterId());
        }
        //判断请求参数是否有小区ID
        if (!ObjectUtils.isEmpty(orderVo.getPlotId())){
            //小区ID不为空  添加查询条件
            //根据小区用户ID查询关联的小区ID
            int plotByUserId = userMapper.getPlotByUserId(orderVo.getPlotId());
            //将查询到的小区ID设置进去
            queryWrapper.eq("plot_id",plotByUserId);
        }
        //判断请求参数是否有小区ID
        if (!ObjectUtils.isEmpty(orderVo.getStallId())){
            //小区ID不为空  添加查询条件
            queryWrapper.eq("stall_id",orderVo.getStallId());
        }
        //判断是否需要添加字段
        if(!ObjectUtils.isEmpty(orderVo.getStatus())){
            //小区ID不为空  添加查询条件
            queryWrapper.eq("status",orderVo.getStatus());
        }
        //平台方获取时不需要参数  查询全部  可以后续添加定位查询功能

        //根据条件查询订单
        List<Order> list = orderService.list(queryWrapper);
        //将查询的订单信息返回给前端
        return new Result(list);
    }

    //结单的方法  要求传用户用户ID  订单ID  下单时间  单价   结单时间由系统生成
    @RequiresRoles(value ="普通用户",logical = Logical.OR)
    @RequestMapping("/statement")
    public Result statement(@RequestBody OrderVo orderVo){
        //调用结单的业务层方法
        BigDecimal statement = orderService.statement(orderVo.getOrderId());
        //判断是否为null  null表示余额不足  操作失败  需要提醒用户充值
        if (ObjectUtils.isEmpty(statement)) {
            return new Result("余额不足，请前往充值",false);
        }
        double v = statement.doubleValue();
        return new Result("结单成功！",true,20000,v);
    }

    //评价订单的接口   要求传入订单ID  订单的满意度
    @RequiresRoles(value ="普通用户",logical = Logical.OR)
    @RequestMapping("/rate")
    public Result rateOrder(@RequestBody OrderVo orderVo){
        //创建对象
        Order order = new Order();
        order.setOrderId(orderVo.getOrderId());
        order.setRate(orderVo.getRate());
        //更新字段
        order.setStatus(3);
        orderService.updateById(order);
       return new Result<>("评分成功");
    }

}

