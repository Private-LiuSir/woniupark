package com.woniu.vo;

import lombok.Data;

@Data
public class StallVo {
    //地址address  车位号 stall_id  小区名字plot_name 单价price  上架时间shelf_time   自动下架时间()

    //车位地址
    private String address;
    //车位ID
    private Integer stall_id;
    //小区名
    private String plot_name;
    //小区ID
    private Integer plot_Id;
    //出租客ID
    private Integer letter_Id;
    //车位号
    private Integer parking_lot_no;
    //单价   需要做精准处理BigDecimal
    private Double price;
    //上架时长，即出租客上架选择出租的时间  前台转换为小时
    private Integer shelf_time;
    //上架的时间
    private String upstall_time;
    //剩余可操作租用时间
    private Integer leave_time;
}
