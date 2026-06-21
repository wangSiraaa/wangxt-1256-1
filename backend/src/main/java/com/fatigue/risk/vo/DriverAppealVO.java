package com.fatigue.risk.vo;

import java.time.LocalDateTime;

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
    private java.util.List<AppealMaterialRecordVO> materialRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(Long restrictionId) {
        this.restrictionId = restrictionId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getAppealNo() {
        return appealNo;
    }

    public void setAppealNo(String appealNo) {
        this.appealNo = appealNo;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public String getRestProofUrl() {
        return restProofUrl;
    }

    public void setRestProofUrl(String restProofUrl) {
        this.restProofUrl = restProofUrl;
    }

    public String getRestProofDesc() {
        return restProofDesc;
    }

    public void setRestProofDesc(String restProofDesc) {
        this.restProofDesc = restProofDesc;
    }

    public LocalDateTime getRestStartTime() {
        return restStartTime;
    }

    public void setRestStartTime(LocalDateTime restStartTime) {
        this.restStartTime = restStartTime;
    }

    public LocalDateTime getRestEndTime() {
        return restEndTime;
    }

    public void setRestEndTime(LocalDateTime restEndTime) {
        this.restEndTime = restEndTime;
    }

    public Integer getRestMinutes() {
        return restMinutes;
    }

    public void setRestMinutes(Integer restMinutes) {
        this.restMinutes = restMinutes;
    }

    public Integer getMaterialComplete() {
        return materialComplete;
    }

    public void setMaterialComplete(Integer materialComplete) {
        this.materialComplete = materialComplete;
    }

    public String getIncompleteReason() {
        return incompleteReason;
    }

    public void setIncompleteReason(String incompleteReason) {
        this.incompleteReason = incompleteReason;
    }

    public Integer getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(Integer appealStatus) {
        this.appealStatus = appealStatus;
    }

    public String getAppealStatusDesc() {
        return appealStatusDesc;
    }

    public void setAppealStatusDesc(String appealStatusDesc) {
        this.appealStatusDesc = appealStatusDesc;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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

    public String getRestrictionNo() {
        return restrictionNo;
    }

    public void setRestrictionNo(String restrictionNo) {
        this.restrictionNo = restrictionNo;
    }

    public java.util.List<AppealMaterialRecordVO> getMaterialRecords() {
        return materialRecords;
    }

    public void setMaterialRecords(java.util.List<AppealMaterialRecordVO> materialRecords) {
        this.materialRecords = materialRecords;
    }
}
