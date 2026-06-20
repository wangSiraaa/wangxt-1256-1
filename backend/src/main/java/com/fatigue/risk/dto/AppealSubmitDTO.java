package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppealSubmitDTO {
    @NotNull(message = "限制记录ID不能为空")
    private Long restrictionId;

    @NotNull(message = "司机ID不能为空")
    private Long driverId;

    @NotBlank(message = "申诉理由不能为空")
    private String appealReason;

    private String restProofUrl;

    private String restProofDesc;

    private LocalDateTime restStartTime;

    private LocalDateTime restEndTime;

    private Integer restMinutes;
}
