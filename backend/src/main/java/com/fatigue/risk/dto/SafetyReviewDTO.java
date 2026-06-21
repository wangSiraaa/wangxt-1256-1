package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SafetyReviewDTO {
    @NotNull(message = "申诉ID不能为空")
    private Long appealId;

    @NotNull(message = "限制ID不能为空")
    private Long restrictionId;

    @NotNull(message = "复核结果不能为空")
    private Integer reviewResult;

    @NotBlank(message = "复核意见不能为空")
    private String reviewRemark;

    @NotNull(message = "安全专员ID不能为空")
    private Long reviewerId;

    @NotBlank(message = "安全专员姓名不能为空")
    private String reviewerName;

    private String verifyEvidence;

    private Integer restVerifiedMinutes;

    private Integer keepEvidence = 1;

    public Long getAppealId() {
        return appealId;
    }

    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public Long getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(Long restrictionId) {
        this.restrictionId = restrictionId;
    }

    public Integer getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Integer reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getVerifyEvidence() {
        return verifyEvidence;
    }

    public void setVerifyEvidence(String verifyEvidence) {
        this.verifyEvidence = verifyEvidence;
    }

    public Integer getRestVerifiedMinutes() {
        return restVerifiedMinutes;
    }

    public void setRestVerifiedMinutes(Integer restVerifiedMinutes) {
        this.restVerifiedMinutes = restVerifiedMinutes;
    }

    public Integer getKeepEvidence() {
        return keepEvidence;
    }

    public void setKeepEvidence(Integer keepEvidence) {
        this.keepEvidence = keepEvidence;
    }
}
