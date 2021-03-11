package com.woniu.vo;

import lombok.Data;

@Data
public class checkVo {
    //所在小区
    private String plotName;
    //详细地址
    private String address;
    //车位号
    private Integer parkingLotNo;
    //产权编号
    private Integer propertyNum;
    //出租方id
    private Integer letterId;
    //审核时间
    private String checkTime;
    //审核建议
    private String cause;
    //审核状态
    private Integer status;

}
