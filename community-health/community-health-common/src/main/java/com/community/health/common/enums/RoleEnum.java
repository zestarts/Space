package com.community.health.common.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),
    ADMIN("ADMIN", "普通管理员"),
    USER("USER", "普通用户");

    private final String code;
    private final String name;

    RoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RoleEnum fromCode(String code) {
        for (RoleEnum r : values()) {
            if (r.code.equals(code)) return r;
        }
        return USER;
    }
}