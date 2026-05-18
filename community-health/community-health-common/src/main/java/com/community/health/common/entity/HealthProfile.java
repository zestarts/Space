package com.community.health.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("health_profiles")
public class HealthProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer age;
    private String gender;
    private String occupation;
    private String livingCondition;
    private String disabilityLevel;
    private String chronicDiseases;
    private String bloodPressure;
    private BigDecimal bloodSugar;
    private Integer heartRate;
    private BigDecimal height;
    private BigDecimal weight;
    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}