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
| 前端 | Vue 3 + Vite |
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
│       ├── controller/      # REST API 控制器
│       │   ├── AIController.py      # AI 对话与推荐
│       │   ├── ProductController.py # 产品管理
│       │   ├── TrialController.py   # 试用管理
│       │   ├── FeedbackController.py # 反馈管理
│       │   └── AdminController.py    # 管理员控制台
│       ├── service/        # 业务逻辑层
│       ├── mapper/         # MyBatis 数据访问层
│       └── model/          # 数据模型
│
├── frontend/               # Vue 3 前端
│   └── src/
│       ├── views/          # 页面视图
│       │   ├── Home.vue            # 门户首页
│       │   ├── AIChat.vue          # AI 对话室
│       │   ├── Products.vue        # 产品库
│       │   ├── ProductDetail.vue   # 产品详情
│       │   ├── TrialWorkbench.vue  # 试用工作台
│       │   └── AdminConsole.vue    # 管理后台
│       └── components/     # 可复用组件
│
├── ai/                     # AI 模块设计文档
├── scripts/                # 启动脚本
│   ├── dev-backend.sh     # 启动后端
│   ├── dev-frontend.sh    # 启动前端
│   └── dev.sh             # 一键启动
│
├── START.md               # 本地开发启动指南
├── TECHNICAL.md           # 技术方案文档
└── 核心功能梳理-物流智能门户.md  # 产品功能需求文档
```

---

## ✨ 核心功能

### 1. 多角色自适应门户
- ✅ 购买方视图：AI 助手入口、热门推荐、试用工作台
- ✅ 提供方视图：产品管理、方案配置、线索洞察
- ✅ 管理方视图：权限配置、审核中心、运营看板

### 2. AI 需求采集与方案推荐
- ✅ 多轮对话界面，自然语言理解需求
- ✅ 结构化需求指标（场景/预算/现状系统/数据基础/成熟度）
- ✅ 智能推荐 1-3 套方案，含解释性说明
- ✅ 方案横向对比（能力/成本/周期/风险）

### 3. 产品库
- ✅ 产品列表展示与搜索
- ✅ 产品详情页（能力、案例、价格、版本）
- ✅ 热门产品推荐

### 4. 在线试用工作台
- ✅ 仿真环境开通（试用实例管理）
- ✅ 测试数据管理（上传/样例数据）
- ✅ 功能试用体验入口
- ✅ 试用报告自动生成

### 5. 反馈与意向管理
- ✅ 一键提交反馈（评分/问题/附件）
- ✅ 购买意向表单（预算/期望上线时间/联系方式）
- ✅ 状态流转追踪（已提交 → 已查看 → 已跟进 → 已关闭）
- ✅ 站内通知推送

### 6. 管理后台
- ✅ 组织与权限管理
- ✅ 产品/方案审核流程
- ✅ 运营看板与数据监控
- ✅ 日志审计

---

## 🚀 快速开始

### 前置要求

- Node.js 18+
- Java 17
- Maven 3.8+
- MySQL 8.0

### 本地启动

```bash
# 克隆项目后，进入目录
cd tu

# 方式一：使用项目脚本一键启动（推荐）
./scripts/dev.sh

# 方式二：手动分步启动

# 终端 A：启动后端
cd backend
mvn spring-boot:run

# 终端 B：启动前端
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

## 📊 API 概览

### 产品模块
- `GET /api/products` - 获取产品列表
- `GET /api/products/{id}` - 获取产品详情
- `GET /api/products/recommend` - 智能推荐

### AI 模块
- `POST /api/ai/chat` - AI 对话
- `POST /api/ai/recommend` - 方案推荐

### 试用模块
- `POST /api/trials` - 创建试用
- `GET /api/trials/{id}` - 试用详情
- `POST /api/trials/{id}/feedback` - 提交反馈
- `GET /api/trials/user/{userId}` - 用户试用记录

---

## 📄 相关文档

- [START.md](./START.md) - 本地开发启动指南
- [TECHNICAL.md](./TECHNICAL.md) - 技术方案详解
- [核心功能梳理-物流智能门户.md](./核心功能梳理-物流智能门户.md) - 产品需求功能梳理

---

## 📝 TODO

- [ ] AI 对话能力增强（更多场景理解）
- [ ] 组合方案高级配置
- [ ] 需求洞察看板
- [ ] 本地化部署能力
- [ ] AI 运营工具（语料管理、A/B 测试）

---

## License

MIT
