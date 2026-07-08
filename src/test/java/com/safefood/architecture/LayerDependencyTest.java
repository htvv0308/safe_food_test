package com.safefood.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.architectures.layeredArchitecture.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * 架构分层测试 - 验证模块边界和依赖规则
 *
 * 依赖方向: domain <- config <- mapper <- service <- controller
 */
@AnalyzeClasses(
    packages = "com.safefood",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class LayerDependencyTest {

    // 层级架构规则
    @ArchTest
    public static final ArchRule layeredArchitectureRule = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Domain").definedBy("..domain..")
        .layer("Config").definedBy("..config..")
        .layer("Mapper").definedBy("..mapper..")
        .layer("Service").definedBy("..service..")
        .layer("Controller").definedBy("..controller..")
        .layer("Infrastructure").definedBy("..infrastructure..")
        .layer("Api").definedBy("..api..")

        // Controller不能被任何层访问（最外层）
        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        // Service只能被Controller访问
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Api")
        // Mapper只能被Service访问
        .whereLayer("Mapper").mayOnlyBeAccessedByLayers("Service")
        // Config只能被同层或上层访问
        .whereLayer("Config").mayOnlyBeAccessedByLayers("Mapper", "Service", "Controller")
        // Domain不能依赖任何业务层
        .whereLayer("Domain").mayNotBeAccessedByAnyLayer()
        .as(
            "❌ 层级依赖违规。\n" +
            "✅ 正确依赖方向: domain <- config <- mapper <- service <- controller\n" +
            "📖 See: docs/architecture/boundaries.md"
        );

    // Controller不能直接调用Mapper
    @ArchTest
    public static final ArchRule controllerMustNotUseMapper = noClasses()
        .that().resideInAPackage("..controller..")
        .should().dependOnClassesThat().resideInAPackage("..mapper..")
        .as(
            "❌ Controller不得直接依赖Mapper。\n" +
            "✅ FIX: 在Service中编排数据访问，Controller仅持有Service引用。\n" +
            "📖 See: docs/architecture/boundaries.md"
        );

    // Service不能调用Controller
    @ArchTest
    public static final ArchRule serviceMustNotUseController = noClasses()
        .that().resideInAPackage("..service..")
        .should().dependOnClassesThat().resideInAPackage("..controller..")
        .as(
            "❌ Service不得调用Controller。\n" +
            "✅ FIX: 使用事件驱动或返回结果由调用方处理。\n" +
            "📖 See: docs/architecture/boundaries.md"
        );

    // Domain不能依赖任何业务包
    @ArchTest
    public static final ArchRule domainMustNotDependOnBusiness = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAnyOf(
            "..config..",
            "..mapper..",
            "..service..",
            "..controller..",
            "..infrastructure..",
            "..api.."
        )
        .as(
            "❌ Domain包不得依赖业务层。\n" +
            "✅ FIX: Domain应只包含纯数据和枚举，不包含任何业务逻辑。\n" +
            "📖 See: docs/architecture/boundaries.md"
        );

    // 禁止字段注入
    @ArchTest
    public static final ArchRule noFieldInjection = noClasses()
        .should().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
        .as(
            "❌ 禁止字段级@Autowired。\n" +
            "✅ FIX: 构造器注入：\n" +
            "   @RequiredArgsConstructor\n" +
            "   public class UserService {\n" +
            "       private final UserMapper userMapper;\n" +
            "   }\n" +
            "📖 See: docs/conventions/README.md"
        );

    // Controller必须使用@RestController或@Controller
    @ArchTest
    public static final ArchRule controllerMustHaveRestAnnotation = classes()
        .that().resideInAPackage("..controller..")
        .and().haveNameMatching(".*Controller")
        .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
        .orShould().beAnnotatedWith("org.springframework.stereotype.Controller")
        .as(
            "❌ Controller类必须有@RestController或@Controller注解。\n" +
            "📖 See: docs/architecture/boundaries.md"
        );

    // Service必须标注@Service
    @ArchTest
    public static final ArchRule serviceMustHaveServiceAnnotation = classes()
        .that().resideInAPackage("..service..")
        .and().haveNameMatching(".*Service")
        .should().beAnnotatedWith("org.springframework.stereotype.Service")
        .as(
            "❌ Service类必须有@Service注解。\n" +
            "📖 See: docs/architecture/boundaries.md"
        );
}