package com.community.health.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("permissions")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String module;
    private String action;
    private String permissionCode;
    private String description;
}