package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("risk_restriction")
public class RiskRestriction {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("driver_id")
    private Long driverId;

    @TableField("restriction_no")
    private String restrictionNo;

    @TableField("restriction_type")
    private Integer restrictionType;

    @TableField("trigger_reason")
    private String triggerReason;

    @TableField("trigger_value")
    private BigDecimal triggerValue;

    @TableField("threshold_value")
    private BigDecimal thresholdValue;

    @TableField("risk_level")
    private Integer riskLevel;

    @TableField("restriction_status")
    private Integer restrictionStatus;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("appeal_id")
    private Long appealId;

    @TableField("review_id")
    private Long reviewId;

    @TableField("evidence_data")
    private String evidenceData;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
