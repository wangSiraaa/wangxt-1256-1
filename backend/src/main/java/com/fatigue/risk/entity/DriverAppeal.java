package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("driver_appeal")
public class DriverAppeal {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("restriction_id")
    private Long restrictionId;

    @TableField("driver_id")
    private Long driverId;

    @TableField("appeal_no")
    private String appealNo;

    @TableField("appeal_reason")
    private String appealReason;

    @TableField("rest_proof_url")
    private String restProofUrl;

    @TableField("rest_proof_desc")
    private String restProofDesc;

    @TableField("rest_start_time")
    private LocalDateTime restStartTime;

    @TableField("rest_end_time")
    private LocalDateTime restEndTime;

    @TableField("rest_minutes")
    private Integer restMinutes;

    @TableField("material_complete")
    private Integer materialComplete;

    @TableField("incomplete_reason")
    private String incompleteReason;

    @TableField("appeal_status")
    private Integer appealStatus;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("auditor_id")
    private Long auditorId;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("audit_remark")
    private String auditRemark;

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
