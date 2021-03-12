package com.woniu.mapper;

import com.woniu.model.CheckPutaway;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniu.vo.CheckPutawayVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-11
 */
public interface CheckPutawayMapper extends BaseMapper<CheckPutaway> {
    @Select("SELECT t.plot_name,s.address,s.property_num," +
            "s.parking_lot_no,c.cause,c.check_time,c.shelf_time" +
            ",c.`status`,c.unit_price,c.upstall_time" +
            " FROM `t_check_putaway` AS c " +
            "JOIN t_stall AS s " +
            " ON s.stall_id=c.stall_id AND c.user_id=#{userId} " +
            "JOIN t_plot AS t " +
            "ON s.plot_id = t.plot_id")
    List<CheckPutawayVo> getShowCheckPutaway(Integer userId);
}
