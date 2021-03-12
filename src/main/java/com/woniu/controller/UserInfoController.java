package com.woniu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.woniu.DTO.Result;
import com.woniu.DTO.StatusCode;
import com.woniu.model.UserInfo;
import com.woniu.service.UserInfoService;
import com.woniu.vo.PageVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/userinfo")
@CrossOrigin
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @GetMapping("page")
    public Result findpage(PageVo pageVo){

        Page<UserInfo> userInfoPage=new Page<>(pageVo.getCurrent(),pageVo.getSize());
        IPage<UserInfo> page = userInfoService.page(userInfoPage, null);

        return new Result(true, StatusCode.OK,"分页查询成功",page);
    }

    @DeleteMapping("delete/{uid}")
    public Result deleteUserInfo(@PathVariable Integer uid){
        userInfoService.removeById(uid);


        return new Result(true,StatusCode.OK,"删除物业信息成功");
    }
    @PutMapping
    public Result updateUserInfo(@PathVariable Integer uid){


        return new Result(true,StatusCode.OK,"修改成功");
    }

}

