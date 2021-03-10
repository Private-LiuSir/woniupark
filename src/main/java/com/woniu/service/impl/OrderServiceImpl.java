package com.woniu.service.impl;

import com.woniu.mapper.UserInfoMapper;
import com.woniu.model.Order;
import com.woniu.mapper.OrderMapper;
import com.woniu.model.UserInfo;
import com.woniu.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniu.util.DateUtil;
import com.woniu.vo.OrderVo;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import com.woniu.vo.OrderVo;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
    //order的mapper


    @Resource
    private UserInfoMapper userInfoMapper;





    //根据订单ID 返回订单详情
    @Override
    public OrderVo getOrderInfo(@RequestBody Integer orderId) {
        OrderVo orderVo = orderMapper.selectInfoByOrderId(orderId);
        return orderVo;
    }

    //订单结单的方法
    @Override
    public BigDecimal statement(Integer orderId) {
        //1、获取订单的上架上架
        //2、计算租用时长
        //3、在用户详情里面余额扣除  余额不足时处理方式
        //4、获取平台的结算比例
        //5、计算出租客  物业的利润
        //6、修改数据库的时间
        System.out.println("开始定单结算的业务");
        //获取数据库的订单对象
        Order order = orderMapper.selectById(orderId);
        //获取下单时间
        Date gmtCreate = order.getGmtCreate();
        //获取当前时间  即结单时间
        Date statementDate = new Date(System.currentTimeMillis());
        Integer integer = DateUtil.maxTime(gmtCreate, statementDate);
        //转换为精确数
        BigDecimal hour=new BigDecimal(integer);
        //获取上架的单价
        BigDecimal price = order.getPrice();
        //计算订单总价
        BigDecimal total = price.multiply(hour);
        //判断租客的余额是否足够  需要先获取用户余额
        UserInfo userInfo = userInfoMapper.selectById(order.getUserId());
        //获取用户的余额
        BigDecimal leaveMoney = userInfo.getMoney();
        int i = leaveMoney.compareTo(total);
        //判断余额是否充足 不足返回-1  操作失败  需要充值
        if(i<0){
            System.out.println("余额不足，操作失败");
            return null;
        }
        //余额充足  可以进行转账   结单处理
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        //获取用户方的提成比例
        String letter = valueOperations.get("woniupark:commission:letter");
        //获取出租方的BigDecimal比例对象
        BigDecimal letterBigDecimal = new BigDecimal(letter);
        //获取物业方的提成比例
        String property = valueOperations.get("woniupark:commission:property");
        //获取物业的big的BigDecimal比例对象
        BigDecimal propertyBigDecimal = new BigDecimal(property);
        //获取出租方  物业的info信息
        UserInfo letterInfo = userInfoMapper.selectById(order.getLetterId());
        UserInfo propertyInfo = userInfoMapper.selectById(order.getPlotId());
        //获取出租方的余额
        BigDecimal letterInfoMoney = letterInfo.getMoney();
        //获取物业的余额
        BigDecimal propertyInfoMoney = propertyInfo.getMoney();
        //计算出租方收益
        BigDecimal letterCommiss = total.multiply(letterBigDecimal);
        letterCommiss.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        //计算物业收益
        BigDecimal propCommiss = total.multiply(propertyBigDecimal);
        propCommiss.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        //将收益支出设置回查询的对象

        //用户
        userInfo.setMoney(leaveMoney.subtract(total));

        System.out.println("客户减去的金额"+leaveMoney.doubleValue());
        //出租客
        BigDecimal add = letterInfoMoney.add(letterCommiss, MathContext.DECIMAL32);
        add.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        System.out.println("出租客收益"+letterCommiss.doubleValue());
        System.out.println("出租客余额"+add.doubleValue());
        letterInfo.setMoney(add);
        //物业
        BigDecimal add1 = propertyInfoMoney.add(propCommiss);
        add1.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        System.out.println("物业收益"+propCommiss.doubleValue());
        System.out.println("物业余额"+add1.doubleValue());
        propertyInfo.setMoney(add1);
        //修改数据库数据
        userInfoMapper.updateById(propertyInfo);
        userInfoMapper.updateById(letterInfo);
        userInfoMapper.updateById(userInfo);

        //修改订单的状态
        order.setStatus(2);
        //设置结单时间
        order.setStatementTime(statementDate);
        //设置订单总价
        order.setMoney(total);
        orderMapper.updateById(order);

        System.out.println("结单业务完成，开始判断是否重新上架该车位");
        //订单完成  后续是判定是否需要重新上架的业务

        //获取车位ID
        Integer stallId = order.getStallId();
        //获取数据库的上架信息
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //获取用户上架的时长
        Object existTime = hashOperations.get("woniupark:letter:"+order.getStallId(), "上架时长");
        //判断数据库这个key是否还存在
        if(!ObjectUtils.isEmpty(existTime)){
            System.out.println("上架时长未过期，开始判断是否符合上架规则");
            //redis中可以取到该数据 还没有过期
            int i1 = Integer.parseInt(existTime.toString());
            System.out.println("客户出租时间"+i1);
            //获取上架的时间
            String upTime = hashOperations.get("woniupark:letter:"+order.getStallId(), "上架时间").toString();
            //获取目前上架的时间  取整
            Integer integer1 = DateUtil.maxTime(DateUtil.stringToDate(upTime), new Date(System.currentTimeMillis()));
            //剩余时间超过5小时   可以重新上架该车位
            System.out.println("已经上架时间："+integer1);
            if(i1-integer1>5){
                //在上架集合中添加该车位的ID
                System.out.println("车位剩余上架时间大于5小时，在上架集合中查询该车位ID");
                stringRedisTemplate.opsForList().leftPush("woniupark:already-shelves",order.getStallId().toString());
            }
        }
        return total;
    }
}
