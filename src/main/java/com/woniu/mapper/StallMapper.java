package com.woniu.mapper;

import com.woniu.model.Stall;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniu.vo.StallVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface StallMapper extends BaseMapper<Stall> {
    /**
     * 查询可以上架的车位信息
     * @param userId
     * @return
     */
    @Select("SELECT s.property_num,s.parking_lot_no,p.plot_address,plot_name,p.plot_id,stall_id " +
            "            FROM t_stall AS s " +
            "             JOIN t_plot AS p " +
            "             WHERE user_id=1 " +
            "             AND s.plot_id=p.plot_id " +
            "            and s.status=1")
    List<StallVo> getStallVos(Integer userId);

    /***
     * 上架存入数据库
     * @param stallVo
     * @return
     */
    @Insert("insert into t_putaway(user_id,plot_id,stall_id,unit_price,shelf_time,upstall_time)" +
            "value(#{letterId},#{plotId},#{stallId},#{price},#{shelfTime},#{date})")
    Integer addPutAway(StallVo stallVo);

    /**
     * 查询所有车位状态
     * @return
     */
    @Select("SELECT p.shelf_time,p.`status`,p.unit_price,p.upstall_time," +
            "s.address,s.parking_lot_no,s.property_num,t.plot_name,p.putaway_id " +
            "FROM t_putaway AS p " +
            "JOIN  t_stall AS s " +
            "ON p.stall_id=s.stall_id " +
            "JOIN t_plot AS t " +
            "ON s.plot_id=t.plot_id")
    List<StallVo> findStall();

    /**
     * 下架车位方法（状态改为2）
     * @param putawayId
     * @return
     */
    @Update("update t_putaway set status=2 where putaway_id=#{putawayId}")
    Integer updatePutaway(Integer putawayId);

    //重新上架的接口
    @Update("update t_stall set status=2 where stall_id=#{stallId}")
    public int reUpStall(Integer satllId);

    //车位被下单的状态
    @Update("update t_stall set status=2 where stall_id=#{stallId}")
    public int getStall(Integer stallId);

    //下架的方法
    @Update("update t_stall set status=1 where stall_id=#{stallId}")
    public int downStall(Integer stallId);


}
