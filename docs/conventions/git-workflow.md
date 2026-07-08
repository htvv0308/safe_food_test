# Git工作流规范

## 仓库信息
- **远程仓库**: https://github.com/htvv0308/safe_food_test
- **主分支**: main（生产环境可运行代码）
- **开发分支**: develop（最新开发进度）

## 分支策略

### 分支类型
| 分支类型 | 命名规范 | 用途 | 合并目标 |
|---------|---------|------|---------|
| main | main | 生产版本 | - |
| develop | develop | 开发主分支 | - |
| feature | feature/功能名 | 新功能开发 | develop |
| hotfix | hotfix/问题描述 | 生产问题修复 | main + develop |
| bugfix | bugfix/问题描述 | 开发期问题修复 | develop |
| release | release/版本号 | 发布准备 | main + develop |

### 分支生命周期
```
develop ←──── feature/xxx ←──── 开发者本地
   │
   ▼
release/1.0.0 ←──── 准备发布
   │
   ▼
main (tag: v1.0.0) ←──── 正式发布
   │
   ▼
hotfix/fix-xxx (如需)
```

## 提交规范

### 提交信息格式
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type类型
| 类型 | 说明 |
|-----|------|
| feat | 新功能 |
| fix | Bug修复 |
| docs | 文档更新 |
| style | 代码格式（不影响功能）|
| refactor | 重构（既不是新功能也不是bug修复）|
| test | 测试相关 |
| chore | 构建过程或辅助工具变动 |

### 示例
```bash
# 功能
feat(food-analysis): 添加食品OCR识别功能

# 修复
fix(ocr): 修复特殊字符识别失败的问题

# 文档
docs(readme): 更新安装步骤

# 重构
refactor(service): 优化FoodAnalysisService逻辑
```

### 提交约束
- 提交信息必须使用英文
- subject首字母小写，结尾不加句号
- 每行不超过100字符
- 关联Issue: 使用Closes #123

## 合并流程

### Feature开发流程
```bash
# 1. 从develop创建功能分支
git checkout develop
git pull origin develop
git checkout -b feature/xxx

# 2. 开发并提交
git add .
git commit -m "feat: 添加xxx功能"

# 3. 推送并创建PR
git push -u origin feature/xxx
# 在GitHub创建Pull Request

# 4. 合并后删除分支
git checkout develop
git pull origin develop
git branch -d feature/xxx
```

### Hotfix修复流程
```bash
# 1. 从main创建修复分支
git checkout main
git pull origin main
git checkout -b hotfix/xxx

# 2. 修复并提交
git add .
git commit -m "fix: 修复xxx问题"

# 3. 推送到远程
git push -u origin hotfix/xxx

# 4. 合并到main和develop
git checkout main
git merge --no-ff hotfix/xxx
git push origin main

git checkout develop
git merge --no-ff hotfix/xxx
git push origin develop

# 5. 删除分支
git branch -d hotfix/xxx
```

## 标签管理（Tag）

### 标签命名
- 版本标签: `v1.0.0`, `v1.1.0`, `v2.0.0`
- 预发布标签: `v1.0.0-alpha`, `v1.0.0-beta`

### 创建标签
```bash
# 创建版本标签
git tag -a v1.0.0 -m "Release v1.0.0"

# 推送到远程
git push origin v1.0.0
```

## 冲突处理

### 原则
1. 优先使用rebase而非merge（保持线性历史）
2. 冲突必须手动解决，禁止强制推送
3. 解决后必须测试通过才能提交

### rebase流程
```bash
# 1. 拉取最新代码
git fetch origin

# 2. 变基到最新develop
git rebase origin/develop

# 3. 解决冲突后
git add .
git rebase --continue

# 4. 强制推送（仅限自己分支）
git push --force-with-lease
```

## 禁止操作

❌ 禁止直接向main分支推送代码  
❌ 禁止强制推送main/develop分支  
❌ 禁止提交敏感信息（密码、Token等）  
❌ 禁止合并未通过CI的PR  
❌ 禁止将代码提交到错误分支

## CI/CD触发条件

| 分支 | 触发构建 | 触发部署 |
|-----|---------|---------|
| main | ✅ 自动 | ✅ 自动 |
| develop | ✅ 自动 | ✅ 手动 |
| feature/* | ✅ 自动 | ❌ |
| hotfix/* | ✅ 自动 | ✅ 手动 |

## 回滚操作

### 回滚单个提交
```bash
git revert <commit-hash>
git push origin main
```

### 回滚到特定版本
```bash
git reset --hard <tag>
git push --force-with-lease origin main
```

## 附：常用命令速查

```bash
# 克隆仓库
git clone https://github.com/htvv0308/safe_food_test.git

# 查看状态
git status

# 查看分支
git branch -a

# 创建并切换分支
git checkout -b feature/xxx

# 暂存修改
git stash

# 恢复暂存
git stash pop

# 查看提交历史
git log --oneline --graph

# 查看差异
git diff origin/main
```