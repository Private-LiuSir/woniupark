package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniu.mapper.StallMapper;
import com.woniu.model.Plot;
import com.woniu.model.Stall;
import com.woniu.service.PlotService;
import com.woniu.service.StallService;
import com.woniu.util.Result;
import com.woniu.vo.CreateStallVo;
import com.woniu.vo.StallVo;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private PlotService plotService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 新增车位信息
     * @param createStallVo
     * @return
     */
    @RequestMapping("createStall")
    public Result createStall(@RequestBody CreateStallVo createStallVo){
        System.out.println(createStallVo.getPlotName()+"------------------------------");
        QueryWrapper<Plot> wrapper = new QueryWrapper<>();
        wrapper.eq("plot_name",createStallVo.getPlotName());
        Plot plot = plotService.getOne(wrapper);
        Stall t = new Stall();
        t.setAddress(createStallVo.getAddress());
        t.setPropertyNum(createStallVo.getPropertyNum());
        t.setParkingLotNo(createStallVo.getParkingLotNo());
        t.setPlotId(plot.getPlotId());
        boolean save = stallService.save(t);
        return new Result(save);
    }

    /**
     * 根据用户id查询所有车位信息
     * @return
     */
    @RequestMapping("getStalls")
    public Result getStalls(Integer userId){
        List stallVos = stallService.getStallVos(userId);
        if (!ObjectUtils.isEmpty(stallVos)){
            return new Result(stallVos);
        }

        return new Result(false);
    }

    /**
     * 删除车位信息
     * @param parkingLotNo
     * @return
     */
    @RequestMapping("deletStall/{parkingLotNo}")
    public Result deletStall(@PathVariable Integer parkingLotNo){
        System.out.println(parkingLotNo+"---------------------------");
        QueryWrapper<Stall> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_lot_no",parkingLotNo);
        boolean remove = stallService.remove(wrapper);
        return new Result(remove);
    }

    /***
     * 根据车位号查询车位信息
     * @param parkingLotNo
     * @return
     */
    @RequestMapping("getStall/{parkingLotNo}")
    public Result getStall(@PathVariable Integer parkingLotNo){
        System.out.println(parkingLotNo+"-----------------------------");
        QueryWrapper<Stall> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_lot_no",parkingLotNo);
        Stall one = stallService.getOne(wrapper);
        System.out.println(one+"**************************");
        if (one==null){
            return new Result(false);
        }

        return new Result(true);
    }

    /**
     * redis中新增审核信息表
     * @param stallVo
     * @return
     */
    @RequestMapping("checkStall")
    public Result checkStall(@RequestBody StallVo stallVo){
        stallVo.setLetterId(1);
        System.out.println(stallVo);
        stallService.insertcheck(stallVo);

        return new Result(true);
    }


}

