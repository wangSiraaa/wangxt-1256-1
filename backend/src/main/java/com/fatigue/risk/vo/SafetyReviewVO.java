package com.fatigue.risk.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SafetyReviewVO {
    private Long id;
    private Long appealId;
    private Long restrictionId;
    private Long driverId;
    private String reviewNo;
    private Long reviewerId;
    private String reviewerName;
    private Integer reviewResult;
    private String reviewResultDesc;
    private String reviewRemark;
    private String verifyEvidence;
    private Integer restVerifiedMinutes;
    private LocalDateTime reviewTime;
    private Integer keepEvidence;
    private LocalDateTime createTime;

    private String driverCode;
    private String driverName;
    private String appealNo;
    private String restrictionNo;
}
