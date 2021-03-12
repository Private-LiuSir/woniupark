package com.woniu.service.impl;

import com.woniu.model.CheckPutaway;
import com.woniu.mapper.CheckPutawayMapper;
import com.woniu.service.CheckPutawayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.vo.CheckPutawayVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-11
 */
@Service
public class CheckPutawayServiceImpl extends ServiceImpl<CheckPutawayMapper, CheckPutaway> implements CheckPutawayService {
    @Resource
    private CheckPutawayMapper checkPutawayMapper;
    @Override
    public List<CheckPutawayVo> getShowCheckPutaway(Integer userId) {
        List<CheckPutawayVo> putaway = checkPutawayMapper.getShowCheckPutaway(userId);
        return putaway;
    }
}
