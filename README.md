# 物流数字化解决方案智能门户平台

> AI 对话驱动的解决方案选型与验证平台

## 📋 项目简介

本项目是一个面向物流行业的**AI 驱动解决方案选型与验证平台**，旨在把"需求采集 → 方案推荐 → 在线试用 → 反馈回流"打通成闭环。

### 核心价值

- **需求智能化**：通过 AI 多轮对话将模糊需求转化为结构化选型指标
- **推荐精准化**：基于需求智能匹配 1-3 套个性化方案
- **试用在线化**：一键开通仿真环境，边用边评估
- **反馈闭环化**：试用后直接提交反馈与购买意向，追踪处理状态

---

## 🛠 技术栈

| 层级 | 技术选型 |
|------|----------|
| 前端 | Vue 3 + Vite + Pinia + Vue Router |
| 后端 | Spring Boot 3.2 (Java 17) |
| 数据库 | MySQL 8.0 |
| ORM | MyBatis |
| AI | 可扩展对接大语言模型 |

---

## 📁 项目结构

```
tu/
├── backend/                 # Spring Boot 后端
│   └── src/main/java/com/example/demo/
│       ├── controller/       # REST API 控制器
│       │   ├── AIController.java      # AI 对话与推荐
│       │   ├── ProductController.java # 产品管理
│       │   ├── SolutionController.java # 方案管理
│       │   ├── TrialController.java   # 试用管理
│       │   ├── FeedbackController.java # 反馈管理
│       │   ├── AdminController.java   # 管理员控制台
│       │   └── PartnerController.java # 提供方工作台
│       ├── service/         # 业务逻辑层
│       ├── mapper/          # MyBatis 数据访问层
│       └── model/           # 数据模型
│
├── frontend/               # Vue 3 前端
│   └── src/
│       ├── views/           # 页面视图
│       │   ├── Home.vue              # 门户首页
│       │   ├── AIChat.vue            # AI 对话室
│       │   ├── Products.vue          # 产品库
│       │   ├── ProductDetail.vue     # 产品详情
│       │   ├── Solutions.vue         # 解决方案列表
│       │   ├── SolutionDetail.vue    # 解决方案详情
│       │   ├── SolutionCompare.vue   # 方案对比
│       │   ├── RequirementCenter.vue # 需求中心
│       │   ├── RecommendationCenter.vue # 推荐中心
│       │   ├── TrialWorkbench.vue    # 试用工作台
│       │   ├── TrialReport.vue      # 试用报告
│       │   ├── PartnerConsole.vue    # 提供方工作台
│       │   ├── AdminConsole.vue     # 管理后台
│       │   ├── AdminRoles.vue       # 角色权限管理
│       │   ├── AdminAudit.vue       # 审核中心
│       │   └── AdminDashboard.vue   # 运营看板
│       ├── components/      # 可复用组件
│       │   ├── ProductCard.vue      # 产品卡片
│       │   └── RequirementForm.vue # 需求表单
│       ├── styles/          # 样式文件
│       │   └── theme.css   # 统一主题样式
│       ├── api/             # API 接口
│       └── router/          # 路由配置
│
├── scripts/               # 启动脚本
├── ROADMAP.md            # 版本路线图
├── ADJUSTMENT_PLAN.md    # 调整计划
└── 核心功能梳理-物流智能门户.md  # 产品功能需求文档
```

---

## ✨ 核心功能

### 1. 购买方闭环
- **首页**：AI 顾问入口、热门推荐、解决方案、产品库
- **需求中心**：我的需求、历史对话、需求复用
- **推荐中心**：推荐方案列表、方案对比、匹配度展示
- **AI 对话**：多轮对话采集需求、智能推荐方案
- **试用工作台**：环境开通、测试数据管理、试用报告
- **反馈与意向**：评分反馈、购买意向、状态追踪

### 2. 供方闭环
- **产品管理**：产品上架/编辑/上下线、版本管理
- **试用配置**：功能开关、数据范围、试用权限、样例数据
- **需求洞察**：行业分布、热门场景、试用记录
- **线索管理**：意向客户、跟进状态

### 3. 平台闭环
- **角色权限**：组织、用户、角色、菜单权限、数据权限
- **审核中心**：产品审核、方案审核、内容审核
- **运营看板**：访问指标、对话指标、试用指标、转化漏斗
- **日志审计**：访问日志、操作日志、异常告警

---

## 🗄️ 数据库表 (18张)

| 分类 | 表名 | 说明 |
|------|------|------|
| 核心 | users | 用户表 |
| 核心 | products | 产品表 |
| 核心 | solutions | 解决方案表 |
| 核心 | trials | 试用记录表 |
| 核心 | trial_feedback | 试用反馈表 |
| 需求 | requirements | 结构化需求表 |
| 需求 | requirement_feedback | 需求反馈表 |
| 需求 | conversations | 对话记录表 |
| 试用 | trial_reports | 试用报告表 |
| 试用 | trial_test_data | 测试数据表 |
| 试用 | trial_configs | 试用配置表 |
| 供方 | partner_products | 提供方产品表 |
| 供方 | leads | 线索表 |
| 供方 | product_versions | 产品版本表 |
| 管理 | orgs | 组织表 |
| 管理 | roles | 角色表 |
| 管理 | permissions | 权限表 |
| 管理 | audit_logs | 审计日志表 |

---

## 🚀 快速开始

### 前置要求

- Node.js 18+
- Java 17
- Maven 3.8+
- MySQL 8.0

### 本地启动

```bash
# 克隆项目
git clone https://github.com/lyubia/tu.git
cd tu

# 启动后端
cd backend
mvn spring-boot:run

# 启动前端 (新终端)
cd frontend
npm install
npm run dev
```

### 访问地址

| 服务 | 地址 |
|------|------|
| 前端 | http://localhost:3000 |
| 后端 API | http://localhost:8080 |

---

## 📱 功能页面

| 页面 | 路径 | 说明 |
|------|------|------|
| 首页 | / | 门户首页 |
| 产品库 | /products | 产品列表 |
| 产品详情 | /products/:id | 产品详情页 |
| 解决方案 | /solutions | 方案列表 |
| 方案详情 | /solutions/:id | 方案详情 |
| 方案对比 | /solutions/compare | 方案对比 |
| 需求中心 | /requirements | 我的需求 |
| 推荐中心 | /recommendations | 推荐方案 |
| AI 顾问 | /ai-chat | AI 对话 |
| 试用工作台 | /trial | 试用管理 |
| 试用报告 | /trial/report | 试用报告 |
| 提供方工作台 | /partner | 供方工作台 |
| 管理后台 | /admin | 管理员控制台 |
| 角色权限 | /admin/roles | 角色权限 |
| 审核中心 | /admin/audit | 审核管理 |
| 运营看板 | /admin/dashboard | 数据看板 |

---

## 📊 API 概览

### 产品模块
- `GET /api/products` - 获取产品列表
- `GET /api/products/{id}` - 获取产品详情
- `GET /api/products/popular` - 热门产品
- `GET /api/products/categories` - 产品分类

### 方案模块
- `GET /api/solutions` - 获取方案列表
- `GET /api/solutions/{id}` - 获取方案详情
- `GET /api/solutions/industries` - 行业分类

### AI 模块
- `POST /api/ai/chat` - AI 对话
- `POST /api/ai/recommend` - 方案推荐

### 试用模块
- `POST /api/trials` - 创建试用
- `GET /api/trials/{id}` - 试用详情
- `POST /api/trials/{id}/feedback` - 提交反馈
- `GET /api/trials/user/{userId}` - 用户试用记录

### 反馈模块
- `POST /api/feedback/submit` - 提交反馈
- `GET /api/feedback/user/{userId}` - 用户反馈历史

---

## 📄 相关文档

- [ROADMAP.md](./ROADMAP.md) - 版本路线图
- [ADJUSTMENT_PLAN.md](./ADJUSTMENT_PLAN.md) - 调整计划
- [核心功能梳理-物流智能门户.md](./核心功能梳理-物流智能门户.md) - 产品需求功能梳理
- [START.md](./START.md) - 本地开发启动指南

---

## License

MIT
