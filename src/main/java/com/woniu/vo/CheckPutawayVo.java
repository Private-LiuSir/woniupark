package com.woniu.vo;

import lombok.Data;

@Data
public class CheckPutawayVo {
    //用户id
    private Integer userId;
    //小区id
    private Integer plotId;
    //车位id
    private Integer stallId;
    //单价
    private Double unitPrice;
    //上架时长
    private Integer shelfTime;
    //上架时间
    private String upstallTime;
    //审核时间
    private String checkTime;
    //状态
    private Integer Status;
    //小区地址
    private String plotAddress;
    //小区名
    private String plotName;
    //出租客id
    private Integer letterId;
    //审核建议
    private String cause;
}
