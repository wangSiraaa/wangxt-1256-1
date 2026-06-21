package com.fatigue.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fatigue.risk.entity.AppealMaterialRecord;
import com.fatigue.risk.vo.AppealMaterialRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppealMaterialRecordMapper extends BaseMapper<AppealMaterialRecord> {

    @Select("SELECT r.*, d.driver_name AS default_operator_name " +
            "FROM appeal_material_record r " +
            "LEFT JOIN driver d ON r.operator_id = d.id " +
            "WHERE r.appeal_id = #{appealId} AND r.deleted = 0 " +
            "ORDER BY r.submit_time ASC")
    List<AppealMaterialRecordVO> selectByAppealId(@Param("appealId") Long appealId);
}
