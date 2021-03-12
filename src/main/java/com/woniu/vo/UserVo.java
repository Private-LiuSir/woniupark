package com.woniu.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;



@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserVo {
    private Integer userId;
    private String nickname;
    private String password;
    private String salt;
    private String tel;
    private String status;
    private Date gmtCreate;
    private Date gmtModifified;
    private Integer version;
    //推荐码
        private String referral;
        //判断角色是否是租客
    private String radio;
    //验证码
    private String research;
}
