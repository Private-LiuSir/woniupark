package com.woniu.controller;

import com.woniu.model.CheckPutaway;
import com.woniu.model.CheckStall;
import com.woniu.service.*;
import com.woniu.util.DateUtil;
import com.woniu.vo.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniu.model.Plot;
import com.woniu.model.Stall;
import com.woniu.util.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.woniu.service.StallService;
import com.woniu.vo.StallVo;
import javax.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

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
    @Resource
    private CheckStallService checkStallService;
    @Resource
    private CheckPutawayService checkPutawayService;

    /**
     * 新增车位信息
     * @param check
     * @return
     */
    @RequestMapping("createStall")
    public Result createStall(@RequestBody CheckStall check){
        //存入redis
        boolean b = stallService.addCheck(check);

        //存入mysql车位信息表
        checkStallService.save(check);

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

        //redis
        stallService.insertcheck(stallVo);
        //mysql
        CheckPutaway checkPutaway = new CheckPutaway();
        checkPutaway.setPlotId(stallVo.getPlotId());
        checkPutaway.setUserId(stallVo.getLetterId());
        checkPutaway.setShelfTime(stallVo.getShelfTime());
        checkPutaway.setStallId(stallVo.getStallId());
        checkPutaway.setUnitPrice(stallVo.getPrice());
        checkPutawayService.save(checkPutaway);

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
        //修改车位表中的状态为2
        QueryWrapper<Stall> wrapper = new QueryWrapper<>();
        wrapper.eq("stall_id",stallVo.getStallId());
        Stall stall = stallService.getOne(wrapper);
        stall.setStatus(2);
        stallService.update(stall,wrapper);
        //上架表
        Date date = DateUtil.stringToDate(stallVo.getUpstallTime());
        stallVo.setDate(date );
        stallService.addPutAway(stallVo);
        //审核表
        CheckPutaway checkPutaway = new CheckPutaway();
        checkPutaway.setStatus(2);
        checkPutaway.setCheckTime(new Date(System.currentTimeMillis()));
        QueryWrapper<CheckPutaway> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("stall_id",stallVo.getStallId());
        checkPutawayService.update(checkPutaway,wrapper1);


    }
    /**
     * 修改审核车位状态为3
     * @return
     */
    @RequestMapping("updateCheckStallStatuTo3")
    public void updateCheckStallStatuTo3(@RequestBody CheckPutawayVo checkPutawayVo){
        stallService.updateCheckStallStatusTo3(checkPutawayVo.getStallId());
        //修改mysql车位表状态
        QueryWrapper<Stall> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("stall_id",checkPutawayVo.getStallId());
        Stall stall = new Stall();
        stall.setStatus(1);
        stallService.update(stall,wrapper1);

        //修改mysql上架审核表数据
        CheckPutaway checkPutaway = new CheckPutaway();
        checkPutaway.setStatus(3);
        checkPutaway.setCause(checkPutawayVo.getCause());
        checkPutaway.setCheckTime(new Date(System.currentTimeMillis()));
        QueryWrapper<CheckPutaway> wrapper = new QueryWrapper<>();
        wrapper.eq("stall_id",checkPutawayVo.getStallId());
        checkPutawayService.update(checkPutaway,wrapper);

    }

    /**
     * 上架方法
     * @param stallVo
     * @return
     */
    @RequestMapping("up")
    public Result upStall(@RequestBody StallVo stallVo){

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
     * @param checkVo
     * @return
     */
    @RequestMapping("addStall")
    public Result addStall(@RequestBody checkVo checkVo){
        QueryWrapper<Plot> wrapper = new QueryWrapper<>();
        wrapper.eq("plot_name",checkVo.getPlotName());
        //去小区表捞取数据填充车位对象
        Plot plot = plotService.getOne(wrapper);
        Stall stall = new Stall();
        stall.setPlotId(plot.getPlotId());
        stall.setPropertyNum(checkVo.getPropertyNum());
        stall.setParkingLotNo(checkVo.getParkingLotNo());
        stall.setAddress(plot.getPlotAddress());
        stall.setUserId(checkVo.getLetterId());
        //将车位对象填充入mysql车位表中
        boolean save = stallService.save(stall);
        //将前端审核通过的数据存入审核车位（CheckStall）对象中
        CheckStall checkStall = new CheckStall();
        checkStall.setStatus(2);
        checkStall.setCheckTime(new Date(System.currentTimeMillis()));
        QueryWrapper<CheckStall> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parking_lot_no",checkVo.getParkingLotNo());
        checkStallService.update(checkStall,wrapper1);
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

    /**
     * 下架的方法，修改上架表状态和车位表的状态
     * @param checkPutawayVo
     * @return
     */
    @RequestMapping("updatePutaway")
    public Result updatePutaway(@RequestBody CheckPutawayVo checkPutawayVo){
        System.out.println(checkPutawayVo);
        //修改上架表的状态为2
        Integer integer = stallService.updatePutaway(checkPutawayVo.getPutawayId());
        //修改车位表的状态为1
        QueryWrapper<Stall> wrapper = new QueryWrapper<>();
        wrapper.eq("stall_id",checkPutawayVo.getStallId());
        Stall stall = new Stall();
        stall.setStatus(1);
        stallService.update(stall,wrapper);


        return new Result(integer);
    }

    @RequestMapping("/findUpstall")
    public Result getUpStall(){
        List<StallVo> allStall = stallService.findAllStall();
        Result<List<StallVo>> listResult = new Result<>(allStall);
        return listResult;
    }

    /**
     * 审核车位表的修改状态为3，添加审核建议
     * @param checkVo
     * @return
     */
    @RequestMapping("updateCheckStall")
    public Result updateCheckStall(@RequestBody checkVo checkVo){
        System.out.println(checkVo);
        QueryWrapper<CheckStall> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_lot_no",checkVo.getParkingLotNo());
        CheckStall checkStall = new CheckStall();
        checkStall.setStatus(3);
        checkStall.setCause(checkVo.getCause());
        Date date = new Date(System.currentTimeMillis());
        checkStall.setCheckTime(date);
        boolean b = checkStallService.update(checkStall, wrapper);

        return new Result(b);
    }

}

