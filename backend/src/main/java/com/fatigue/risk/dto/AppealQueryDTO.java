package com.fatigue.risk.dto;

import com.fatigue.risk.common.PageQuery;
import lombok.Data;

@Data
public class AppealQueryDTO extends PageQuery {
    private Long driverId;

    private Long restrictionId;

    private Integer appealStatus;

    private Integer materialComplete;

    private String appealNo;
}
