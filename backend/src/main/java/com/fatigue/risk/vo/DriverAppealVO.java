package com.fatigue.risk.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DriverAppealVO {
    private Long id;
    private Long restrictionId;
    private Long driverId;
    private String appealNo;
    private String appealReason;
    private String restProofUrl;
    private String restProofDesc;
    private LocalDateTime restStartTime;
    private LocalDateTime restEndTime;
    private Integer restMinutes;
    private Integer materialComplete;
    private String incompleteReason;
    private Integer appealStatus;
    private String appealStatusDesc;
    private LocalDateTime submitTime;
    private Long auditorId;
    private LocalDateTime auditTime;
    private String auditRemark;
    private LocalDateTime createTime;

    private String driverCode;
    private String driverName;
    private String driverPhone;
    private String restrictionNo;
}
