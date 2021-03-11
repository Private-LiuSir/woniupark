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
    //下单用户的联系方式
    private String userTel;
    //用户名
    private String userName;
    //车位所在小区ID
    private Integer plotId;
    //小区名
    private String plotName;
    //小区物业联系方式
    private String plotTel;
    //车位ID
    private Integer stallId;
    //车位号
    private Integer parkingLotNo;
    //车位主人ID
    private Integer letterId;
    //出租客昵称
    private String letterTel;
    //出租客昵称
    private String letterName;
    //订单创建时间
    private Date createTime;
    //订单结算时间
    private Date statementTime;
    //订单金额
    private Double money;
    //订单金额
    private Double price;
    //订单状态
    private Integer status;
    //地址信息
    private String address;
    //车位户主昵称  车主联系方式   下单时间  订单价格total   单价money
    private Integer rate;
}
