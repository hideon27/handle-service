# 岩芯图像管理系统 - 项目上下文索引

## 1. 项目概述

这是一个基于Spring Boot的岩芯图像管理系统，主要用于管理地质勘探过程中的岩芯图像、地层信息和用户数据。系统提供了用户认证、图像上传处理、数据查询和管理等功能。

## 2. 技术栈

- **后端框架**: Spring Boot 2.7.6
- **数据库**: MySQL 8.x
- **ORM框架**: MyBatis
- **API文档**: Knife4j (基于Swagger)
- **认证**: JWT (JSON Web Token)
- **其他工具**: Lombok, Spring Security Crypto

## 3. 项目结构

```
handle-service/
├── src/main/java/com/example/handle/
│   ├── config/                 # 配置类
│   ├── controller/             # 控制器
│   ├── dto/                    # 数据传输对象
│   │   ├── requestdata/        # 请求数据对象
│   │   └── resultdata/         # 响应数据对象
│   ├── function/               # 工具类和SQL提供者
│   ├── mapper/                 # MyBatis映射接口
│   ├── model/                  # 数据模型
│   ├── schedule/               # 定时任务
│   ├── service/                # 服务层
│   └── util/                   # 工具类
├── src/main/resources/
│   ├── application.yml         # 应用配置
│   ├── db/                     # 数据库脚本
│   ├── csv/                    # CSV数据文件
│   └── pictures/               # 上传的图片
```

## 4. 核心功能模块

### 4.1 用户认证模块

- 用户登录 (`/post/login`)
- 管理员登录 (`/post/adminLogin`)
- 用户注册 (`/post/register`)
- 获取用户信息 (`/userinfo`)

### 4.2 图片上传和处理模块

- 上传图片 (`/image/upload`, `/uploadimage_path`)
- 处理图片 (`/image/process`)
- 获取图片 (`/images/{image_id}`)
- 上传图片信息 (`/post/upload`)

### 4.3 数据查询模块

- 获取工程队名称 (`/getEngineeringTeamName`)
- 获取岩芯信息 (`/getStratumName`)
- 获取图片信息 (`/change/showImageInfo`, `/get/getImageInfo`)
- 获取地层信息 (`/change/showStratumInfo`, `/get/getStratumInfo`)
- 获取用户信息 (`/change/showUserInfo`, `/get/getUserInfo`)

### 4.4 数据管理模块

- 更新图片信息 (`/change/updateImageInfo`, `/change/updateSubmit`)
- 更新用户信息 (`/change/updateSubmitUser`)
- 更新地层信息 (`/change/updateSubmitStratum`)
- 插入岩柱信息 (`/post/insertStratum`)
- 删除图片 (`/change/deleteImage`)
- 删除用户 (`/change/deleteUser`)
- 删除地层 (`/change/deleteStratum`)

### 4.5 岩柱完整性检查模块

- 检查岩柱完整性(触发式) (`/get/checkStratumIntegrity_trigger`)
- 检查岩柱完整性(非触发式) (`/get/checkStratumIntegrity_notrigger`)
- 手动触发岩柱完整性检查 (`/checkStratumIntegrity/manual`)

### 4.6 操作日志模块

- 记录用户操作日志 (`/log/operation`)
- 查询用户操作日志 (`/log/query`)

## 5. 数据库结构

### 5.1 主要表

- `administrators`: 管理员表
- `engineering`: 工程队表
- `users`: 用户表
- `stratums`: 地层信息表
- `project`: 项目表
- `core_segments`: 岩心段信息表
- `user_operation_logs`: 用户操作日志表

### 5.2 视图

- `UPLOADER_Image`: 上传者与图像关联视图
- `ADMINI_INFO`: 管理员信息视图
- `ET_STAFF`: 工程队员工视图

## 6. 关键文件索引

### 6.1 配置文件

- `application.yml`: 主配置文件，包含数据库连接、文件上传路径等配置

### 6.2 核心代码文件

- `HandleApplication.java`: 应用入口
- `HandleController.java`: 主控制器，包含所有API端点
- `HandleService.java`: 服务层，处理业务逻辑
- `HandleMapper.java`: MyBatis映射接口，处理数据库操作
- `StratumIntegritySchedule.java`: 岩柱完整性检查定时任务

### 6.3 数据库脚本

- `schema.sql`: 数据库表结构和初始数据

## 7. 业务流程

### 7.1 用户认证流程

1. 用户提交账号密码
2. 系统验证账号密码
3. 生成JWT令牌返回给用户
4. 用户后续请求携带令牌进行身份验证

### 7.2 图片上传和处理流程

1. 用户上传图片
2. 系统保存图片到指定目录
3. 调用外部服务(http://127.0.0.1:5000)处理图片
4. 保存图片信息到数据库

### 7.3 岩柱完整性检查流程

1. 获取岩柱长度和岩心段总长度
2. 比较两者是否一致(误差<0.001)
3. 如果一致，按深度排序并更新序列号
4. 更新岩柱完整性状态
5. 生成CSV报告文件

## 8. 定时任务

- 岩柱完整性检查: 每天凌晨2点执行 (`0 0 2 * * ?`)

## 9. 外部依赖

- 图像处理服务: 运行在本地5000端口，提供图像分析功能

## 10. 文件存储路径

- 图片上传目录: `C:\Users\hideon27\IdeaProjects\handle-service\src\main\resources\pictures`
- 岩柱完整性检查结果目录: `C:\Users\hideon27\IdeaProjects\handle-service\src\main\resources\csv\stratum_integrity_results`
- 岩芯完整性检查结果目录: `C:\Users\hideon27\IdeaProjects\handle-service\src\main\resources\csv\core_integrity_results`
- 有效岩芯目录: `C:\Users\hideon27\IdeaProjects\handle-service\src\main\resources\csv\valid_core`