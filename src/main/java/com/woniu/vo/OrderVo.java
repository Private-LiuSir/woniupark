package com.woniu.vo;

import lombok.Data;

import java.util.Date;

//订单的自定义类
@Data
public class OrderVo {
    //订单ID
    private Integer orderId;
    //下单用户ID
    private Integer userId;
    //车位所在小区ID
    private Integer plotId;
    //车位ID
    private Integer stallId;
    //车位主人ID
    private Integer letterId;
    //订单创建时间
    private Date createTime;
    //订单结算时间
    private Date statementTime;
    //订单金额
    private Double money;
    //订单状态
    private Integer status;
}
