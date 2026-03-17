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
    industry VARCHAR(50) COMMENT '行业：快递物流/专线运输/仓储服务/跨境物流/冷链物流/物流设备',
    email VARCHAR(100),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品表（新增）
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    industry VARCHAR(50) COMMENT '行业：快递物流/专线运输/仓储服务/跨境物流/冷链物流/物流设备',
    category VARCHAR(50),
    view_count INT DEFAULT 0,
    trial_count INT DEFAULT 0,
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

-- 插入示例产品数据
INSERT INTO products (name, description, industry, category, view_count, trial_count) VALUES
-- 快递物流
('智能快递柜系统', '末端配送智能解决方案，支持人脸识别、扫码取件', '快递物流', '智能设备', 1520, 320),
('快递轨迹追踪平台', '全链路物流可视化，实时追踪包裹位置', '快递物流', '软件系统', 980, 210),
('智能分拣系统', '自动分拣提高效率，支持多类型包裹', '快递物流', '硬件设备', 750, 180),
-- 专线运输
('专线物流管理系统', '专线运输全流程管理，订单跟踪', '专线运输', '软件系统', 1100, 280),
('车辆调度平台', '智能调度降低空驶率，节约成本', '专线运输', '软件系统', 890, 195),
-- 仓储服务
('WMS仓储管理系统', '智能化仓储解决方案，支持多仓库', '仓储服务', '软件系统', 2300, 520),
('库存预警系统', '实时库存监控预警，防止缺货', '仓储服务', '软件系统', 1650, 380),
-- 跨境物流
('跨境报关系统', '一键通关解决方案，支持多口岸', '跨境物流', '软件系统', 1400, 310),
('海外仓管理', '海外仓储配送一体化服务', '跨境物流', '软件系统', 980, 220),
-- 冷链物流
('冷链温控系统', '全程温湿度监控，智能报警', '冷链物流', '硬件设备', 850, 190),
('冷链追溯平台', '食品医药全程追溯，保障安全', '冷链物流', '软件系统', 720, 160),
-- 物流设备
('AGV搬运机器人', '无人化智能搬运，提高效率', '物流设备', '硬件设备', 1800, 420),
('智能货架系统', '自动化仓储货架，节约空间', '物流设备', '硬件设备', 1250, 290),
-- 热门通用
('TMS运输管理系统', '运输全流程数字化管理', '物流', '软件系统', 3500, 850),
('物流大数据分析平台', '智能决策支持，数据可视化', '物流', '软件系统', 2800, 620),
('智能调度系统', 'AI优化路线规划，降低成本', '物流', '软件系统', 2200, 480),
('电子合同签约平台', '物流电子合同，在线签约', '物流', '软件系统', 1650, 360);

-- 创建索引
CREATE INDEX idx_trials_user ON trials(user_id);
CREATE INDEX idx_trials_status ON trials(status);
CREATE INDEX idx_feedback_trial ON trial_feedback(trial_id);

SELECT '数据库简化完成！' AS message;
