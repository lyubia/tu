# 物流智能门户平台 - 版本路线图

> 基于《核心功能梳理-物流智能门户.md》的完整实现

## ✅ 已完成

### v0.1 - 基础框架
- Spring Boot + Vue 3 + MySQL
- 基础 CRUD

### v1.0 - 解决方案模块
- 方案列表、详情、对比

### v1.1 - 多角色切换
- 角色切换器
- 提供方工作台基础

### v1.2 - AI需求采集
- RequirementForm 需求清单
- requirements 表

### v1.3 - 智能推荐反馈
- 感兴趣/不匹配反馈

### v1.4 - 试用工作台
- 试用报告

### v1.5 - 提供方工作台
- 产品管理
- 线索管理

### v1.6 - 管理后台
- 角色权限
- 审核中心
- 运营看板
- 审计日志

### 购买方门户增强
- 需求中心
- 推荐中心

### 供方工作台增强
- 产品版本管理
- 试用配置

### 管理后台增强
- AdminRoles 角色权限
- AdminAudit 审核中心
- AdminDashboard 运营看板

---

## 📊 数据表 (18张)

核心: users, products, solutions, trials, trial_feedback

扩展: requirements, requirement_feedback, trial_reports, trial_test_data

管理: orgs, roles, permissions, user_roles, audit_logs, system_config

业务: favorites, recently_viewed, conversations, intentions, product_versions, trial_configs, leads

---

## 📱 前端页面 (16个)

首页、产品库、方案列表/详情/对比、AI对话

需求中心、推荐中心、试用工作台、试用报告

提供方工作台、产品管理

管理后台、角色权限、审核中心、运营看板
