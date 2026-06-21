package com.fatigue.risk.dto;

import com.fatigue.risk.common.PageQuery;

public class RestrictionQueryDTO extends PageQuery {
    private Long driverId;

    private Integer restrictionType;

    private Integer restrictionStatus;

    private Integer riskLevel;

    private String restrictionNo;

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getRestrictionType() {
        return restrictionType;
    }

    public void setRestrictionType(Integer restrictionType) {
        this.restrictionType = restrictionType;
    }

    public Integer getRestrictionStatus() {
        return restrictionStatus;
    }

    public void setRestrictionStatus(Integer restrictionStatus) {
        this.restrictionStatus = restrictionStatus;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRestrictionNo() {
        return restrictionNo;
    }

    public void setRestrictionNo(String restrictionNo) {
        this.restrictionNo = restrictionNo;
    }
}
