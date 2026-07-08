# 编码规范

## Java后端规范

### 命名规范
| 类型 | 命名规则 | 示例 |
|-----|---------|------|
| 类名 | PascalCase | `FoodAnalysisService` |
| 方法名 | camelCase | `analyzeFoodInfo()` |
| 变量名 | camelCase | `foodInfoList` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT` |
| 包名 | 全小写 | `com.safefood.service` |

### 代码风格
- **缩进**: 4空格（不使用Tab）
- **行长度**: 最大120字符
- **空行**: 逻辑块之间空一行
- **大括号**: K&R风格（换行开括号）

### 注释规范
- 类和公共方法必须添加Javadoc注释
- 复杂业务逻辑添加行内注释
- FIXME/TODO必须注明日期和负责人

### 异常处理
- 业务异常抛出`BusinessException`
- 使用全局异常处理器`GlobalExceptionHandler`统一响应
- 禁止catch后不做任何处理（至少记录日志）

### 日志规范
```java
// 正确
@Slf4j
public class FoodService {
    public void analyze() {
        log.info("开始分析食品信息, foodId={}", foodId);
        try {
            // 业务逻辑
        } catch (Exception e) {
            log.error("分析食品信息失败, foodId={}", foodId, e);
            throw new BusinessException("分析失败");
        }
    }
}

// 错误
public class FoodService {
    public void analyze() {
        System.out.println("start");  // ❌ 禁止
        try {
            // 业务逻辑
        } catch (Exception e) {
            e.printStackTrace();  // ❌ 禁止
        }
    }
}
```

### 注入规范
```java
// 正确：构造器注入
@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodMapper foodMapper;
    private final OcrService ocrService;
}

// 错误：字段注入
@Service
public class FoodService {
    @Autowired  // ❌ 禁止
    private FoodMapper foodMapper;
}
```

## 前端规范

### TypeScript规范
- 所有.ts文件启用严格模式
- 接口和类型必须定义
- 禁止使用any类型

### Vue组件规范
```vue
<script setup lang="ts">
// 使用TypeScript类型
interface Props {
  foodName: string;
  scanResult?: ScanResult;
}

// 定义props
const props = withDefaults(defineProps<Props>(), {
  scanResult: () => ({})
});

// 定义emits
const emit = defineEmits<{
  (e: 'success', data: AnalysisResult): void;
  (e: 'error', error: Error): void;
}>();
</script>
```

### 样式规范
- 优先使用uview-plus组件
- 自定义样式使用scoped
- 禁止使用行内样式（除了动态绑定）