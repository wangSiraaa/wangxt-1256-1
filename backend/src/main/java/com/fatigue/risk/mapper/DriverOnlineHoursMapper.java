package com.fatigue.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fatigue.risk.entity.DriverOnlineHours;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DriverOnlineHoursMapper extends BaseMapper<DriverOnlineHours> {

    @Select("SELECT SUM(IFNULL(order_count, 0)) FROM driver_online_hours " +
            "WHERE driver_id = #{driverId} AND work_date BETWEEN #{startDate} AND #{endDate} AND deleted = 0")
    Integer sumOrderCountByDateRange(@Param("driverId") Long driverId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Select("SELECT SUM(IFNULL(online_minutes, 0)) FROM driver_online_hours " +
            "WHERE driver_id = #{driverId} AND work_date BETWEEN #{startDate} AND #{endDate} AND deleted = 0")
    Integer sumOnlineMinutesByDateRange(@Param("driverId") Long driverId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Select("SELECT * FROM driver_online_hours " +
            "WHERE driver_id = #{driverId} AND work_date BETWEEN #{startDate} AND #{endDate} AND deleted = 0 " +
            "ORDER BY work_date ASC")
    List<DriverOnlineHours> selectByDriverAndDateRange(@Param("driverId") Long driverId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);
}
