package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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
}
