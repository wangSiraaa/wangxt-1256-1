package com.fatigue.risk.dto;

import com.fatigue.risk.common.PageQuery;

public class AppealQueryDTO extends PageQuery {
    private Long driverId;

    private Long restrictionId;

    private Integer appealStatus;

    private Integer materialComplete;

    private String appealNo;

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(Long restrictionId) {
        this.restrictionId = restrictionId;
    }

    public Integer getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(Integer appealStatus) {
        this.appealStatus = appealStatus;
    }

    public Integer getMaterialComplete() {
        return materialComplete;
    }

    public void setMaterialComplete(Integer materialComplete) {
        this.materialComplete = materialComplete;
    }

    public String getAppealNo() {
        return appealNo;
    }

    public void setAppealNo(String appealNo) {
        this.appealNo = appealNo;
    }
}
