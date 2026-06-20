package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("driver_appeal")
public class DriverAppeal {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("restriction_id")
    private Long restrictionId;

    @TableField("driver_id")
    private Long driverId;

    @TableField("appeal_no")
    private String appealNo;

    @TableField("appeal_reason")
    private String appealReason;

    @TableField("rest_proof_url")
    private String restProofUrl;

    @TableField("rest_proof_desc")
    private String restProofDesc;

    @TableField("rest_start_time")
    private LocalDateTime restStartTime;

    @TableField("rest_end_time")
    private LocalDateTime restEndTime;

    @TableField("rest_minutes")
    private Integer restMinutes;

    @TableField("material_complete")
    private Integer materialComplete;

    @TableField("incomplete_reason")
    private String incompleteReason;

    @TableField("appeal_status")
    private Integer appealStatus;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("auditor_id")
    private Long auditorId;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("audit_remark")
    private String auditRemark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
