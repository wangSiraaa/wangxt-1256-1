package com.fatigue.risk.dto;

import com.fatigue.risk.common.PageQuery;
import lombok.Data;

@Data
public class RestrictionQueryDTO extends PageQuery {
    private Long driverId;

    private Integer restrictionType;

    private Integer restrictionStatus;

    private Integer riskLevel;

    private String restrictionNo;
}
