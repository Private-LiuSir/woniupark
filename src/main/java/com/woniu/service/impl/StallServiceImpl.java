package com.woniu.service.impl;

import com.woniu.model.Stall;
import com.woniu.mapper.StallMapper;
import com.woniu.service.StallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.vo.StallVo;
import net.sf.jsqlparser.expression.DoubleValue;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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


    //后台执行上架的方法
    @Override
    public int upStall(StallVo stallVo) {
        //获取前台传入的数据   包地址  车位号    小区名字    单价   上架时间（系统生成）  自动下架时间()
        //获取操作Hash的工具
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //查询当前车位是否已经上架是否已经上架
        Boolean hasKey = stringRedisTemplate.hasKey("woniupark:letter:"+stallVo.getStall_id());
        //判断是否上架
        if(hasKey){
            //已经上架   不再进行上架处理
        }else{
            //创建一个hashmap 用于保存数据
            HashMap<String, String> hashMap = new HashMap<>();
            //往集合中添加数据
            hashMap.put("地址",stallVo.getAddress());
            hashMap.put("小区名",stallVo.getPlot_name());
            hashMap.put("小区ID",stallVo.getPlot_Id().toString());
            hashMap.put("出租方ID",stallVo.getLetter_Id().toString());
            hashMap.put("单价",stallVo.getPrice().toString());
            hashMap.put("车位号",stallVo.getParking_lot_no().toString());
            hashMap.put("上架时长",stallVo.getShelf_time().toString());
            //系统生成时间戳  转换为字符串存入
            long timeMillis = System.currentTimeMillis();
            //转换为时间
            Date date = new Date(timeMillis);
            //获取时间格式
            String toLocaleString = date.toLocaleString();
            hashMap.put("上架时间",toLocaleString);
            hashOperations.putAll("woniupark:letter:"+stallVo.getStall_id(),hashMap);

            //上架完成数据后  把上架了的车位ID存到上架ID集合中
            ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
            //将新增的车位ID添加到集合中
            listOperations.rightPush("woniupark:already-shelves",stallVo.getStall_id().toString());
        }
        return 0;
    }

    //获取当前已经上架车位的接口（可进行下单操作）
    @Override
    public List<StallVo> findAllStall() {
        //获取目前已将上架的ID列表
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        List<String> range = listOperations.range("woniupark:already-shelves", 0, -1);
        //获取hash的操作对象
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //创建集合  保存车位信息
        ArrayList<StallVo> stallVos = new ArrayList<>();
        //遍历ID列表  然后从redis中获取对应的车位集合
        range.forEach(id ->{
            //创建车库vo对象
            StallVo stallVo = new StallVo();
            //从redis读取数据并且赋值
            stallVo.setAddress(hashOperations.get("woniupark:letter:"+id,"地址").toString());
            stallVo.setPlot_name(hashOperations.get("woniupark:letter:"+id,"小区名").toString());
            stallVo.setPlot_Id(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"小区ID").toString()));
            stallVo.setLetter_Id(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"出租方ID").toString()));
            stallVo.setPrice(Double.valueOf(hashOperations.get("woniupark:letter:"+id,"单价").toString()));
            stallVo.setParking_lot_no(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"车位号").toString()));
            stallVo.setShelf_time(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"上架时长").toString()));
            stallVo.setUpstall_time(hashOperations.get("woniupark:letter:"+id,"上架时间").toString());
            stallVo.setStall_id(Integer.valueOf(id));
            //添加到集合中
            stallVos.add(stallVo);
        });
        //将数据传出去
        return stallVos;
    }
}
