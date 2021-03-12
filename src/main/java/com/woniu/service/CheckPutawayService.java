package com.woniu.service;

import com.woniu.model.CheckPutaway;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woniu.vo.CheckPutawayVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-11
 */
public interface CheckPutawayService extends IService<CheckPutaway> {
    List<CheckPutawayVo> getShowCheckPutaway(Integer userId);
}
