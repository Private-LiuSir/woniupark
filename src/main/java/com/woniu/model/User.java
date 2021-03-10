package com.woniu.model;

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
@TableName("t_user")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键，并自增")
        @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId=1;

        @ApiModelProperty(value = "昵称，注册时需输入")
        private String nickname;

        @ApiModelProperty(value = "需要对密码加密，不能明文")
        private String password;

        @ApiModelProperty(value = "对密码加盐")
        private String salt;

        @ApiModelProperty(value = "电话号码不能为null，注册需要使用电话号码")
        private String tel;

        @ApiModelProperty(value = "判断用户是否审核成功（默认为0）")
        private Integer status;

        @ApiModelProperty(value = "创建时间")
        @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

        @ApiModelProperty(value = "修改时间")
        private Date gmtModifified;

        @ApiModelProperty(value = "版本号")
        @Version
    private Integer version;


}
