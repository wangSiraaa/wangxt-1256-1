package com.fatigue.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.risk.dto.RestrictionQueryDTO;
import com.fatigue.risk.entity.RiskRestriction;
import com.fatigue.risk.vo.RiskRestrictionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RiskRestrictionMapper extends BaseMapper<RiskRestriction> {

    @Select("<script>" +
            "SELECT r.*, d.driver_code, d.driver_name, d.phone AS driver_phone " +
            "FROM risk_restriction r LEFT JOIN driver d ON r.driver_id = d.id " +
            "WHERE r.deleted = 0 " +
            "<if test='query.driverId != null'>AND r.driver_id = #{query.driverId}</if>" +
            "<if test='query.restrictionType != null'>AND r.restriction_type = #{query.restrictionType}</if>" +
            "<if test='query.restrictionStatus != null'>AND r.restriction_status = #{query.restrictionStatus}</if>" +
            "<if test='query.riskLevel != null'>AND r.risk_level = #{query.riskLevel}</if>" +
            "<if test='query.restrictionNo != null and query.restrictionNo != \"\"'>AND r.restriction_no LIKE CONCAT('%', #{query.restrictionNo}, '%')</if>" +
            "ORDER BY r.create_time DESC" +
            "</script>")
    IPage<RiskRestrictionVO> selectRestrictionPage(Page<RiskRestrictionVO> page,
                                                    @Param("query") RestrictionQueryDTO query);

    @Select("SELECT r.*, d.driver_code, d.driver_name, d.phone AS driver_phone " +
            "FROM risk_restriction r LEFT JOIN driver d ON r.driver_id = d.id " +
            "WHERE r.id = #{id} AND r.deleted = 0")
    RiskRestrictionVO selectRestrictionDetail(@Param("id") Long id);

    @Update("UPDATE risk_restriction SET restriction_status = #{status}, update_time = NOW() " +
            "WHERE id = #{id} AND deleted = 0")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT COUNT(1) FROM risk_restriction " +
            "WHERE driver_id = #{driverId} AND restriction_status IN (1, 2) AND deleted = 0")
    int countActiveRestriction(@Param("driverId") Long driverId);
}
