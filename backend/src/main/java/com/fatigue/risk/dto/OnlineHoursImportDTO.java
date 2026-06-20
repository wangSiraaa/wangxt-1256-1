package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OnlineHoursImportDTO {
    @NotNull(message = "司机ID不能为空")
    private Long driverId;

    @NotNull(message = "工作日期不能为空")
    private LocalDate workDate;

    @NotNull(message = "上线时间不能为空")
    private LocalDateTime onlineStart;

    @NotNull(message = "下线时间不能为空")
    private LocalDateTime onlineEnd;

    private Integer onlineMinutes;

    private Integer workMinutes;

    private Integer restMinutes;

    private Integer orderCount;

    private String batchNo;
}
