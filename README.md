# TileForge - 专业瓦片地图解决方案

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-8+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.5.9-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![GitHub stars](https://img.shields.io/github/stars/your-username/tileforge.svg)](https://github.com/your-username/tileforge/stargazers)

🚀 专业的瓦片地图解决方案，将超大像素图片精工锻造成高效瓦片系统，支持理论上无限像素图片的流畅展示和交互操作。

[English](#english) | [中文](#chinese)

## 中文 {#chinese}

### 📋 项目简介

TileForge 是一个专为处理超大像素静态图片而设计的专业瓦片地图解决方案。它采用先进的瓦片锻造技术，能够高效处理理论上无限像素的图片（目前支持最大 2,147,483,648 像素），并提供丰富的前端交互功能。项目名称寓意着将原始大图"锻造"成精细的瓦片系统，为用户提供流畅的浏览体验。

### ✨ 主要特性

#### 后端服务 (Java)
- 🔧 **瓦片切片**: 采用标准瓦片地图切片算法
- 💾 **多存储支持**: 本地存储 + 阿里云OSS云存储
- 📈 **高性能**: 支持超大像素图片处理
- 🌐 **RESTful API**: 提供完整的API接口
- 📚 **Swagger文档**: 内置API文档支持

#### 前端展示 (JavaScript + OpenLayers)
- 🎨 **绘制工具**: 支持多种形状绘制和标记
- 🎮 **WebGL渲染**: 支持透明度、灰度、亮度等颜色渲染
- 🔒 **区域控制**: 缩放、拖拽、显示区域边界控制
- 📸 **截图功能**: 当前显示区域截图并上传
- 🖱️ **交互友好**: 流畅的用户交互体验

### 🚀 快速开始

#### 环境要求

- Java 8+
- Gradle 4+
- Node.js (用于前端开发)

#### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/your-username/tileforge.git
cd tileforge
```

2. **后端服务部署**
```bash
cd tile.server
# 开发环境
./gradlew bootRun

# 生产环境构建
./gradlew build -Pprofile=prod
```

3. **前端部署**
```bash
cd view
# 可以使用任何HTTP服务器，如：
python -m http.server 8080
# 或
npx serve .
```

#### 配置说明

在 `tile.server/src/main/resources/application.properties` 中配置：

```properties
# 服务端口
server.port=8080

# 阿里云OSS配置（可选）
aliyun.oss.endpoint=your-endpoint
aliyun.oss.accessKeyId=your-access-key
aliyun.oss.accessKeySecret=your-secret-key
aliyun.oss.bucketName=your-bucket
```

### 📖 使用说明

#### API文档

启动服务后，访问 Swagger UI 查看完整API文档：
```
http://localhost:8080/swagger-ui.html
```

#### 主要API端点

- `POST /api/source` - 上传原始图片
- `GET /api/tiles/{z}/{x}/{y}` - 获取瓦片
- `POST /api/snapshot` - 创建截图
- `GET /api/area` - 获取区域信息

#### 前端使用

1. 在浏览器中打开 `view/index.html`
2. 上传或选择要展示的图片
3. 使用工具栏进行绘制、标记等操作
4. 支持缩放、拖拽等交互

### 🏗️ 项目结构

```
tileforge/
├── tile.server/          # Java后端服务
│   ├── src/main/java/    # Java源码
│   ├── src/main/resources/ # 配置文件
│   └── build.gradle      # Gradle构建文件
├── view/                 # 前端页面
│   ├── src/             # 前端源码
│   └── public/          # 静态资源
└── README.md            # 项目文档
```

### 🤝 贡献指南

我们欢迎所有形式的贡献！请查看 [CONTRIBUTING.md](CONTRIBUTING.md) 了解详细信息。

### 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。

### 🆘 支持

如果您遇到问题或有建议，请：
- 查看 [Issues](https://github.com/your-username/tileforge/issues)
- 创建新的 Issue
- 发送邮件至: your-email@example.com

---

## English {#english}

### 📋 Project Description

TileForge is a professional tile mapping solution designed for handling ultra-high-resolution static images with efficient visualization and interactive operations. The name represents the concept of "forging" large images into precise tile systems for optimal performance.

### ✨ Key Features

#### Backend Service (Java)
- 🔧 **Tile Slicing**: Standard tile map slicing algorithm
- 💾 **Multi-Storage**: Local storage + Aliyun OSS cloud storage
- 📈 **High Performance**: Support for unlimited pixel images (currently up to 2,147,483,648 pixels)
- 🌐 **RESTful API**: Complete API interface
- 📚 **Swagger Documentation**: Built-in API documentation

#### Frontend Display (JavaScript + OpenLayers)
- 🎨 **Drawing Tools**: Support for various shapes and annotations
- 🎮 **WebGL Rendering**: Transparency, grayscale, brightness rendering
- 🔒 **Area Control**: Zoom, drag, display boundary controls
- 📸 **Screenshot**: Current display area screenshot and upload
- 🖱️ **User-Friendly**: Smooth user interaction experience

### 🚀 Quick Start

#### Requirements

- Java 8+
- Gradle 4+
- Node.js (for frontend development)

#### Installation

1. **Clone the repository**
```bash
git clone https://github.com/your-username/tileforge.git
cd tileforge
```

2. **Backend service deployment**
```bash
cd tile.server
# Development
./gradlew bootRun

# Production build
./gradlew build -Pprofile=prod
```

3. **Frontend deployment**
```bash
cd view
# Use any HTTP server, such as:
python -m http.server 8080
# or
npx serve .
```

### 📖 Usage

#### API Documentation

Access Swagger UI for complete API documentation after starting the service:
```
http://localhost:8080/swagger-ui.html
```

### 🤝 Contributing

We welcome all forms of contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

### 📄 License

This project is licensed under the [MIT License](LICENSE).

### 🆘 Support

If you encounter issues or have suggestions:
- Check [Issues](https://github.com/your-username/tileforge/issues)
- Create a new Issue
- Email us at: your-email@example.com
