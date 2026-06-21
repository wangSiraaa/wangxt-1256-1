package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalDateTime getOnlineStart() {
        return onlineStart;
    }

    public void setOnlineStart(LocalDateTime onlineStart) {
        this.onlineStart = onlineStart;
    }

    public LocalDateTime getOnlineEnd() {
        return onlineEnd;
    }

    public void setOnlineEnd(LocalDateTime onlineEnd) {
        this.onlineEnd = onlineEnd;
    }

    public Integer getOnlineMinutes() {
        return onlineMinutes;
    }

    public void setOnlineMinutes(Integer onlineMinutes) {
        this.onlineMinutes = onlineMinutes;
    }

    public Integer getWorkMinutes() {
        return workMinutes;
    }

    public void setWorkMinutes(Integer workMinutes) {
        this.workMinutes = workMinutes;
    }

    public Integer getRestMinutes() {
        return restMinutes;
    }

    public void setRestMinutes(Integer restMinutes) {
        this.restMinutes = restMinutes;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
