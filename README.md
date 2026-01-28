# 招生报名系统（Spring Boot + Vue3）

## 一、项目简介
本项目为一个前后端分离的招生报名系统，包含：
- 家长端：信息填报、查看审核状态
- 管理端：报名审核、列表管理、数据导出

## 二、技术栈
- 后端：Spring Boot + MyBatis-Plus + MySQL
- 前端：Vue3 + Element Plus + Axios
- 数据库：MySQL 

## 三、功能说明
### 家长端
- 填写报名信息
- 查看审核状态
- 驳回后可修改重新提交

### 管理端
- 报名信息分页展示
- 按姓名/手机号搜索
- 审核通过/驳回
- 删除测试数据
- 导出通过审核的学生 Excel

## 四、运行环境
- JDK 21
- Node.js 16+
- MySQL 8+

## 五、启动方式

### 1. 数据库
1. 创建数据库
2. 执行 `sql/enroll.sql`

### 2. 后端
使用 IDEA 运行 EnrollApplication

### 3. 前端
```bash
cd frontend
npm install
npm run dev
```
