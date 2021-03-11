package com.woniu.model;

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
@TableName("t_check_stall")
@ApiModel(value="CheckStall对象", description="")
public class CheckStall implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "check_stall_id", type = IdType.AUTO)
    private Integer checkStallId;

    private Integer userId;

    private String plotName;

    private String address;

    private Integer propertyNum;

    private Integer parkingLotNo;

    @ApiModelProperty(value = "1:待审核2.审核成功3.审核失败")
    private Integer status;

    @ApiModelProperty(value = "审核建议")
    private String cause;

    private Date insertTime;

    private Date checkTime;


}
