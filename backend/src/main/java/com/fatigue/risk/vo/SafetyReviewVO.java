package com.fatigue.risk.vo;

import java.time.LocalDateTime;

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

    public String getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(String reviewNo) {
        this.reviewNo = reviewNo;
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

    public Integer getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Integer reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getReviewResultDesc() {
        return reviewResultDesc;
    }

    public void setReviewResultDesc(String reviewResultDesc) {
        this.reviewResultDesc = reviewResultDesc;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
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

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Integer getKeepEvidence() {
        return keepEvidence;
    }

    public void setKeepEvidence(Integer keepEvidence) {
        this.keepEvidence = keepEvidence;
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

    public String getAppealNo() {
        return appealNo;
    }

    public void setAppealNo(String appealNo) {
        this.appealNo = appealNo;
    }

    public String getRestrictionNo() {
        return restrictionNo;
    }

    public void setRestrictionNo(String restrictionNo) {
        this.restrictionNo = restrictionNo;
    }
}
