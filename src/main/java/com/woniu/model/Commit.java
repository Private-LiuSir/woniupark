package com.woniu.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("t_commit")
@ApiModel(value="Commit对象", description="")
public class Commit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "commit_id", type = IdType.AUTO)
    private Integer commitId;

    private Integer userId;

    private String commitContent;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    private Date gmtModifified;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
