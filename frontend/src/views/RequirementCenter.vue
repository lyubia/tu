<template>
  <div class="requirement-center">
    <div class="page-header">
      <h1>📋 需求中心</h1>
      <p class="subtitle">管理您的需求记录，快速复用历史方案</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-value">{{ stats.total }}</div>
        <div class="stat-label">需求总数</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.completed }}</div>
        <div class="stat-label">已完成</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.recommendations }}</div>
        <div class="stat-label">获得推荐</div>
      </div>
    </div>

    <!-- 功能标签 -->
    <div class="tabs">
      <button class="tab" :class="{ active: activeTab === 'list' }" @click="activeTab = 'list'">我的需求</button>
      <button class="tab" :class="{ active: activeTab === 'history' }" @click="activeTab = 'history'">历史对话</button>
    </div>

    <!-- 需求列表 -->
    <div v-if="activeTab === 'list'" class="tab-content">
      <div v-if="requirements.length === 0" class="empty-state">
        <div class="empty-icon">📋</div>
        <p>暂无需求记录</p>
        <button class="btn-primary" @click="$router.push('/ai-chat')">开始 AI 选型</button>
      </div>

      <div v-else class="requirement-list">
        <div v-for="req in requirements" :key="req.id" class="requirement-card">
          <div class="req-header">
            <span class="req-status" :class="req.status">{{ req.statusText }}</span>
            <span class="req-time">{{ req.createTime }}</span>
          </div>
          
          <div class="req-content">
            <div class="req-items">
              <div v-for="(value, key) in req.requirements" :key="key" class="req-item">
                <span class="req-label">{{ formatLabel(key) }}</span>
                <span class="req-value">{{ value }}</span>
              </div>
            </div>
          </div>

          <div class="req-actions">
            <button class="btn" @click="viewRecommendations(req)">查看推荐</button>
            <button class="btn" @click="reuseRequirement(req)">复用</button>
            <button class="btn btn-outline" @click="deleteRequirement(req)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 历史对话 -->
    <div v-if="activeTab === 'history'" class="tab-content">
      <div class="conversation-list">
        <div v-for="conv in conversations" :key="conv.id" class="conversation-item">
          <div class="conv-header">
            <span class="conv-time">{{ conv.createTime }}</span>
            <span class="conv-messages">{{ conv.messageCount }} 条对话</span>
          </div>
          <div class="conv-preview">{{ conv.preview }}</div>
          <div class="conv-actions">
            <button class="btn" @click="viewConversation(conv)">查看详情</button>
            <button class="btn btn-primary" @click="continueConversation(conv)">继续对话</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'RequirementCenter',
  setup() {
    const activeTab = ref('list')

    const stats = ref({
      total: 8,
      completed: 5,
      recommendations: 12
    })

    const requirements = ref([
      {
        id: 1,
        status: 'COMPLETED',
        statusText: '已完成',
        createTime: '2026-03-12',
        requirements: {
          industry: '物流',
          scenario: '仓储管理',
          budget: '50-100万',
          scale: '中大型企业',
          currentSystem: '有WMS'
        }
      },
      {
        id: 2,
        status: 'COLLECTING',
        statusText: '收集中',
        createTime: '2026-03-13',
        requirements: {
          industry: '电商',
          scenario: '订单管理'
        }
      }
    ])

    const conversations = ref([
      {
        id: 1,
        createTime: '2026-03-12 14:30',
        messageCount: 6,
        preview: '我想找一款适合物流行业的仓储管理系统...'
      }
    ])

    const formatLabel = (key) => {
      const map = {
        industry: '行业',
        scenario: '场景',
        budget: '预算',
        scale: '企业规模',
        currentSystem: '现有系统'
      }
      return map[key] || key
    }

    const viewRecommendations = (req) => {
      console.log('查看推荐:', req)
    }

    const reuseRequirement = (req) => {
      console.log('复用:', req)
    }

    const deleteRequirement = (req) => {
      console.log('删除:', req)
    }

    const viewConversation = (conv) => {
      console.log('查看对话:', conv)
    }

    const continueConversation = (conv) => {
      console.log('继续对话:', conv)
    }

    return {
      activeTab, stats, requirements, conversations,
      formatLabel, viewRecommendations, reuseRequirement,
      deleteRequirement, viewConversation, continueConversation
    }
  }
}
</script>

<style scoped>
.requirement-center { max-width: 1000px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 28px; color: #333; margin-bottom: 8px; }
.subtitle { color: #666; }
.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
.stat-card { background: #fff; border-radius: 12px; padding: 24px; text-align: center; }
.stat-value { font-size: 32px; font-weight: 600; color: #1890ff; }
.stat-label { font-size: 14px; color: #999; margin-top: 8px; }
.tabs { display: flex; gap: 8px; margin-bottom: 20px; background: #fff; padding: 12px; border-radius: 12px; }
.tab { padding: 10px 20px; border: none; background: transparent; cursor: pointer; border-radius: 8px; }
.tab.active { background: #1890ff; color: #fff; }
.tab-content { background: #fff; border-radius: 12px; padding: 24px; }
.empty-state { text-align: center; padding: 60px; }
.empty-icon { font-size: 48px; margin-bottom: 16px; }
.empty-state p { color: #999; margin-bottom: 20px; }
.btn-primary { padding: 10px 24px; background: #1890ff; color: #fff; border: none; border-radius: 8px; cursor: pointer; }
.requirement-list { display: flex; flex-direction: column; gap: 16px; }
.requirement-card { border: 1px solid #eee; border-radius: 12px; padding: 20px; }
.req-header { display: flex; justify-content: space-between; margin-bottom: 16px; }
.req-status { padding: 4px 12px; border-radius: 4px; font-size: 12px; }
.req-status.COMPLETED { background: #f6ffed; color: #52c41a; }
.req-status.COLLECTING { background: #e6f7ff; color: #1890ff; }
.req-time { font-size: 12px; color: #999; }
.req-items { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 16px; }
.req-item { display: flex; flex-direction: column; gap: 4px; }
.req-label { font-size: 12px; color: #999; }
.req-value { font-size: 14px; color: #333; font-weight: 500; }
.req-actions { display: flex; gap: 8px; padding-top: 16px; border-top: 1px solid #f0f0f0; }
.btn { padding: 8px 16px; border: none; background: #f5f5f5; border-radius: 6px; cursor: pointer; font-size: 13px; }
.btn-primary { background: #1890ff; color: #fff; }
.btn-outline { background: #fff; border: 1px solid #d9d9d9; }
.conversation-list { display: flex; flex-direction: column; gap: 12px; }
.conversation-item { border: 1px solid #eee; border-radius: 8px; padding: 16px; }
.conv-header { display: flex; justify-content: space-between; margin-bottom: 8px; }
.conv-time { font-size: 14px; color: #333; }
.conv-messages { font-size: 12px; color: #999; }
.conv-preview { font-size: 14px; color: #666; margin-bottom: 12px; }
.conv-actions { display: flex; gap: 8px; }
</style>
