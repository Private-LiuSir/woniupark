package com.woniu.controller;

import com.woniu.util.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.woniu.service.StallService;
import com.woniu.util.Result;
import com.woniu.vo.StallVo;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Date;
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
     * @param stallVo
     * @return
     */
    @RequestMapping("createStall")
    public Result createStall(@RequestBody StallVo stallVo){

        boolean b = stallService.addCheck(stallVo);

        return new Result(b);
    }

    /**
     * 根据用户id查询所有车位信息
     * @return
     */
    @RequestMapping("getStalls/{letterId}")
    public Result getStalls(@PathVariable Integer letterId){
        List stallVos = stallService.getStallVos(letterId);
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

        QueryWrapper<Stall> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_lot_no",parkingLotNo);
        Stall one = stallService.getOne(wrapper);

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


        stallService.insertcheck(stallVo);

        return new Result(true);
    }

    /**
     * 获取待审核车位信息（用于平台方审核车位上架信息）
     * @return
     */
    @RequestMapping("getCheckStalls")
    public Result getCheckStall(Integer stallId){
        List<StallVo> checkStalls = stallService.getCheckStalls();
        return new Result(checkStalls);
    }
    /**
     * 修改审核车位状态为2
     * @return
     */
    @RequestMapping("updateCheckStallStatuTo2")
    public void updateCheckStallStatuTo2(@RequestBody StallVo stallVo){
        stallService.updateCheckStallStatuTo2(stallVo.getStallId());
        QueryWrapper<Stall> wrapper = new QueryWrapper<>();
        wrapper.eq("stall_id",stallVo.getStallId());
        Stall stall = stallService.getOne(wrapper);
        stall.setStatus(2);
        stallService.update(stall,wrapper);
        Date date = DateUtil.stringToDate(stallVo.getUpstallTime());
        stallVo.setDate(date );
        stallService.addPutAway(stallVo);


    }
    /**
     * 修改审核车位状态为3
     * @return
     */
    @RequestMapping("updateCheckStallStatuTo3/{stallId}")
    public void updateCheckStallStatuTo3(@PathVariable Integer stallId){
        stallService.updateCheckStallStatusTo3(stallId);
    }

    /**
     * 上架方法
     * @param stallVo
     * @return
     */
    @RequestMapping("up")
    public Result upStall(@RequestBody StallVo stallVo){
        System.out.println(stallVo);
        int i = stallService.upStall(stallVo);

        return new Result();
    }

    /**
     * 获取待审核的全部车位信息
     * @return
     */
    @RequestMapping("getCheck")
    public Result getCheck(){
        List<StallVo> checks = stallService.getChecks();
        return new Result(checks);
    };

    /**
     * 审核通过后向mysql存数据
     * @param stallVo
     * @return
     */
    @RequestMapping("addStall")
    public Result addStall(@RequestBody StallVo stallVo){
        QueryWrapper<Plot> wrapper = new QueryWrapper<>();
        wrapper.eq("plot_name",stallVo.getPlotName());
        Plot plot = plotService.getOne(wrapper);
        Stall stall = new Stall();
        stall.setPlotId(plot.getPlotId());
        stall.setPropertyNum(stallVo.getPropertyNum());
        stall.setParkingLotNo(stallVo.getParkingLotNo());
        stall.setAddress(plot.getPlotAddress());
        stall.setUserId(stallVo.getLetterId());
        boolean save = stallService.save(stall);
        return new Result(save);
    };

    /**
     * 审核车位信息失败后删除该车位在redis中的待审核信息
     * @param parkingLotNo
     */
    @RequestMapping("deleteCheck/{parkingLotNo}")
    public void deleteCheck(@PathVariable Integer parkingLotNo){
        stallService.deleteCheck(parkingLotNo);
    }

    /**
     * 根据出租客id查询所有已上架车位信息
     * @return
     */
    @RequestMapping("getputAways")
    public Result getPutAways(){
        List<StallVo> stall = stallService.findStall();

        return  new Result(stall);
    }
    @RequestMapping("updatePutaway/{putawayId}")
    public Result updatePutaway(@PathVariable Integer putawayId){
        System.out.println(putawayId);
        Integer integer = stallService.updatePutaway(putawayId);
        return new Result(integer);
    }

}

