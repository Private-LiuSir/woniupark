package com.woniu.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AlipayVO {
    private String outTradeNo;
    private String subjects;
    private String totalAmount;
    private String  bodys;

}
