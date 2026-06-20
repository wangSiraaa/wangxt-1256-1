package com.fatigue.risk.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RiskRestrictionVO {
    private Long id;
    private Long driverId;
    private String restrictionNo;
    private Integer restrictionType;
    private String restrictionTypeDesc;
    private String triggerReason;
    private BigDecimal triggerValue;
    private BigDecimal thresholdValue;
    private Integer riskLevel;
    private String riskLevelDesc;
    private Integer restrictionStatus;
    private String restrictionStatusDesc;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long appealId;
    private Long reviewId;
    private String evidenceData;
    private LocalDateTime createTime;

    private String driverCode;
    private String driverName;
    private String driverPhone;
}
