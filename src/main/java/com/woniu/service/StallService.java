package com.woniu.service;

import com.woniu.model.Stall;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woniu.util.Result;
import com.woniu.vo.CreateStallVo;
import com.woniu.vo.StallVo;

import java.util.List;
import com.woniu.vo.StallVo;

import java.util.List;
import com.woniu.vo.StallVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface StallService extends IService<Stall> {
    /**
     * 根据用户id查询所有车位
     * @param userId
     * @return
     */
    List<StallVo> getStallVos(Integer userId);

    /**
     * 上架的方法
     */
    public int upStall(StallVo stallVo);

    /**
     * 查询已经上架的车位的接口  所有
     * @return
     */
    public List<StallVo> findAllStall();





    Result insertcheck(StallVo stallVo);

    /**
     * 获取redis中待审核的全部车位信息
     * @return
     */
    List<StallVo> getCheckStalls();

    /**
     * 修改审核车位状态为2
     * @return
     */
    void updateCheckStallStatuTo2(Integer stallId);
    /**
     * 修改审核车位状态为3
     * @return
     */
    void updateCheckStallStatusTo3(Integer stallId);

    /**
     * 新增需要审核的车位信息
     */
    boolean addCheck(StallVo stallVo);

    /**
     * 获取需要审核的车位信息
     * @return
     */
    List<StallVo> getChecks();

    /**
     * 审核车位信息失败后删除该车位在redis中的待审核信息
     * @param parkingLotNo
     */
    void deleteCheck(Integer parkingLotNo);

    /***
     * 上架存入数据库
     * @param stallVo
     * @return
     */
    Integer addPutAway(StallVo stallVo);

    /***
     * 获取全部通过审核的车位信息
     * @return
     */
    List<StallVo> findStall();

    Integer updatePutaway(Integer putawayId);

}
