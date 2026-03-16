-- 产品体验平台 v2.0 简化版 - 只保留首页、AI顾问、试用工作台

-- 创建数据库
CREATE DATABASE IF NOT EXISTS demo_platform DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE demo_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100),
    role VARCHAR(20) NOT NULL COMMENT 'BUYER/PROVIDER/ADMIN',
    industry VARCHAR(50),
    email VARCHAR(100),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 试用记录表（简化版，移除products/solutions外键）
CREATE TABLE IF NOT EXISTS trials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    environment_url VARCHAR(500),
    test_data TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/RUNNING/COMPLETED/EXPIRED',
    start_time DATETIME,
    end_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 试用反馈表
CREATE TABLE IF NOT EXISTS trial_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trial_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT COMMENT '1-5',
    feedback TEXT,
    issues TEXT,
    purchase_intent VARCHAR(20) COMMENT 'NONE/INTERESTED/PENDING/PURCHASED',
    status VARCHAR(30) DEFAULT 'SUBMITTED' COMMENT 'SUBMITTED/VIEWED/REPLIED/CLOSED',
    provider_reply TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (trial_id) REFERENCES trials(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入示例数据
INSERT INTO users (username, name, role, industry, email) VALUES
('buyer1', '张总', 'BUYER', '物流', 'zhang@example.com'),
('buyer2', '李总', 'BUYER', '电商', 'li@example.com'),
('provider1', '王经理', 'PROVIDER', 'IT', 'wang@example.com'),
('admin1', '系统管理员', 'ADMIN', '互联网', 'admin@example.com');

-- 创建索引
CREATE INDEX idx_trials_user ON trials(user_id);
CREATE INDEX idx_trials_status ON trials(status);
CREATE INDEX idx_feedback_trial ON trial_feedback(trial_id);

SELECT '数据库简化完成！' AS message;
