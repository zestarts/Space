CREATE DATABASE IF NOT EXISTS community_health
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE community_health;

DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS health_profiles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    phone       VARCHAR(20)     NOT NULL UNIQUE,
    real_name   VARCHAR(50)     DEFAULT NULL,
    role_code   VARCHAR(20)     NOT NULL DEFAULT 'USER',
    status      TINYINT         NOT NULL DEFAULT 1,
    avatar      VARCHAR(255)    DEFAULT NULL,
    version     INT             NOT NULL DEFAULT 0,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_role (role_code),
    INDEX idx_users_status (status),
    INDEX idx_users_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE health_profiles (
    id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT          NOT NULL UNIQUE,
    age              INT             DEFAULT NULL,
    gender           VARCHAR(10)     DEFAULT NULL,
    occupation       VARCHAR(100)    DEFAULT NULL,
    living_condition VARCHAR(50)     DEFAULT NULL,
    disability_level VARCHAR(20)     DEFAULT NULL,
    chronic_diseases TEXT            DEFAULT NULL,
    blood_pressure   VARCHAR(20)    DEFAULT NULL,
    blood_sugar      DECIMAL(5,2)   DEFAULT NULL,
    heart_rate       INT             DEFAULT NULL,
    height           DECIMAL(5,2)   DEFAULT NULL,
    weight           DECIMAL(5,2)   DEFAULT NULL,
    version          INT             NOT NULL DEFAULT 0,
    created_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE roles (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_code   VARCHAR(20)     NOT NULL UNIQUE,
    role_name   VARCHAR(50)     NOT NULL,
    description VARCHAR(200)    DEFAULT NULL,
    version     INT             NOT NULL DEFAULT 0,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE permissions (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    module          VARCHAR(50)     NOT NULL,
    action          VARCHAR(50)     NOT NULL,
    permission_code VARCHAR(100)    NOT NULL UNIQUE,
    description     VARCHAR(200)    DEFAULT NULL,
    version         INT             NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE role_permissions (
    id              BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_id         BIGINT  NOT NULL,
    permission_id   BIGINT  NOT NULL,
    version         INT     NOT NULL DEFAULT 0,
    UNIQUE KEY uk_role_perm (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================== SEED DATA ==================

-- Roles
INSERT INTO roles (role_code, role_name, description) VALUES
('SUPER_ADMIN', '超级管理员', '全系统权限，可管理所有角色和用户'),
('ADMIN', '普通管理员', '仅管理所辖范围数据'),
('USER', '普通用户', '服务对象端基础功能');

-- Permissions (module-based)
INSERT INTO permissions (module, action, permission_code, description) VALUES
('alert', 'view', 'alert:view', '查看预警信息'),
('alert', 'edit', 'alert:edit', '编辑预警规则'),
('alert', 'resolve', 'alert:resolve', '闭环处理预警'),
('task', 'view', 'task:view', '查看任务列表'),
('task', 'assign', 'task:assign', '指派/领取任务'),
('task', 'close', 'task:close', '关闭/完成任务'),
('dashboard', 'view', 'dashboard:view', '查看数据看板'),
('dashboard', 'export', 'dashboard:export', '导出统计数据'),
('user', 'view', 'user:view', '查看用户列表'),
('user', 'edit', 'user:edit', '编辑用户信息'),
('user', 'delete', 'user:delete', '删除用户'),
('system', 'config', 'system:config:view', '查看系统配置'),
('system', 'config_edit', 'system:config:edit', '修改系统配置');

-- Role-Permission mappings
-- SUPER_ADMIN: all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT 1, id FROM permissions;
-- ADMIN: alert:view, task:view/assign/close, dashboard:view, user:view
INSERT INTO role_permissions (role_id, permission_id)
SELECT 2, id FROM permissions WHERE permission_code IN ('alert:view', 'task:view', 'task:assign', 'task:close', 'dashboard:view', 'user:view');
-- USER: alert:view (own), task:view (own)
INSERT INTO role_permissions (role_id, permission_id)
SELECT 3, id FROM permissions WHERE permission_code IN ('alert:view', 'task:view');

-- Test users (password is BCrypt hash of "123456")
INSERT INTO users (username, password, phone, real_name, role_code, status) VALUES
('admin', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000001', '系统管理员', 'SUPER_ADMIN', 1),
('community_admin', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000002', '社区管理员', 'ADMIN', 1),
('zhangsan', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000003', '张三', 'USER', 1),
('lisi', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000004', '李四', 'USER', 1),
('wangwu', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000005', '王五', 'USER', 1),
('laonian', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000006', '赵六', 'USER', 1),
('test_disabled', '$2b$10$JDnqSrzvtQQzIz6sgOJYEu0sGbWj/bhdWNLZFHmKGO0KNbx96Y/zC', '13800000007', '测试禁用', 'USER', 0);

-- Health profiles for test users
INSERT INTO health_profiles (user_id, age, gender, occupation, living_condition, disability_level, chronic_diseases, blood_pressure, blood_sugar, heart_rate, height, weight) VALUES
(3, 35, 'male', 'sedentary_office', 'with_family', NULL, '颈椎病', '130/85', 5.8, 72, 175.0, 78.0),
(4, 68, 'male', 'retired', 'alone', '二级', '高血压,糖尿病', '150/95', 7.2, 88, 168.0, 65.0),
(5, 28, 'female', 'shift_worker', 'alone', NULL, NULL, '110/70', 5.1, 68, 162.0, 52.0),
(6, 72, 'male', 'retired', 'alone', '一级', '高血压,冠心病,关节炎', '160/100', 8.5, 95, 170.0, 60.0);

SELECT '=== Database initialized successfully ===' AS status;
SELECT COUNT(*) AS user_count FROM users;
SELECT COUNT(*) AS role_count FROM roles;
SELECT COUNT(*) AS permission_count FROM permissions;