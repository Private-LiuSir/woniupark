package com.woniu.controller;


import com.woniu.service.StallService;
import com.woniu.util.Result;
import com.woniu.vo.StallVo;
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
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/stall")
public class StallController {

    @Resource
    private StallService stallService;

    @RequestMapping("/up")
    public Result upStall(StallVo stallVo){
        int i = stallService.upStall(stallVo);

        return new Result();
    }

    @RequestMapping("/findUpstall")
    public Result findUpStall(){
        List<StallVo> allStall = stallService.findAllStall();
        return new Result(allStall);
    }

}

