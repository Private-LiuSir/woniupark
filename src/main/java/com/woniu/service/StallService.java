package com.woniu.service;

import com.woniu.model.Stall;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woniu.vo.CreateStallVo;
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
     * 将数据存入待上架审核车位信息redis
     * @param stallVo
     */
    void insertcheck(StallVo stallVo);
}
