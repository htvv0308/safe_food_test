# 模块边界与依赖规则

## 包结构规范

### 后端包结构
```
src/main/java/com/safefood/
├── domain/                 # 领域模型（不依赖任何业务包）
│   ├── entity/            # MyBatis-Plus Entity
│   ├── dto/               # Data Transfer Object (请求/响应)
│   ├── enums/             # 枚举类
│   └── constant/          # 常量类
├── config/                 # Spring配置类（只依赖domain）
├── mapper/                 # MyBatis-Plus Mapper（依赖domain、config）
├── service/               # 业务逻辑（依赖domain、config、mapper）
├── controller/            # REST接口（依赖domain、service）
├── router/                # 前端路由（仅前端项目）
├── infrastructure/        # 基础设施（横切关注点）
│   ├── exception/         # 异常处理
│   ├── util/              # 工具类
│   └── common/            # 公共组件
└── api/                   # 外部API调用
    ├── ocr/               # 阿里云OCR
    └── llm/               # 大模型API
```

### 前端目录结构
```
src/
├── api/                   # API接口定义
├── components/            # 公共组件
├── pages/                 # 页面组件
│   ├── index/             # 首页
│   ├── scan/              # 扫描页
│   ├── history/           # 历史记录
│   └── profile/           # 个人中心
├── stores/                # Pinia状态管理
├── utils/                 # 工具函数
└── static/                # 静态资源
```

## 依赖规则

### 层级依赖方向（单向依赖）
```
domain ← config ← mapper ← service ← controller ← router
                              ↓
                        infrastructure
                              ↓
                           api(外部)
```

### 禁止的依赖模式
1. ❌ Controller直接调用Mapper（必须经由Service）
2. ❌ Service调用Controller
3. ❌ Mapper调用Service或Controller
4. ❌ Domain依赖Config、Mapper、Service
5. ❌ 前端页面直接调用数据库

### 正确的调用示例
```java
// Controller - 只做参数校验和响应包装
@RestController
@RequiredArgsConstructor
public class FoodAnalysisController {
    private final FoodAnalysisService foodAnalysisService;

    @PostMapping("/api/food/analyze")
    public Result<AnalysisResponse> analyze(@Valid @RequestBody AnalysisRequest request) {
        AnalysisResponse response = foodAnalysisService.analyze(request);
        return Result.success(response);
    }
}

// Service - 编排业务逻辑
@Service
@RequiredArgsConstructor
public class FoodAnalysisService {
    private final FoodInfoMapper foodInfoMapper;
    private final OcrService ocrService;
    private final LlMService llmService;

    public AnalysisResponse analyze(AnalysisRequest request) {
        // 业务逻辑
    }
}

// Mapper - 数据访问
@Mapper
public interface FoodInfoMapper extends BaseMapper<FoodInfoEntity> {
}
```