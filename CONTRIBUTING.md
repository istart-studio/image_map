# 贡献指南 / Contributing Guide

感谢您对 TileForge 项目的关注！我们欢迎任何形式的贡献。

Thank you for your interest in contributing to TileForge! We welcome contributions of all kinds.

[English](#english) | [中文](#chinese)

## 中文 {#chinese}

### 🤝 如何贡献

#### 报告问题 (Bug Reports)

如果您发现了bug，请：

1. 首先检查 [现有的 Issues](https://github.com/your-username/tileforge/issues) 确认问题尚未被报告
2. 创建新的 Issue，包含以下信息：
   - 详细的问题描述
   - 重现步骤
   - 期望的行为
   - 实际的行为
   - 环境信息（操作系统、Java版本、浏览器等）
   - 相关的日志或截图

#### 功能请求 (Feature Requests)

我们欢迎新功能建议：

1. 创建一个 Issue 并标记为 "enhancement"
2. 描述您希望添加的功能
3. 解释为什么这个功能有用
4. 如果可能，提供设计方案或示例

#### 代码贡献 (Code Contributions)

##### 开发环境设置

1. **Fork 项目**
```bash
# Fork GitHub上的项目到您的账户
# 然后克隆到本地
git clone https://github.com/YOUR_USERNAME/image_map.git
cd image_map
```

2. **设置开发环境**
```bash
# 后端环境
cd tile.server
./gradlew build

# 前端环境
cd ../view
# 确保有HTTP服务器环境
```

3. **创建开发分支**
```bash
git checkout -b feature/your-feature-name
# 或
git checkout -b fix/your-fix-name
```

##### 代码规范

###### Java 代码规范
- 使用 4 个空格缩进
- 遵循 Java 命名约定
- 类名使用 PascalCase
- 方法和变量使用 camelCase
- 常量使用 UPPER_SNAKE_CASE
- 添加必要的注释和JavaDoc

###### JavaScript 代码规范
- 使用 2 个空格缩进
- 使用分号结尾
- 使用单引号包含字符串
- 变量和函数名使用 camelCase

###### 提交信息规范
使用语义化的提交信息：

```
类型(范围): 简短描述

详细描述（可选）

关闭的Issue（可选）
```

类型包括：
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

示例：
```
feat(api): 添加图片批量上传接口

- 支持多文件同时上传
- 添加上传进度显示
- 增加错误处理机制

Closes #123
```

##### Pull Request 流程

1. **确保代码质量**
   - 代码通过所有测试
   - 遵循代码规范
   - 添加必要的测试用例
   - 更新相关文档

2. **创建 Pull Request**
   - 使用清晰的标题描述更改
   - 在描述中说明：
     - 解决的问题
     - 更改的内容
     - 测试方式
     - 相关的 Issue 编号

3. **代码审查**
   - 等待维护者审查
   - 根据反馈进行修改
   - 保持友好和建设性的讨论

#### 文档贡献

- 改进现有文档
- 添加使用示例
- 翻译文档到其他语言
- 修复文档中的错误

### 📋 开发任务

#### 新手友好的任务
查找标记为 "good first issue" 的 Issues，这些通常适合新贡献者。

#### 优先级任务
- 性能优化
- 错误处理改进
- 单元测试覆盖
- 文档完善

### 🎯 发布流程

1. 版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)
2. 重要更改记录在 CHANGELOG.md 中
3. 创建 Release 标签
4. 发布到相应的包管理器

### 📞 联系方式

- GitHub Issues: 报告问题和功能请求
- 邮件: your-email@example.com
- 讨论区: 技术讨论和问答

---

## English {#english}

### 🤝 How to Contribute

#### Bug Reports

If you find a bug, please:

1. Check [existing Issues](https://github.com/your-username/tileforge/issues) first
2. Create a new Issue with:
   - Detailed problem description
   - Steps to reproduce
   - Expected behavior
   - Actual behavior
   - Environment info (OS, Java version, browser, etc.)
   - Relevant logs or screenshots

#### Feature Requests

We welcome feature suggestions:

1. Create an Issue labeled "enhancement"
2. Describe the desired feature
3. Explain why it would be useful
4. Provide design ideas or examples if possible

#### Code Contributions

##### Development Setup

1. **Fork the project**
```bash
# Fork the project on GitHub to your account
# Then clone locally
git clone https://github.com/YOUR_USERNAME/image_map.git
cd image_map
```

2. **Setup development environment**
```bash
# Backend environment
cd tile.server
./gradlew build

# Frontend environment
cd ../view
# Ensure HTTP server environment
```

3. **Create development branch**
```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/your-fix-name
```

##### Code Standards

###### Java Code Standards
- Use 4 spaces for indentation
- Follow Java naming conventions
- Class names use PascalCase
- Methods and variables use camelCase
- Constants use UPPER_SNAKE_CASE
- Add necessary comments and JavaDoc

###### JavaScript Code Standards
- Use 2 spaces for indentation
- Use semicolons
- Use single quotes for strings
- Variables and function names use camelCase

###### Commit Message Format
Use semantic commit messages:

```
type(scope): short description

Detailed description (optional)

Closes issues (optional)
```

Types include:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation update
- `style`: Code formatting
- `refactor`: Code refactoring
- `test`: Test related
- `chore`: Build process or auxiliary tool changes

Example:
```
feat(api): add batch image upload endpoint

- Support multiple file upload
- Add upload progress display
- Enhance error handling

Closes #123
```

##### Pull Request Process

1. **Ensure code quality**
   - Pass all tests
   - Follow code standards
   - Add necessary test cases
   - Update relevant documentation

2. **Create Pull Request**
   - Use clear title describing changes
   - In description, explain:
     - Problem solved
     - Changes made
     - How to test
     - Related Issue numbers

3. **Code Review**
   - Wait for maintainer review
   - Make changes based on feedback
   - Keep discussions friendly and constructive

#### Documentation Contributions

- Improve existing documentation
- Add usage examples
- Translate documentation
- Fix documentation errors

### 📋 Development Tasks

#### Beginner-Friendly Tasks
Look for Issues labeled "good first issue" - these are usually suitable for new contributors.

#### Priority Tasks
- Performance optimization
- Error handling improvements
- Unit test coverage
- Documentation enhancement

### 🎯 Release Process

1. Version numbers follow [Semantic Versioning](https://semver.org/)
2. Important changes recorded in CHANGELOG.md
3. Create Release tags
4. Publish to relevant package managers

### 📞 Contact

- GitHub Issues: Report problems and feature requests
- Email: your-email@example.com
- Discussions: Technical discussions and Q&A 