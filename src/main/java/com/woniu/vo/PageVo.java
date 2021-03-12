package com.woniu.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageVo {
    private Integer current;
    private Integer size;
}
