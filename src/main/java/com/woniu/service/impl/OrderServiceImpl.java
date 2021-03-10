package com.woniu.service.impl;

import com.woniu.model.Order;
import com.woniu.mapper.OrderMapper;
import com.woniu.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.vo.OrderVo;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
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
//声明式事务  除查询订单外均需事务管理
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    //order的mapper
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //创建订单的方法
    // 返回0表示已经被人抢租了
    // 返回值1表示下单成功
    // 返回2表示下单时卡顿导致锁过期，或者其他用户在过期期间抢租了该车位   可能需要重新刷新再下单（前端提示为刷新页面进行加载）
    @Override
    public int createOrder(OrderVo orderVo) {
        Integer execute=0;
        //根据前端传回的 stallID查询redis中是否有该车位的上架记录
        List<String> range = stringRedisTemplate.opsForList().range("woniupark:already-shelves", 0, -1);
        boolean contains = range.contains(orderVo.getStallId() + "");

       // Boolean hasKey = stringRedisTemplate.hasKey("woniupark:letter:"+orderVo.getStallId());
        System.out.println(contains);

        //判断是否已上架
        if(contains){
            //获取String的操作工具
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            //获取一个随机的uuid字符串作为锁的值  避免误删除
            String lockValue = UUID.randomUUID().toString();
            System.out.println(lockValue);
            //已经上架  设置分布式锁  设置锁的生命周期为120秒  即两分钟
            Boolean ifAbsent = valueOperations.setIfAbsent("woniupark:createOrder:" + orderVo.getStallId(), lockValue, 120, TimeUnit.SECONDS);
            System.out.println(2);
            if(ifAbsent){
                //创建一个SessionCallback对象  内部为事务代码
                SessionCallback<Object> callback = new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations operations) throws DataAccessException {
                        //监听锁
                        operations.watch("woniupark:createOrder:" + orderVo.getStallId());
                        //开启事务
                        operations.multi();
                        //操作redis数据中的字段
                        operations.opsForList().remove("woniupark:already-shelves",1,orderVo.getStallId().toString());
                        List exec = operations.exec();
                        //判断返回的集合是不是空的
                        if(ObjectUtils.isEmpty(exec)){
                            return 0;
                        }
                        return 1;
                    }
                };
                execute = (Integer)stringRedisTemplate.execute(callback);

            }
            String  s=valueOperations.get("woniupark:createOrder:" + orderVo.getStallId()).toString();
            System.out.println(s);
            if(ObjectUtils.isEmpty(s)){
                //锁已经过期
                return 2;
            }
            //判断是否为自己的锁
            if(s.equals(lockValue)){
                //是  删除该锁
                stringRedisTemplate.delete("woniupark:createOrder:" + orderVo.getStallId());
                //开始往数据库添加订单
                Order order = new Order();
                //设置用户ID
                order.setUserId(orderVo.getUserId());
                //设置小区ID
                order.setPlotId(orderVo.getPlotId());
                //设置车位ID
                order.setStallId(orderVo.getStallId());
                //设置出租方ID
                order.setLetterId(orderVo.getLetterId());
                //设置订单状态
                order.setStatus(1);
                //订单的事件使用创建时间  自动生成  金额由结单后核算  结单时间结单时添加
                int insert = orderMapper.insert(order);
                return insert;
            }
        }
        return execute;
    }
}
