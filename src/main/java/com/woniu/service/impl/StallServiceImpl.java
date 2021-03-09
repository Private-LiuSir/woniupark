package com.woniu.service.impl;

import com.woniu.model.Stall;
import com.woniu.mapper.StallMapper;
import com.woniu.service.StallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.vo.CreateStallVo;
import com.woniu.vo.StallVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@Service
public class StallServiceImpl extends ServiceImpl<StallMapper, Stall> implements StallService {
    @Resource
    private StallMapper stallMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<StallVo> getStallVos(Integer userId) {
        List stallVos = stallMapper.getStallVos(userId);
        return stallVos;
    }

    @Override
    public void insertcheck(StallVo stallVo) {
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
        ,"小区id",stallVo.getPlotId().toString());
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
                ,"小区名",stallVo.getPlotName());
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
                ,"地址",stallVo.getPlotAddress());
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
                ,"单价",stallVo.getPrice().toString());
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
                ,"用户id",stallVo.getLetterId().toString());
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
                ,"上架时长",stallVo.getShelfTime().toString());
        stringRedisTemplate.opsForHash().put("woniupark:letterCheck:"+stallVo.getLetterId()
                ,"上架时间",new Date(System.currentTimeMillis()).toLocaleString());

    }
}
