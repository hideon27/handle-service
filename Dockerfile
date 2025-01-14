FROM openjdk:17

# 设置工作目录
WORKDIR /app

# 创建必要的目录
RUN mkdir -p /app/upload \
    /app/stratum-integrity \
    /app/core-integrity \
    /app/valid-core \
    /app/operation-log

# 复制项目JAR包到容器中
COPY target/*.jar app.jar

# 暴露应用端口
EXPOSE 3000

# 启动命令
ENTRYPOINT ["java","-jar","app.jar"]