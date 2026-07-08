# 食品安全AI助手 (Safe Food Assistant)

> 基于Harness Engineering实践构建的AI辅助移动端应用

## 仓库信息
- **GitHub**: https://github.com/htvv0308/safe_food_test
- **分支策略**: Git Flow（main/develop/feature/hotfix）

## 项目概述

帮助消费者通过OCR扫描食品包装，AI分析配料、营养、添加剂并给出购买建议。

## 技术栈

### 前端
- **框架**: uni-app 3.x + Vue 3.x
- **状态管理**: Pinia
- **UI组件**: uview-plus
- **构建工具**: Vite

### 后端
- **框架**: Spring Boot 3.x (JDK 17)
- **持久层**: MyBatis-Plus 3.5.x
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.x

### AI服务
- **OCR**: 阿里云食品标签识别
- **大模型**: 通义千问/文心一言/智谱GLM（可配置）

## 快速开始

### 后端启动

```bash
# 1. 创建数据库
mysql -u root -p < src/main/resources/sql/init.sql

# 2. 配置application.yml中的数据库和Redis连接

# 3. 编译运行
mvn clean install
mvn spring-boot:run
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

## 项目结构

```
safe_food_test/
├── AGENTS.md                    # Agent导航文件
├── pom.xml                      # Maven配置
├── docs/                        # 文档
│   ├── architecture/            # 架构文档
│   ├── conventions/             # 编码规范
│   ├── design/                  # 功能设计
│   ├── plans/                   # 迭代计划
│   └── reference/               # API参考
├── src/                         # 后端源码
│   ├── main/java/               # 业务代码
│   └── test/                    # 测试代码
└── frontend/                    # 前端源码
```

## 架构约束

本项目遵循Harness Engineering实践，详见：

- [AGENTS.md](./AGENTS.md) - Agent导航
- [docs/architecture/boundaries.md](./docs/architecture/boundaries.md) - 依赖规则
- [docs/conventions/README.md](./docs/conventions/README.md) - 编码规范
- [docs/conventions/git-workflow.md](./docs/conventions/git-workflow.md) - Git版本管理

## CI质量门禁

- Maven Enforcer: JDK 17 + Maven 3.9+
- ArchUnit: 层级依赖检查
- JaCoCo: 行覆盖率 ≥ 70%
- Checkstyle: 代码规范检查

## License

MIT