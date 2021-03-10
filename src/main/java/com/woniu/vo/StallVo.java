package com.woniu.vo;

import lombok.Data;

import java.util.Date;

@Data
public class StallVo {
    //地址address  车位号 stall_id  小区名字plot_name 单价price  上架时间shelf_time   自动下架时间()

    //车位地址
    private String address;
    //车位ID
    private Integer stallId;
    //小区名
    private String plotName;
    //小区ID
    private Integer plotId;
    //出租客ID
    private Integer letterId;
    //车位号
    private Integer parkingLotNo;
    //上架时长，即出租客上架选择出租的时间  前台转换为小时
    private Integer shelfTime;
    //上架的时间
    private String upstallTime;
    //剩余可操作租用时间   该属性只在上架后存在
    private Integer leaveTime;
    //车位状态  上架后车位保存的值   1、上架(可进行抢租)   2、出租中(无法进行抢租)   3、未上架  4、
    private Integer stallStatus;
    //地址
    private String plotAddress;

    //单价   需要做精准处理BigDecimal
    private Double price;

    //车位状态(1为待审核，2审核通过，3审核未通过)
    private Integer status;
    //产权编号
    private Integer propertyNum;
    //date类型上架时间
    private Date date;

}