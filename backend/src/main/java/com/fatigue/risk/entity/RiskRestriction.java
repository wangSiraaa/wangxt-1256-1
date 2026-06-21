package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Integer getRestrictionStatus() {
        return restrictionStatus;
    }

    public void setRestrictionStatus(Integer restrictionStatus) {
        this.restrictionStatus = restrictionStatus;
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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
