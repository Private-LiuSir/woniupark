package com.woniu.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

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
@TableName("t_permission")
@ApiModel(value="Permission对象", description="")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限ID")
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;

    @ApiModelProperty(value = "权限菜单名")
    private String element;

    @ApiModelProperty(value = "访问路径")
    private String url;

    @ApiModelProperty(value = "菜单等级")
    private Integer level;

    @ApiModelProperty(value = "父级菜单ID")
    private Integer pId;

    //表示该字段数据库不存在
    @TableField(exist = false)
    private List<Permission> permission;




}
