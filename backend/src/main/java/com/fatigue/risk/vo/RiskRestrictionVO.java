package com.fatigue.risk.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getRestrictionNo() {
        return restrictionNo;
    }

    public void setRestrictionNo(String restrictionNo) {
        this.restrictionNo = restrictionNo;
    }

    public Integer getRestrictionType() {
        return restrictionType;
    }

    public void setRestrictionType(Integer restrictionType) {
        this.restrictionType = restrictionType;
    }

    public String getRestrictionTypeDesc() {
        return restrictionTypeDesc;
    }

    public void setRestrictionTypeDesc(String restrictionTypeDesc) {
        this.restrictionTypeDesc = restrictionTypeDesc;
    }

    public String getTriggerReason() {
        return triggerReason;
    }

    public void setTriggerReason(String triggerReason) {
        this.triggerReason = triggerReason;
    }

    public BigDecimal getTriggerValue() {
        return triggerValue;
    }

    public void setTriggerValue(BigDecimal triggerValue) {
        this.triggerValue = triggerValue;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskLevelDesc() {
        return riskLevelDesc;
    }

    public void setRiskLevelDesc(String riskLevelDesc) {
        this.riskLevelDesc = riskLevelDesc;
    }

    public Integer getRestrictionStatus() {
        return restrictionStatus;
    }

    public void setRestrictionStatus(Integer restrictionStatus) {
        this.restrictionStatus = restrictionStatus;
    }

    public String getRestrictionStatusDesc() {
        return restrictionStatusDesc;
    }

    public void setRestrictionStatusDesc(String restrictionStatusDesc) {
        this.restrictionStatusDesc = restrictionStatusDesc;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getAppealId() {
        return appealId;
    }

    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getEvidenceData() {
        return evidenceData;
    }

    public void setEvidenceData(String evidenceData) {
        this.evidenceData = evidenceData;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
}
