# 岩芯图像管理系统

## 项目简介
本系统是一个基于 Spring Boot 的岩芯图像管理系统，用于管理和查询岩芯图像信息。系统提供了用户管理、图像上传、信息检索等功能。

## 技术栈
- **后端**：Spring Boot 2.x
- **数据库**：MySQL
- **ORM**：MyBatis
- **API文档**：Swagger
- **权限认证**：JWT

## 主要功能
1. **用户管理**
   - 用户注册
   - 用户登录
   - 用户信息管理
   - 管理员登录

2. **图像管理**
   - 图像上传
   - 图像信息录入
   - 图像信息修改
   - 图像删除

3. **信息检索**
   - 支持岩芯编号模糊查询
   - 支持深度范围查询
   - 支持岩芯类型模糊查询
   - 动态组合查询条件

## 项目结构
```
src
├── main
│   ├── java
│   │   └── com.example.handle
│   │       ├── config         // 配置类
│   │       ├── controller     // 控制器
│   │       ├── model          // 实体类
│   │       ├── mapper         // 数据访问层
│   │       ├── service        // 业务逻辑层
│   │       └── function       // 工具类
│   └── resources
│       ├── application.yml    // 应用配置文件
│       ├── static             // 静态资源
│       └── templates          // 模板文件
```

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 安装步骤
1. **克隆项目**
   ```bash
   git clone https://github.com/hideon27/handle-service.git
   ```

2. **配置数据库**
   - 创建数据库
     ```sql
     CREATE DATABASE core_image_system;
     ```
   - 修改 `application.yml` 中的数据库配置

3. **运行项目**
   ```bash
   mvn spring-boot:run
   ```

### 接口文档
启动项目后，访问 Knife4j 文档：
```
http://localhost:3000/doc.html
```

注意：确保已经在 pom.xml 中添加了 Knife4j 相关依赖，并且配置了 SwaggerConfig 类。

## 部署说明
1. **打包**
   ```bash
   mvn clean package
   ```

2. **运行**
   ```bash
   java -jar target/core-image-system.jar
   ```

## 常见问题
- **如何配置数据库连接？**
  在 `src/main/resources/application.yml` 中配置数据库的 URL、用户名和密码。

- **如何添加新的 API 接口？**
  在 `controller` 包中添加新的控制器类，并使用 `@RestController` 和 `@RequestMapping` 注解。




