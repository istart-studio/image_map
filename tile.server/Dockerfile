# 使用官方OpenJDK 8作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置维护者信息
LABEL maintainer="your-email@example.com"
LABEL description="TileForge Tile Server - 专业瓦片地图解决方案"
LABEL version="1.0.0"

# 设置工作目录
WORKDIR /app

# 创建应用用户
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# 安装必要的工具
RUN apk add --no-cache \
    curl \
    tzdata \
    && rm -rf /var/cache/apk/*

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制gradle wrapper和构建文件
COPY gradlew gradle.build settings.gradle ./
COPY gradle ./gradle

# 赋予执行权限
RUN chmod +x ./gradlew

# 复制源代码
COPY src ./src

# 构建应用
RUN ./gradlew build -Pprofile=prod --no-daemon

# 创建应用目录结构
RUN mkdir -p /app/data /app/logs /app/uploads /app/tiles

# 移动构建好的jar文件
RUN mv build/libs/*.jar app.jar

# 清理构建缓存
RUN rm -rf build .gradle src gradle gradlew build.gradle settings.gradle

# 更改文件所有者
RUN chown -R appuser:appuser /app

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8080

# 设置JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=32m -XX:+UseStringDeduplication"

# 设置应用参数
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 