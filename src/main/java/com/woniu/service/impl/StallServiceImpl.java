package com.woniu.service.impl;

import com.woniu.model.CheckStall;
import com.woniu.model.Stall;
import com.woniu.mapper.StallMapper;
import com.woniu.service.StallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.util.DateUtil;
import com.woniu.util.Result;
import com.woniu.vo.StallVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    /**
     * 审核车位信息失败后删除该车位在redis中的待审核信息
     * @param parkingLotNo
     */
    @Override
    public void deleteCheck(Integer parkingLotNo) {
        System.out.println(parkingLotNo+"*********************************");
        stringRedisTemplate.delete("woniupark:check:"+parkingLotNo);
        stringRedisTemplate.opsForList().remove("woniupark:parkingLotNo",0,parkingLotNo.toString());
    }

    /**
     * 获取需要审核的车位信息
     * @return
     */
    @Override
    public List<StallVo> getChecks() {
        //获取目前已将上传到审核列表的id
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        List<String> range = listOperations.range("woniupark:parkingLotNo", 0, -1);
        //获取hash的操作对象
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //创建集合  保存车位信息
        ArrayList<StallVo> stallVos = new ArrayList<>();
        //遍历ID列表  然后从redis中获取对应的车位集合
        range.forEach(id -> {
            //创建车库vo对象
            StallVo stallVo = new StallVo();
            //从redis读取数据并且赋值
            System.out.println(id);
            stallVo.setAddress(hashOperations.get("woniupark:check:" + id, "地址").toString());
            stallVo.setPlotName(hashOperations.get("woniupark:check:" + id, "小区名").toString());
            stallVo.setLetterId(Integer.valueOf(hashOperations.get("woniupark:check:" + id, "出租方ID").toString()));
            stallVo.setParkingLotNo(Integer.valueOf(hashOperations.get("woniupark:check:" + id, "车位号").toString()));
            stallVo.setPropertyNum(Integer.valueOf(hashOperations.get("woniupark:check:" + id, "产权编号").toString()));
            stallVo.setUpstallTime(hashOperations.get("woniupark:check:" + id, "提交时间").toString());
            //添加到集合中
            stallVos.add(stallVo);

        });
        //将数据传出去
        return stallVos;
    }

    /**
     * 新增需要审核的车位信息
     * @param checkStall
     */
    @Override
    public boolean addCheck(CheckStall checkStall) {
        //获取操作Hash的工具
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //查询当前车位是否已经提交审核
        Boolean hasKey = stringRedisTemplate.hasKey("woniupark:check:" + checkStall.getPropertyNum());
        //判断是否已经提交过了
        if (hasKey) {

            //已经提交   不再进行提交处理
            return false;
        } else {
            //创建一个hashmap 用于保存数据
            HashMap<String, String> hashMap = new HashMap<>();
            //往集合中添加数据
            hashMap.put("地址", checkStall.getAddress());
            hashMap.put("小区名", checkStall.getPlotName());
            hashMap.put("出租方ID", checkStall.getUserId().toString());
            hashMap.put("车位号", checkStall.getParkingLotNo().toString());
            hashMap.put("产权编号", checkStall.getPropertyNum().toString());
            //系统生成时间戳  转换为字符串存入
            long timeMillis = System.currentTimeMillis();
            //转换为时间
            Date date = new Date(timeMillis);
            //获取时间格式
            String string = DateUtil.dateToString(date);
            hashMap.put("提交时间", string);
            hashOperations.putAll("woniupark:check:" + checkStall.getParkingLotNo(), hashMap);

            //提交完成数据后  把上架了的车位号存到上架ID集合中
            ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
            //将新增的车位号添加到集合中
            listOperations.rightPush("woniupark:parkingLotNo", checkStall.getParkingLotNo().toString());
        }
        return true;
    }
    //后台执行上架的方法
    @Override
    public int upStall(StallVo stallVo) {
        //获取前台传入的数据   包地址  车位号    小区名字    单价   上架时间（系统生成）  自动下架时间()
        //获取操作Hash的工具
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //查询当前车位是否已经上架是否已经上架
        Boolean hasKey = stringRedisTemplate.hasKey("woniupark:letter:"+stallVo.getStallId());
        //判断是否上架
        if(hasKey){
            //已经上架   不再进行上架处理
        }else{
            //创建一个hashmap 用于保存数据
            HashMap<String, String> hashMap = new HashMap<>();
            //往集合中添加数据
            hashMap.put("地址",stallVo.getAddress());
            hashMap.put("小区名",stallVo.getPlotName());
            hashMap.put("小区ID",stallVo.getPlotId().toString());
            hashMap.put("出租方ID",stallVo.getLetterId().toString());
            hashMap.put("单价",stallVo.getPrice().toString());
            hashMap.put("车位号",stallVo.getParkingLotNo().toString());
            hashMap.put("上架时长",stallVo.getShelfTime().toString());
            //系统生成时间戳  转换为字符串存入
            long timeMillis = System.currentTimeMillis();
            //转换为时间
            Date date = new Date(timeMillis);
            //获取时间格式
            String string = DateUtil.dateToString(date);
            hashMap.put("上架时间",string);
            hashOperations.putAll("woniupark:letter:"+stallVo.getStallId(),hashMap);

            //设置有效期

            stringRedisTemplate.expire("woniupark:letter:"+stallVo.getStallId(),stallVo.getShelfTime(), TimeUnit.HOURS);

            //上架完成数据后  把上架了的车位ID存到上架ID集合中
            ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
            //将新增的车位ID添加到集合中
            listOperations.rightPush("woniupark:already-shelves",stallVo.getStallId().toString());
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
            stallVo.setPlotName(hashOperations.get("woniupark:letter:"+id,"小区名").toString());
            stallVo.setPlotId(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"小区ID").toString()));
            stallVo.setLetterId(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"出租方ID").toString()));
            stallVo.setPrice(Double.valueOf(hashOperations.get("woniupark:letter:"+id,"单价").toString()));
            stallVo.setParkingLotNo(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"车位号").toString()));
            stallVo.setShelfTime(Integer.valueOf(hashOperations.get("woniupark:letter:"+id,"上架时长").toString()));
            stallVo.setUpstallTime(hashOperations.get("woniupark:letter:"+id,"上架时间").toString());
            stallVo.setStallId(Integer.valueOf(id));
            //添加到集合中
            stallVos.add(stallVo);
        });
        //将数据传出去
        return stallVos;
    }

    /**
     * 上架审核（存入redis待审核上架）
     * @param stallVo
     */
    @Override
    public Result insertcheck(StallVo stallVo) {
        //获取操作Hash的工具
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //查询当前车位是否已经上架是否已经上架
        Boolean hasKey = stringRedisTemplate.hasKey("woniupark:letterCheck:"+stallVo.getStallId());
        //判断是否上架
        if(hasKey){
            //已经上架   不再进行上架处理
            return new Result(false);
        }else{
            stallVo.setStatus(1);
            //创建一个hashmap 用于保存数据
            HashMap<String, String> hashMap = new HashMap<>();
            //往集合中添加数据
            hashMap.put("地址",stallVo.getPlotAddress());
            hashMap.put("小区名",stallVo.getPlotName());
            hashMap.put("小区ID",stallVo.getPlotId().toString());
            hashMap.put("出租方ID",stallVo.getLetterId().toString());
            hashMap.put("单价",stallVo.getPrice().toString());
            hashMap.put("车位号",stallVo.getParkingLotNo().toString());
            hashMap.put("上架时长",stallVo.getShelfTime().toString());
            hashMap.put("状态",stallVo.getStatus().toString());
//            hashMap.put("车位id",stallVo.getStallId().toString());
            //系统生成时间戳  转换为字符串存入
            long timeMillis = System.currentTimeMillis();
            //转换为时间
            Date date = new Date(timeMillis);
            //获取时间格式
            String string = DateUtil.dateToString(date);
            hashMap.put("上架时间",string);

            hashOperations.putAll("woniupark:letterCheck:"+stallVo.getStallId(),hashMap);

            //上架完成数据后  把上架了的车位ID存到上架ID集合中
            ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
            //将新增的车位ID添加到集合中
            listOperations.rightPush("woniupark:check-stay",stallVo.getStallId().toString());

        }
        return new Result(true);
    }

    @Override
    public Integer updatePutaway(Integer putawayId) {

        Integer putaway = stallMapper.updatePutaway(putawayId);

        return putaway;
    }
    /**
     * 查询所有车位状态
     * @return
     */
    @Override
    public List<StallVo> findStall() {
        List<StallVo> stall = stallMapper.findStall();
        return stall;
    }

    /***
     * 上架存入数据库
     * @param stallVo
     * @return
     */
    @Override
    public Integer addPutAway(StallVo stallVo) {
        Integer integer = stallMapper.addPutAway(stallVo);
        return integer;
    }

    /**
     * 修改审核车位状态为2
     * @return
     */
    @Override
    public void updateCheckStallStatuTo2(Integer stallId) {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("woniupark:letterCheck:"+stallId,"状态","2");

    }
    /**
     * 修改审核车位状态为3
     * @return
     */
    @Override
    public void updateCheckStallStatusTo3(Integer stallId) {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("woniupark:letterCheck:"+stallId,"状态","3");

    }

    /**
     * 获取待审核上架的车位信息
     * @return
     */
    @Override
    public List<StallVo> getCheckStalls() {
        //获取目前已将上架的ID列表
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        List<String> range = listOperations.range("woniupark:check-stay", 0, -1);
        //获取hash的操作对象
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //创建集合  保存车位信息
        ArrayList<StallVo> stallVos = new ArrayList<>();
        //遍历ID列表  然后从redis中获取对应的车位集合
        range.forEach(id ->{
            //创建车库vo对象
            StallVo stallVo = new StallVo();
            //从redis读取数据并且赋值
            stallVo.setAddress(hashOperations.get("woniupark:letterCheck:"+id,"地址").toString());
            stallVo.setPlotName(hashOperations.get("woniupark:letterCheck:"+id,"小区名").toString());
            stallVo.setPlotId(Integer.valueOf(hashOperations.get("woniupark:letterCheck:"+id,"小区ID").toString()));
            stallVo.setLetterId(Integer.valueOf(hashOperations.get("woniupark:letterCheck:"+id,"出租方ID").toString()));
            stallVo.setPrice(Double.valueOf(hashOperations.get("woniupark:letterCheck:"+id,"单价").toString()));
            stallVo.setParkingLotNo(Integer.valueOf(hashOperations.get("woniupark:letterCheck:"+id,"车位号").toString()));
            stallVo.setShelfTime(Integer.valueOf(hashOperations.get("woniupark:letterCheck:"+id,"上架时长").toString()));
            stallVo.setUpstallTime(hashOperations.get("woniupark:letterCheck:"+id,"上架时间").toString());

            stallVo.setStallId(Integer.valueOf(id));
            //判断状态为1则添加到集合中
            if (!ObjectUtils.isEmpty(hashOperations.get("woniupark:letterCheck:"+id,"状态"))){
                if (hashOperations.get("woniupark:letterCheck:"+id,"状态").toString().equals("1")){
                    stallVos.add(stallVo);
                }
            }

        });

        return stallVos;
    }


}
