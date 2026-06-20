package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}
