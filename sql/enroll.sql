-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS enroll DEFAULT CHARSET utf8mb4;
USE enroll;

-- 2. 学生表
CREATE TABLE student (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(50) NOT NULL,
     id_card VARCHAR(18) NOT NULL,
     parent_phone VARCHAR(20) NOT NULL,
     address VARCHAR(255) NOT NULL,
     status TINYINT NOT NULL DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
     deleted TINYINT NOT NULL DEFAULT 0,
     create_time DATETIME,
     update_time DATETIME,
     UNIQUE KEY uk_id_card (id_card)
);

-- 3. 初始化测试数据
INSERT INTO student(name,id_card,parent_phone,address,status,deleted,create_time)
VALUES
    ('张三','110101200001010011','13800000001','北京市朝阳区',1,0,NOW()),
    ('李四','110101200002020022','13800000002','北京市海淀区',0,0,NOW()),
    ('王五','110101200003030033','13800000003','北京市西城区',2,0,NOW());
