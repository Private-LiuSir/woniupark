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
@TableName("t_plot")
@ApiModel(value="Plot对象", description="")
public class Plot implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区编号ID")
    @TableId(value = "plot_id", type = IdType.AUTO)
    private Integer plotId;

    @ApiModelProperty(value = "小区地址")
    private String plotAddress;

    @ApiModelProperty(value = "小区名字")
    private String plotName;


}
