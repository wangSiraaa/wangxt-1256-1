package com.fatigue.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fatigue.risk.common.PageQuery;
import com.fatigue.risk.entity.SafetyReview;
import com.fatigue.risk.vo.SafetyReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SafetyReviewMapper extends BaseMapper<SafetyReview> {

    @Select("<script>" +
            "SELECT s.*, d.driver_code, d.driver_name, a.appeal_no, r.restriction_no " +
            "FROM safety_review s " +
            "LEFT JOIN driver d ON s.driver_id = d.id " +
            "LEFT JOIN driver_appeal a ON s.appeal_id = a.id " +
            "LEFT JOIN risk_restriction r ON s.restriction_id = r.id " +
            "WHERE s.deleted = 0 " +
            "ORDER BY s.create_time DESC" +
            "</script>")
    IPage<SafetyReviewVO> selectReviewPage(Page<SafetyReviewVO> page, @Param("query") PageQuery query);

    @Select("SELECT s.*, d.driver_code, d.driver_name, a.appeal_no, r.restriction_no " +
            "FROM safety_review s " +
            "LEFT JOIN driver d ON s.driver_id = d.id " +
            "LEFT JOIN driver_appeal a ON s.appeal_id = a.id " +
            "LEFT JOIN risk_restriction r ON s.restriction_id = r.id " +
            "WHERE s.id = #{id} AND s.deleted = 0")
    SafetyReviewVO selectReviewDetail(@Param("id") Long id);
}
