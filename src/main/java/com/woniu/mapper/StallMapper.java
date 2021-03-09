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
    @Select("SELECT s.property_num,s.parking_lot_no,p.plot_address,plot_name,p.plot_id,stall_id" +
            "FROM t_stall AS s " +
            "JOIN t_plot AS p " +
            "WHERE user_id=1 " +
            "AND s.plot_id=p.plot_id")
    List<StallVo> getStallVos(Integer userId);

}
