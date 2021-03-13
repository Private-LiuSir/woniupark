package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniu.model.CheckPutaway;
import com.woniu.model.CheckStall;
import com.woniu.service.CheckPutawayService;
import com.woniu.util.Result;
import com.woniu.vo.CheckPutawayVo;
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
@RequestMapping("/checkPutaway")
public class CheckPutawayController {
    @Resource
    private CheckPutawayService checkPutawayService;

    /**
     * 根据出租客的id获取上架审核记录
     * @param userId
     * @return
     */
    @RequestMapping("getShowCheckPutaway/{userId}")
    public Result getShowCheckPutaway(@PathVariable Integer userId){

        List<CheckPutawayVo> putaway = checkPutawayService.getShowCheckPutaway(userId);
        return new Result(putaway);
    }

}

