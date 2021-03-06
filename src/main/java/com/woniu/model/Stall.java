package com.woniu.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2021-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_stall")
@ApiModel(value="Stall对象", description="")
public class Stall implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车位标号ID")
    @TableId(value = "stall_id", type = IdType.AUTO)
    private Integer stallId;

    @ApiModelProperty(value = "租客ID")
    private Integer userId;

    @ApiModelProperty(value = "所属小区ID")
    private Integer plotId;

    @ApiModelProperty(value = "车位地址信息")
    private String address;

    @ApiModelProperty(value = "车位产权编号")
    private Integer propertyNum;

    @ApiModelProperty(value = "车位号")
    private Integer parkingLotNo;

    @ApiModelProperty(value = "车位状态")
    private Integer status;

    @ApiModelProperty(value = "上架次数")
    private Integer number;


}
