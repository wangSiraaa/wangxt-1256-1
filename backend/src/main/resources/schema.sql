CREATE DATABASE IF NOT EXISTS fatigue_risk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fatigue_risk;

DROP TABLE IF EXISTS driver;
CREATE TABLE driver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    driver_code VARCHAR(32) NOT NULL UNIQUE COMMENT '司机工号',
    driver_name VARCHAR(64) NOT NULL COMMENT '司机姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    license_no VARCHAR(32) COMMENT '驾驶证号',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='司机信息表';

DROP TABLE IF EXISTS driver_online_hours;
CREATE TABLE driver_online_hours (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    driver_id BIGINT NOT NULL,
    work_date DATE NOT NULL COMMENT '工作日期',
    online_start DATETIME NOT NULL COMMENT '上线时间',
    online_end DATETIME NOT NULL COMMENT '下线时间',
    online_minutes INT NOT NULL COMMENT '在线时长（分钟）',
    work_minutes INT NOT NULL COMMENT '实际工作时长（分钟）',
    rest_minutes INT DEFAULT 0 COMMENT '休息时长（分钟）',
    order_count INT DEFAULT 0 COMMENT '接单数量',
    batch_no VARCHAR(64) COMMENT '导入批次号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_driver_date (driver_id, work_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='司机在线时长记录表';

DROP TABLE IF EXISTS risk_restriction;
CREATE TABLE risk_restriction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    driver_id BIGINT NOT NULL,
    restriction_no VARCHAR(64) NOT NULL UNIQUE COMMENT '限制单号',
    restriction_type TINYINT NOT NULL COMMENT '限制类型：1-连续接单超限 2-在线时长超限 3-连续工作超时',
    trigger_reason VARCHAR(512) NOT NULL COMMENT '触发原因',
    trigger_value DECIMAL(10,2) COMMENT '触发值',
    threshold_value DECIMAL(10,2) COMMENT '阈值',
    risk_level TINYINT DEFAULT 2 COMMENT '风险等级：1-低 2-中 3-高',
    restriction_status TINYINT DEFAULT 1 COMMENT '限制状态：1-限制中 2-申诉中 3-解除限制 4-维持限制',
    start_time DATETIME NOT NULL COMMENT '限制开始时间',
    end_time DATETIME COMMENT '限制结束时间',
    appeal_id BIGINT COMMENT '关联申诉ID',
    review_id BIGINT COMMENT '关联复核ID',
    evidence_data JSON COMMENT '原始风控依据数据（JSON格式，保留所有判定依据）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_driver_status (driver_id, restriction_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风控限制记录表';

DROP TABLE IF EXISTS driver_appeal;
CREATE TABLE driver_appeal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    restriction_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    appeal_no VARCHAR(64) NOT NULL UNIQUE COMMENT '申诉单号',
    appeal_reason TEXT NOT NULL COMMENT '申诉理由',
    rest_proof_url VARCHAR(512) COMMENT '休息证明文件URL',
    rest_proof_desc VARCHAR(256) COMMENT '休息证明说明',
    rest_start_time DATETIME COMMENT '休息开始时间',
    rest_end_time DATETIME COMMENT '休息结束时间',
    rest_minutes INT COMMENT '实际休息时长（分钟）',
    material_complete TINYINT DEFAULT 0 COMMENT '材料完整性：0-不完整 1-完整',
    incomplete_reason VARCHAR(256) COMMENT '材料不完整原因',
    appeal_status TINYINT DEFAULT 1 COMMENT '申诉状态：1-待审核 2-材料不完整 3-审核中 4-申诉通过 5-申诉驳回',
    submit_time DATETIME NOT NULL COMMENT '提交时间',
    auditor_id BIGINT COMMENT '初审人',
    audit_time DATETIME COMMENT '初审时间',
    audit_remark VARCHAR(512) COMMENT '初审意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_restriction (restriction_id),
    INDEX idx_driver (driver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='司机申诉记录表';

DROP TABLE IF EXISTS appeal_material_record;
CREATE TABLE appeal_material_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    appeal_id BIGINT NOT NULL COMMENT '申诉ID',
    record_type TINYINT NOT NULL COMMENT '记录类型：1-首次提交 2-补充材料 3-退回不完整',
    rest_proof_url VARCHAR(512) COMMENT '休息证明文件URL',
    rest_proof_desc VARCHAR(256) COMMENT '休息证明说明',
    rest_start_time DATETIME COMMENT '休息开始时间',
    rest_end_time DATETIME COMMENT '休息结束时间',
    rest_minutes INT COMMENT '实际休息时长（分钟）',
    appeal_reason TEXT COMMENT '申诉/补充说明理由',
    operator_id BIGINT COMMENT '操作人ID（司机或审核员）',
    operator_name VARCHAR(64) COMMENT '操作人姓名',
    operate_remark VARCHAR(512) COMMENT '操作备注（退回原因等）',
    submit_time DATETIME NOT NULL COMMENT '提交/操作时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_appeal (appeal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申诉材料提交记录表';

DROP TABLE IF EXISTS safety_review;
CREATE TABLE safety_review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    appeal_id BIGINT NOT NULL UNIQUE,
    restriction_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    review_no VARCHAR(64) NOT NULL UNIQUE COMMENT '复核单号',
    reviewer_id BIGINT NOT NULL COMMENT '安全专员ID',
    reviewer_name VARCHAR(64) NOT NULL COMMENT '安全专员姓名',
    review_result TINYINT NOT NULL COMMENT '复核结果：1-解除限制 2-维持限制',
    review_remark TEXT COMMENT '复核意见',
    verify_evidence JSON COMMENT '核实材料数据',
    rest_verified_minutes INT COMMENT '核实有效休息时长',
    review_time DATETIME NOT NULL COMMENT '复核时间',
    keep_evidence TINYINT DEFAULT 1 COMMENT '是否保留原始风控依据：1-是 0-否',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_appeal (appeal_id),
    INDEX idx_restriction (restriction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安全复核记录表';

INSERT INTO driver (driver_code, driver_name, phone, license_no, status) VALUES
('D001', '张三', '13800138001', '110101199001011234', 1),
('D002', '李四', '13800138002', '110101199001021235', 1),
('D003', '王五', '13800138003', '110101199001031236', 1),
('D004', '赵六', '13800138004', '110101199001041237', 1),
('D005', '钱七', '13800138005', '110101199001051238', 1);
