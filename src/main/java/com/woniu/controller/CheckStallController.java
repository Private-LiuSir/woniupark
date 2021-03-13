package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniu.model.CheckStall;
import com.woniu.service.CheckPutawayService;
import com.woniu.service.CheckStallService;
import com.woniu.util.Result;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-11
 */
@RestController
@RequestMapping("/checkStall")
public class CheckStallController {
    @Resource
    private CheckStallService checkStallService;

    /**
     * 根据出租客的id获取车位信息的审核记录
     * @param userId
     * @return
     */

    @RequestMapping("getShowCheckStall/{userId}")
    public Result getShowCheckStall(@PathVariable Integer userId){
        QueryWrapper<CheckStall> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<CheckStall> checkStalls = checkStallService.list(wrapper);
        return new Result(checkStalls);
    }

}

