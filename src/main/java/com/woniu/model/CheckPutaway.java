package com.woniu.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_check_putaway")
@ApiModel(value="CheckPutaway对象", description="")
public class CheckPutaway implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "check_putaway_id", type = IdType.AUTO)
    private Integer checkPutawayId;

    private Integer userId;

    private Integer plotId;

    private Integer stallId;

    private Double unitPrice;

    private Integer shelfTime;

    private Date upstallTime;

    private Date checkTime;

    private Integer status;

    private String cause;


}
