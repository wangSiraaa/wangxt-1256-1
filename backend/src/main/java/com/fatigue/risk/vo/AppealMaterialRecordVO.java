package com.fatigue.risk.vo;

import java.time.LocalDateTime;

public class AppealMaterialRecordVO {
    private Long id;
    private Long appealId;
    private Integer recordType;
    private String recordTypeDesc;
    private String restProofUrl;
    private String restProofDesc;
    private LocalDateTime restStartTime;
    private LocalDateTime restEndTime;
    private Integer restMinutes;
    private String appealReason;
    private Long operatorId;
    private String operatorName;
    private String operateRemark;
    private LocalDateTime submitTime;
    private LocalDateTime createTime;
    private String defaultOperatorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppealId() {
        return appealId;
    }

    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getRecordTypeDesc() {
        return recordTypeDesc;
    }

    public void setRecordTypeDesc(String recordTypeDesc) {
        this.recordTypeDesc = recordTypeDesc;
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

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperateRemark() {
        return operateRemark;
    }

    public void setOperateRemark(String operateRemark) {
        this.operateRemark = operateRemark;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getDefaultOperatorName() {
        return defaultOperatorName;
    }

    public void setDefaultOperatorName(String defaultOperatorName) {
        this.defaultOperatorName = defaultOperatorName;
    }
}
