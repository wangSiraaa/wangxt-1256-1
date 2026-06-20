package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialCheckDTO {
    @NotNull(message = "申诉ID不能为空")
    private Long appealId;

    @NotNull(message = "材料完整性标记不能为空")
    private Integer materialComplete;

    private String incompleteReason;

    private Long auditorId;

    private String auditRemark;
}
