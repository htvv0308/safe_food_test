# AGENTS.md - 食品安全AI助手

## 项目简介
这是一个面向消费者的食品安全AI助手移动端应用，用户通过OCR扫描食品包装，AI分析配料、营养、添加剂并给出购买建议。

## 技术栈基线（不允许擅自升级）
- **前端框架**: uni-app 3.x + Vue 3.x + Pinia + uview-plus
- **后端框架**: Spring Boot 3.x（要求JDK 17+）
- **Java版本**: JDK 17（必须使用JDK 17，禁止使用JDK 8/11/21+）
- **持久层**: MyBatis-Plus 3.5.x
- **数据库**: MySQL 8.0（utf8mb4）
- **缓存**: Redis 7.x
- **OCR服务**: 阿里云OCR（食品标签识别）
- **AI大模型**: 通义千问 / 文心一言 / 智谱GLM（可配置切换）

## 快速导航
| 你想做什么 | 去看哪里 |
|-----------|---------|
| 了解系统架构 | docs/architecture/overview.md |
| 了解模块边界和依赖规则 | docs/architecture/boundaries.md |
| 了解编码规范 | docs/conventions/README.md |
| 了解API规范 | docs/reference/api-spec.yaml |
| 了解功能设计 | docs/design/ |

## 硬性规则（CI会验证）
1. **层级依赖**: domain → config → mapper → service → controller → router
2. **禁止反向依赖**: 下层不能依赖上层
3. **横切关注点**: 统一通过Spring注入，禁止new实例化
4. **单文件行数**: Java ≤500行，Vue单文件组件≤300行
5. **单方法行数**: ≤80行
6. **日志规范**: 禁止System.out.println，使用SLF4J Logger
7. **API响应**: 统一使用Result包装（code/message/data结构）

## 后端开发规范
- 使用构造器注入，禁止字段级@Autowired
- Service层异常统一抛出自定义业务异常，由全局异常处理器捕获
- Controller只做参数校验和响应包装，业务逻辑下沉到Service
- 使用Lombok减少样板代码（@Data/@Builder/@Slf4j等）

## 前端开发规范
- 使用TypeScript（.ts文件）
- 组件使用Composition API (<script setup>)
- 样式使用uview-plus组件库，减少自定义样式
- 接口调用统一使用request封装
- 敏感信息存储在storage中需要加密处理