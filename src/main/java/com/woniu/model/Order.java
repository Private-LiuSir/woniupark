package com.woniu.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order")
@ApiModel(value="Order对象", description="订单的相关信息")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty(value = "userid，便与关联")
    private Integer userId;

    @ApiModelProperty(value = "小区ID")
    private Integer plotId;

    @ApiModelProperty(value = "车位ID")
    private Integer stallId;

    @ApiModelProperty(value = "出租客ID")
    private Integer letterId;

    @ApiModelProperty(value = "订单的金额")
    private BigDecimal money;

    @ApiModelProperty(value = "订单的单价")
    private BigDecimal price;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "订单结算时间")
    private Date statementTime;
    @ApiModelProperty(value = "修改时间")
    private Date gmtModifified;

    @ApiModelProperty(value = "版本号")
    @Version
    private Integer version;

    @ApiModelProperty(value = "订单状态：1、进行中 2、待评价 3、已完成")
    private Integer status;

}
