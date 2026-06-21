package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotNull;

public class MaterialCheckDTO {
    @NotNull(message = "申诉ID不能为空")
    private Long appealId;

    @NotNull(message = "材料完整性标记不能为空")
    private Integer materialComplete;

    private String incompleteReason;

    private Long auditorId;

    private String auditRemark;

    public Long getAppealId() {
        return appealId;
    }

    public void setAppealId(Long appealId) {
        this.appealId = appealId;
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

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }
}
