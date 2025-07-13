# 变更日志 / Changelog

本文档记录了此项目的所有重要更改。

All notable changes to this project will be documented in this file.

日志格式基于 [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)，项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [未发布 / Unreleased]

### 新增 / Added
- 项目重命名为TileForge，提升品牌影响力 / Project renamed to TileForge for enhanced brand impact
- 完整的开源项目文档结构 / Complete open source project documentation structure
- 详细的README文档（中英文双语）/ Detailed README documentation (bilingual)
- MIT开源许可证 / MIT License
- 贡献指南 / Contributing guidelines
- 代码行为准则 / Code of conduct
- 安全策略 / Security policy
- GitHub Issue和PR模板 / GitHub issue and PR templates
- CI/CD工作流程 / CI/CD workflow
- 完整的.gitignore文件 / Comprehensive .gitignore file

### 改进 / Changed
- 优化项目结构和文档组织 / Improved project structure and documentation organization

## [1.0.0] - 2024-01-XX

### 新增 / Added

#### 后端功能 / Backend Features
- 🔧 **瓦片切片服务** / Tile slicing service
  - 基于标准瓦片地图算法的图片切片 / Image slicing based on standard tile map algorithm
  - 支持理论上无限像素图片处理 / Support for theoretically unlimited pixel image processing
  - 当前支持最大 2,147,483,648 像素图片 / Currently supports up to 2,147,483,648 pixel images

- 💾 **存储支持** / Storage Support
  - 本地文件存储 / Local file storage
  - 阿里云OSS云存储集成 / Aliyun OSS cloud storage integration
  - 自动存储管理 / Automatic storage management

- 🌐 **RESTful API** / RESTful API
  - 图片上传API / Image upload API
  - 瓦片获取API / Tile retrieval API
  - 截图创建API / Screenshot creation API
  - 区域信息API / Area information API

- 📚 **API文档** / API Documentation
  - 集成Swagger UI / Integrated Swagger UI
  - 完整的API文档和测试界面 / Complete API documentation and testing interface

#### 前端功能 / Frontend Features
- 🎨 **交互式地图显示** / Interactive Map Display
  - 基于OpenLayers的瓦片地图显示 / Tile map display based on OpenLayers
  - 流畅的缩放和拖拽操作 / Smooth zoom and drag operations
  - 自适应显示区域控制 / Adaptive display area control

- 🖊️ **绘制工具** / Drawing Tools
  - 多种形状绘制支持（圆形、矩形、多边形、线条）/ Multiple shape drawing support (circle, rectangle, polygon, line)
  - 标记和注释功能 / Marking and annotation features
  - 绘制结果数据记录 / Drawing result data recording

- 🎮 **WebGL渲染** / WebGL Rendering
  - 实时颜色调整（透明度、灰度、亮度）/ Real-time color adjustment (transparency, grayscale, brightness)
  - 高性能图像渲染 / High-performance image rendering
  - GPU加速处理 / GPU-accelerated processing

- 📸 **截图功能** / Screenshot Feature
  - 当前显示区域截图 / Current display area screenshot
  - 自动上传和保存 / Automatic upload and save
  - 截图元数据记录 / Screenshot metadata recording

#### 技术特性 / Technical Features
- ☕ **后端技术栈** / Backend Tech Stack
  - Spring Boot 1.5.9 框架 / Spring Boot 1.5.9 framework
  - Java 8+ 支持 / Java 8+ support
  - Gradle 构建系统 / Gradle build system
  - Lombok 代码简化 / Lombok code simplification

- 🌐 **前端技术栈** / Frontend Tech Stack
  - 原生JavaScript实现 / Native JavaScript implementation
  - OpenLayers 地图库 / OpenLayers mapping library
  - WebGL 图形渲染 / WebGL graphics rendering
  - 响应式设计 / Responsive design

- 🔒 **安全特性** / Security Features
  - 输入验证和过滤 / Input validation and filtering
  - 安全的文件上传处理 / Secure file upload handling
  - 跨域请求控制 / Cross-origin request control

### 架构设计 / Architecture Design
- **微服务架构** / Microservice architecture
- **前后端分离** / Frontend-backend separation
- **模块化设计** / Modular design
- **可扩展存储** / Scalable storage

### 性能优化 / Performance Optimization
- **瓦片缓存机制** / Tile caching mechanism
- **懒加载实现** / Lazy loading implementation
- **内存优化** / Memory optimization
- **并发处理** / Concurrent processing

### 已知限制 / Known Limitations
- 目前仅支持静态图片 / Currently only supports static images
- 最大图片尺寸限制为 2,147,483,648 像素 / Maximum image size limited to 2,147,483,648 pixels
- 阿里云OSS为可选存储方案 / Aliyun OSS as optional storage solution

---

## 版本说明 / Version Notes

### 语义化版本格式 / Semantic Versioning Format
- **主版本号**：不兼容的API修改 / **MAJOR**: Incompatible API changes
- **次版本号**：向下兼容的新功能 / **MINOR**: Backwards compatible new features  
- **修订号**：向下兼容的问题修正 / **PATCH**: Backwards compatible bug fixes

### 发布类型 / Release Types
- **Alpha**: 内部测试版本 / Internal testing version
- **Beta**: 公开测试版本 / Public testing version
- **RC**: 发布候选版本 / Release candidate
- **Stable**: 稳定发布版本 / Stable release version

### 标签含义 / Tag Meanings
- `新增 / Added`: 新功能 / New features
- `改进 / Changed`: 现有功能的变更 / Changes in existing functionality
- `弃用 / Deprecated`: 即将删除的功能 / Soon-to-be removed features
- `移除 / Removed`: 已删除的功能 / Removed features
- `修复 / Fixed`: 问题修复 / Bug fixes
- `安全 / Security`: 安全相关修复 / Security fixes

---

[未发布 / Unreleased]: https://github.com/your-username/tileforge/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/your-username/tileforge/releases/tag/v1.0.0 