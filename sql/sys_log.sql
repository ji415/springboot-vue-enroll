-- 系统操作日志表（管理员操作审计）
CREATE TABLE sys_log (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL COMMENT '操作人ID（管理员ID）',
                         username VARCHAR(50) NOT NULL COMMENT '操作人用户名',
                         action VARCHAR(50) NOT NULL COMMENT '操作类型：AUDIT / DELETE / EXPORT',
                         target_type VARCHAR(50) NOT NULL COMMENT '操作对象类型：STUDENT',
                         target_id BIGINT COMMENT '被操作对象ID（student.id）',
                         before_status VARCHAR(50) COMMENT '操作前状态',
                         after_status VARCHAR(50) COMMENT '操作后状态',
                         remark VARCHAR(255) COMMENT '备注信息',
                         ip VARCHAR(50) COMMENT '操作IP',
                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
) COMMENT='系统操作日志表';
