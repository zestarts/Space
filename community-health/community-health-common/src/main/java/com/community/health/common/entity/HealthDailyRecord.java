package com.community.health.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("health_daily_records")
public class HealthDailyRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate recordDate;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private BigDecimal bloodSugar;
    private Integer heartRate;
    private BigDecimal sleepHours;
    private String sleepQuality;
    private String mood;
    private String symptoms;
    private BigDecimal weight;
    private Integer exerciseMinutes;
    private String dietQuality;
    private String notes;
    private Integer hasAlert;
    private String alertLevel;
    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}