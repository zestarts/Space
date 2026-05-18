package com.community.health.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private String realName;
    private String roleCode;
    private String roleName;
    private Integer status;
    private String avatar;
    private LocalDateTime createdAt;
}