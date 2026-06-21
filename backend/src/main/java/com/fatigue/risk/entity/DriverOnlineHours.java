package com.fatigue.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
