package com.woniu.mapper;

import com.woniu.model.Stall;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniu.vo.CreateStallVo;
import com.woniu.vo.StallVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

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

}
