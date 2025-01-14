-- =============================================
-- 表结构定义
-- =============================================
CREATE TABLE `administrators` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `a_account` varchar(50) NOT NULL COMMENT '管理员账号(登录用)',
  `a_password` varchar(255) NOT NULL COMMENT '管理员密码',
  `a_name` varchar(30) NOT NULL COMMENT '管理员姓名',
  `a_id` char(18) NOT NULL COMMENT '身份证号(18位)',
  `a_email` varchar(50) NOT NULL COMMENT '电子邮箱',
  `a_tel` varchar(20) NOT NULL COMMENT '联系电话',
  `a_level` int NOT NULL COMMENT '管理员级别(1-超级管理员,2-普通管理员)',
  `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间(自动记录)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`a_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

CREATE TABLE `engineering` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `engineering_team_name` varchar(100) NOT NULL COMMENT '工程队名称(唯一标识)',
  `number` int NOT NULL COMMENT '工程队人数(>0)',
  `s_name` varchar(20) NOT NULL COMMENT '负责人姓名',
  `s_tel` varchar(20) NOT NULL COMMENT '负责人联系电话',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_name` (`engineering_team_name`),
  CONSTRAINT `engineering_check` CHECK ((`number` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工程队表';

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `u_account` varchar(50) NOT NULL COMMENT '用户账号(登录用,唯一)',
  `u_num` varchar(30) NOT NULL COMMENT '用户工号',
  `u_password` varchar(255) NOT NULL COMMENT '用户密码(加密存储)',
  `u_name` varchar(30) NOT NULL COMMENT '用户姓名',
  `u_sex` char(2) NOT NULL COMMENT '性别(男/女)',
  `u_id` char(18) NOT NULL COMMENT '身份证号(18位)',
  `u_email` varchar(50) NOT NULL COMMENT '电子邮箱',
  `u_tel` varchar(20) NOT NULL COMMENT '联系电话',
  `u_et_name` varchar(100) DEFAULT NULL COMMENT '所属工程队名称(外键关联engineering表)',
  `u_regdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间(自动记录)',
  `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间(自动记录)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`u_account`),
  UNIQUE KEY `uk_num` (`u_num`),
  CONSTRAINT `users_check` CHECK ((`u_sex` in (_utf8mb4'男',_utf8mb4'女'))),
  CONSTRAINT `fk_engineering` FOREIGN KEY (`u_et_name`) 
    REFERENCES `engineering` (`engineering_team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `stratums` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `stratum_id` varchar(30) NOT NULL COMMENT '地层编号(业务主键)',
  `stratum_name` varchar(100) NOT NULL COMMENT '地层名称',
  `stratum_len` decimal(11,3) NOT NULL COMMENT '地层长度(米,>0)',
  `stratum_add` varchar(100) NOT NULL COMMENT '地层地理位置',
  `stratum_pro` varchar(50) DEFAULT NULL COMMENT '所属项目',
  `integrity` varchar(6) DEFAULT 'NO' COMMENT '完整性(YES/NO)',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间(自动记录)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stratum_id` (`stratum_id`),
  CONSTRAINT `stratums_check` CHECK ((`stratum_len` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地层信息表';

CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `p_id` varchar(50) NOT NULL COMMENT '项目编号(业务主键)',
  `p_name` varchar(100) NOT NULL COMMENT '项目名称',
  `p_principal` varchar(30) DEFAULT NULL COMMENT '项目负责人(关联users表u_num)',
  `p_team` varchar(100) DEFAULT NULL COMMENT '负责工程队(关联engineering表)',
  `p_description` varchar(255) NOT NULL COMMENT '项目描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_id` (`p_id`),
  CONSTRAINT `fk_project_team` FOREIGN KEY (`p_team`) 
    REFERENCES `engineering` (`engineering_team_name`),
  CONSTRAINT `fk_project_principal` FOREIGN KEY (`p_principal`) 
    REFERENCES `users` (`u_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

CREATE TABLE `core_segments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `image_id` varchar(30) NOT NULL COMMENT '图像编号(业务主键)',
  `image_name` varchar(50) NOT NULL COMMENT '图像名称',
  `image_path` varchar(100) NOT NULL COMMENT '图像存储路径',
  `upload_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间(自动记录)',
  `uploader_num` varchar(30) NOT NULL COMMENT '上传者编号(关联users表u_num)',
  `seg_start` decimal(10,3) NOT NULL COMMENT '段起始深度(米,>0)',
  `seg_end` decimal(10,3) NOT NULL COMMENT '段结束深度(米,>0)',
  `seg_len` decimal(10,3) NOT NULL COMMENT '段长度(米,>0)',
  `seg_type` varchar(50) NOT NULL COMMENT '岩心段类型',
  `sequence_no` int NOT NULL DEFAULT '0' COMMENT '序列号(默认0)',
  `stratum_id` varchar(30) NOT NULL COMMENT '所属地层编号(关联stratums表)',
  `stratum_len` decimal(11,3) NOT NULL COMMENT '地层长度(米)',
  `edit_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后编辑时间(自动更新)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_image_id` (`image_id`),
  CONSTRAINT `fk_core_uploader` FOREIGN KEY (`uploader_num`) 
    REFERENCES `users` (`u_num`),
  CONSTRAINT `fk_core_stratum` FOREIGN KEY (`stratum_id`) 
    REFERENCES `stratums` (`stratum_id`),
  CONSTRAINT `core_seg_start_check` CHECK ((`seg_start` > 0)),
  CONSTRAINT `core_seg_end_check` CHECK ((`seg_end` > 0)),
  CONSTRAINT `core_seg_len_check` CHECK ((`seg_len` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岩心段信息表';

CREATE TABLE `user_operation_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint NOT NULL COMMENT '操作人ID(关联users表id)',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_content` varchar(500) NOT NULL COMMENT '操作内容描述',
  `operation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation_result` varchar(10) NOT NULL COMMENT '操作结果(SUCCESS/FAIL)',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) 
    REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户操作日志表';

-- =============================================
-- 索引定义
-- =============================================
CREATE INDEX idx_user_account ON users(u_account);
CREATE INDEX idx_stratum_name ON stratums(stratum_name);
CREATE INDEX idx_core_upload ON core_segments(upload_time);
CREATE INDEX idx_operation_time ON user_operation_logs(operation_time);
CREATE INDEX idx_user_id ON user_operation_logs(user_id);

-- =============================================
-- 视图定义
-- =============================================
CREATE VIEW UPLOADER_Image AS  
SELECT DISTINCT u.u_name, u.u_num, u.u_sex, u.u_email, u.u_tel, u.u_et_name
FROM users u  
JOIN core_segments cs ON u.u_num = cs.uploader_num
ORDER BY u.u_num;

CREATE VIEW ADMINI_INFO AS  
SELECT DISTINCT ad.a_name, ad.a_tel, ad.a_email, ad.a_level
FROM administrators ad;

CREATE VIEW ET_STAFF AS  
SELECT u.u_et_name, u.u_name, u.u_num
FROM users u  
JOIN engineering ET ON u.u_et_name = ET.engineering_team_name
ORDER BY u.u_et_name;

-- =============================================
-- 初始数据
-- =============================================
INSERT INTO administrators (a_account, a_password, a_name, a_id, a_email, a_tel, a_level) VALUES
('admin', 'admin123', '系统管理员', '110101199001011234', 'admin@example.com', '13800138000', 1),
('manager', 'manager123', '普通管理员', '110101199001011235', 'manager@example.com', '13800138001', 2);

INSERT INTO engineering (engineering_team_name, number, s_name, s_tel) VALUES
('第一工程队', 10, '张三', '13911111111'),
('第二工程队', 8, '李四', '13922222222');

INSERT INTO users (u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name) VALUES
('user001', 'U2023001', 'user123', '王五', '男', '110101199001011236', 'wangwu@example.com', '13933333333', '第一工程队'),
('user002', 'U2023002', 'user456', '赵六', '女', '110101199001011237', 'zhaoliu@example.com', '13944444444', '第二工程队');

INSERT INTO stratums (stratum_id, stratum_name, stratum_len, stratum_add, stratum_pro) VALUES
('S2023001', '第一地层', 100.500, '北京市海淀区', 'P2023001'),
('S2023002', '第二地层', 150.750, '北京市朝阳区', 'P2023002');

INSERT INTO project (p_id, p_name, p_principal, p_team, p_description) VALUES
('P2023001', '海淀区地质勘探项目', 'U2023001', '第一工程队', '海淀区综合地质勘探工程'),
('P2023002', '朝阳区地质调查项目', 'U2023002', '第二工程队', '朝阳区地质结构调查分析');

INSERT INTO core_segments (image_id, image_name, image_path, uploader_num, seg_start, seg_end, seg_len, seg_type, sequence_no, stratum_id, stratum_len) VALUES
('IMG2023001', '海淀区岩心样本1', '/images/core/2023/001.jpg', 'U2023001', 10.500, 20.500, 10.000, '砂岩', 1, 'S2023001', 100.500),
('IMG2023002', '朝阳区岩心样本1', '/images/core/2023/002.jpg', 'U2023002', 15.750, 25.750, 10.000, '页岩', 1, 'S2023002', 150.750);

INSERT INTO user_operation_logs (user_id, operation_type, operation_content, operation_result) VALUES
('1', '上传岩心图像', '上传图像IMG2023001', '200'),
('2', '修改项目信息', '更新项目P2023002描述', '200');

