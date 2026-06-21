package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("appeal_material_record")
public class AppealMaterialRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("appeal_id")
    private Long appealId;

    @TableField("record_type")
    private Integer recordType;

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

    @TableField("appeal_reason")
    private String appealReason;

    @TableField("operator_id")
    private Long operatorId;

    @TableField("operator_name")
    private String operatorName;

    @TableField("operate_remark")
    private String operateRemark;

    @TableField("submit_time")
    private LocalDateTime submitTime;

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
