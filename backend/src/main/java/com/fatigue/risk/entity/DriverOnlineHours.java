package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("driver_online_hours")
public class DriverOnlineHours {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("driver_id")
    private Long driverId;

    @TableField("work_date")
    private LocalDate workDate;

    @TableField("online_start")
    private LocalDateTime onlineStart;

    @TableField("online_end")
    private LocalDateTime onlineEnd;

    @TableField("online_minutes")
    private Integer onlineMinutes;

    @TableField("work_minutes")
    private Integer workMinutes;

    @TableField("rest_minutes")
    private Integer restMinutes;

    @TableField("order_count")
    private Integer orderCount;

    @TableField("batch_no")
    private String batchNo;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
