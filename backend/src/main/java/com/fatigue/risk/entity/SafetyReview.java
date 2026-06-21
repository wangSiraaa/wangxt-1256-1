package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("safety_review")
public class SafetyReview {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("appeal_id")
    private Long appealId;

    @TableField("restriction_id")
    private Long restrictionId;

    @TableField("driver_id")
    private Long driverId;

    @TableField("review_no")
    private String reviewNo;

    @TableField("reviewer_id")
    private Long reviewerId;

    @TableField("reviewer_name")
    private String reviewerName;

    @TableField("review_result")
    private Integer reviewResult;

    @TableField("review_remark")
    private String reviewRemark;

    @TableField("verify_evidence")
    private String verifyEvidence;

    @TableField("rest_verified_minutes")
    private Integer restVerifiedMinutes;

    @TableField("review_time")
    private LocalDateTime reviewTime;

    @TableField("keep_evidence")
    private Integer keepEvidence;

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
