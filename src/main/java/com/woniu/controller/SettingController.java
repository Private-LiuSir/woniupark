package com.woniu.controller;

import com.woniu.util.Result;
import com.woniu.vo.DistributionVo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/set")
public class SettingController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequiresRoles(value ="平台管理员")
    @RequestMapping("/distribution")
    public Result setDistribution(@RequestBody DistributionVo distribution){
        System.out.println("前台数据："+distribution);
        //获取redis操作工具
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        //设置出租客
        operations.set("woniupark:commission:letter",distribution.getLetterDistribution()+"");
        //设置物业
        operations.set("woniupark:commission:property",distribution.getPlotDistribution()+"");
        return new Result(true);
    }

    @RequiresRoles(value ="平台管理员")
    @RequestMapping("/commission")
    public Result getCommission(){
        DistributionVo distributionVo = new DistributionVo();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        //获取并设置出租客的比例
        distributionVo.setLetterDistribution(Double.valueOf(operations.get("woniupark:commission:letter")));
        //设置物业
        distributionVo.setPlotDistribution(Double.valueOf(operations.get("woniupark:commission:property")));
        return new Result(distributionVo);
    }
}
