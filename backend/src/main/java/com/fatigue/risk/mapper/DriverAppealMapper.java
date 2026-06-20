package com.fatigue.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.risk.dto.AppealQueryDTO;
import com.fatigue.risk.entity.DriverAppeal;
import com.fatigue.risk.vo.DriverAppealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DriverAppealMapper extends BaseMapper<DriverAppeal> {

    @Select("<script>" +
            "SELECT a.*, d.driver_code, d.driver_name, d.phone AS driver_phone, r.restriction_no " +
            "FROM driver_appeal a " +
            "LEFT JOIN driver d ON a.driver_id = d.id " +
            "LEFT JOIN risk_restriction r ON a.restriction_id = r.id " +
            "WHERE a.deleted = 0 " +
            "<if test='query.driverId != null'>AND a.driver_id = #{query.driverId}</if>" +
            "<if test='query.restrictionId != null'>AND a.restriction_id = #{query.restrictionId}</if>" +
            "<if test='query.appealStatus != null'>AND a.appeal_status = #{query.appealStatus}</if>" +
            "<if test='query.materialComplete != null'>AND a.material_complete = #{query.materialComplete}</if>" +
            "<if test='query.appealNo != null and query.appealNo != \"\"'>AND a.appeal_no LIKE CONCAT('%', #{query.appealNo}, '%')</if>" +
            "ORDER BY a.create_time DESC" +
            "</script>")
    IPage<DriverAppealVO> selectAppealPage(Page<DriverAppealVO> page,
                                            @Param("query") AppealQueryDTO query);

    @Select("SELECT a.*, d.driver_code, d.driver_name, d.phone AS driver_phone, r.restriction_no " +
            "FROM driver_appeal a " +
            "LEFT JOIN driver d ON a.driver_id = d.id " +
            "LEFT JOIN risk_restriction r ON a.restriction_id = r.id " +
            "WHERE a.id = #{id} AND a.deleted = 0")
    DriverAppealVO selectAppealDetail(@Param("id") Long id);
}
