package com.woniu.controller;


import com.woniu.model.Plot;
import com.woniu.service.PlotService;
import com.woniu.util.Result;
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
@RequestMapping("/plot")
public class PlotController {
    @Resource
    private PlotService plotService;

    @RequestMapping("getPlots")
    public Result getPlots(){
        List<Plot> plots = plotService.list(null);
        return new Result(plots);
    }

}

