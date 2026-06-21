package com.fatigue.risk.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AppealSupplementDTO {
    @NotNull(message = "申诉ID不能为空")
    private Long appealId;

    @NotNull(message = "司机ID不能为空")
    private Long driverId;

    private String restProofUrl;

    private String restProofDesc;

    private LocalDateTime restStartTime;

    private LocalDateTime restEndTime;

    private Integer restMinutes;

    private String appealReason;

    public Long getAppealId() {
        return appealId;
    }

    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getRestProofUrl() {
        return restProofUrl;
    }

    public void setRestProofUrl(String restProofUrl) {
        this.restProofUrl = restProofUrl;
    }

    public String getRestProofDesc() {
        return restProofDesc;
    }

    public void setRestProofDesc(String restProofDesc) {
        this.restProofDesc = restProofDesc;
    }

    public LocalDateTime getRestStartTime() {
        return restStartTime;
    }

    public void setRestStartTime(LocalDateTime restStartTime) {
        this.restStartTime = restStartTime;
    }

    public LocalDateTime getRestEndTime() {
        return restEndTime;
    }

    public void setRestEndTime(LocalDateTime restEndTime) {
        this.restEndTime = restEndTime;
    }

    public Integer getRestMinutes() {
        return restMinutes;
    }

    public void setRestMinutes(Integer restMinutes) {
        this.restMinutes = restMinutes;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }
}
