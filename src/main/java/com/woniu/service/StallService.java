package com.woniu.service;

import com.woniu.model.Stall;
import com.baomidou.mybatisplus.extension.service.IService;
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

    //上架的方法
    public int upStall(StallVo stallVo);
    //查询已经上架的车位的接口  所有
    public List<StallVo> findAllStall();

}
