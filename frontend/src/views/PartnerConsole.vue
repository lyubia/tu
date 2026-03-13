<template>
  <div class="partner-console">
    <div class="page-header">
      <h1>提供方工作台</h1>
      <p class="subtitle">管理您的产品、方案和客户需求</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">📦</div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.productCount }}</span>
          <span class="stat-label">我的产品</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">👁️</div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.viewCount }}</span>
          <span class="stat-label">总浏览量</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🧪</div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.trialCount }}</span>
          <span class="stat-label">试用申请</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">💬</div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.feedbackCount }}</span>
          <span class="stat-label">用户反馈</span>
        </div>
      </div>
    </div>

    <!-- 功能导航 -->
    <div class="feature-grid">
      <div class="feature-card" @click="activeTab = 'products'">
        <div class="feature-icon">📦</div>
        <h3>产品管理</h3>
        <p>上架、编辑、上下线产品</p>
      </div>
      <div class="feature-card" @click="activeTab = 'demands'">
        <div class="feature-icon">🔍</div>
        <h3>需求洞察</h3>
        <p>查看客户需求和画像</p>
      </div>
      <div class="feature-card" @click="activeTab = 'feedback'">
        <div class="feature-icon">💬</div>
        <h3>反馈管理</h3>
        <p>查看和处理用户反馈</p>
      </div>
    </div>

    <!-- 产品管理 -->
    <div v-if="activeTab === 'products'" class="tab-content">
      <div class="section-header">
        <h2>我的产品</h2>
      </div>
      <div class="products-list">
        <div v-for="product in myProducts" :key="product.id" class="product-item">
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p>{{ product.description }}</p>
            <div class="product-tags">
              <span class="tag">{{ product.category }}</span>
              <span class="tag status" :class="product.status">{{ product.status }}</span>
            </div>
          </div>
          <div class="product-actions">
            <button class="btn btn-sm" @click="editProduct(product)">编辑</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 需求洞察 -->
    <div v-if="activeTab === 'demands'" class="tab-content">
      <div class="section-header">
        <h2>需求洞察</h2>
      </div>
      <div class="demands-grid">
        <div class="demand-card">
          <h3>行业分布</h3>
          <div class="demand-list">
            <div class="demand-item" v-for="(item, index) in demandData.industries" :key="index">
              <span class="demand-name">{{ item.name }}</span>
              <span class="demand-count">{{ item.count }} 次</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 反馈管理 -->
    <div v-if="activeTab === 'feedback'" class="tab-content">
      <div class="section-header">
        <h2>用户反馈</h2>
      </div>
      <div class="feedback-list">
        <div v-for="fb in feedbacks" :key="fb.id" class="feedback-item">
          <div class="feedback-header">
            <span class="rating">⭐ {{ fb.rating }}/5</span>
            <span class="status" :class="fb.status">{{ fb.status }}</span>
          </div>
          <p class="feedback-content">{{ fb.feedback }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'PartnerConsole',
  setup() {
    const activeTab = ref('products')
    
    const stats = ref({
      productCount: 3,
      viewCount: 1256,
      trialCount: 28,
      feedbackCount: 12
    })

    const myProducts = ref([
      { id: 1, name: '智能仓储管理系统', description: '基于AI的智能仓储解决方案', category: '仓储管理', status: 'ACTIVE' },
      { id: 2, name: '物流运输管理平台', description: '覆盖运输全流程的数字化管理平台', category: '运输管理', status: 'ACTIVE' }
    ])

    const demandData = ref({
      industries: [
        { name: '物流', count: 45 },
        { name: '电商', count: 30 },
        { name: '制造', count: 20 },
        { name: '零售', count: 15 }
      ]
    })

    const feedbacks = ref([
      { id: 1, rating: 5, feedback: '系统功能非常强大', status: 'VIEWED' },
      { id: 2, rating: 4, feedback: '系统稳定，响应速度快', status: 'SUBMITTED' }
    ])

    const editProduct = (product) => {
      console.log('Edit:', product)
    }

    return { activeTab, stats, myProducts, demandData, feedbacks, editProduct }
  }
}
</script>

<style scoped>
.partner-console { max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 28px; color: #333; margin-bottom: 8px; }
.subtitle { color: #666; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
.stat-card { background: #fff; border-radius: 12px; padding: 20px; display: flex; align-items: center; gap: 16px; }
.stat-icon { font-size: 32px; }
.stat-content { display: flex; flex-direction: column; }
.stat-value { font-size: 24px; font-weight: 600; color: #333; }
.stat-label { font-size: 14px; color: #999; }
.feature-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
.feature-card { background: #fff; border-radius: 12px; padding: 24px; text-align: center; cursor: pointer; transition: all 0.3s; }
.feature-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.1); }
.feature-icon { font-size: 36px; margin-bottom: 12px; }
.feature-card h3 { font-size: 16px; margin-bottom: 8px; }
.feature-card p { font-size: 14px; color: #999; }
.tab-content { background: #fff; border-radius: 12px; padding: 24px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.section-header h2 { font-size: 20px; }
.btn { padding: 10px 20px; border-radius: 6px; cursor: pointer; border: none; font-size: 14px; }
.btn-primary { background: #1890ff; color: #fff; }
.btn-sm { padding: 6px 12px; font-size: 12px; }
.products-list { display: flex; flex-direction: column; gap: 16px; }
.product-item { display: flex; justify-content: space-between; align-items: center; padding: 16px; border: 1px solid #eee; border-radius: 8px; }
.product-info h3 { font-size: 16px; margin-bottom: 4px; }
.product-info p { font-size: 14px; color: #666; margin-bottom: 8px; }
.product-tags { display: flex; gap: 8px; }
.tag { padding: 2px 8px; background: #f0f0f0; border-radius: 4px; font-size: 12px; }
.tag.status.ACTIVE { background: #f6ffed; color: #52c41a; }
.demands-grid { display: grid; grid-template-columns: 1fr; gap: 24px; }
.demand-card { padding: 20px; background: #f9f9f9; border-radius: 8px; }
.demand-card h3 { font-size: 16px; margin-bottom: 16px; }
.demand-item { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #eee; }
.demand-name { color: #333; }
.demand-count { color: #999; }
.feedback-list { display: flex; flex-direction: column; gap: 16px; }
.feedback-item { padding: 16px; border: 1px solid #eee; border-radius: 8px; }
.feedback-header { display: flex; justify-content: space-between; margin-bottom: 12px; }
.rating { color: #faad14; }
.status { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.status.SUBMITTED { background: #e6f7ff; color: #1890ff; }
.status.VIEWED { background: #fff7e6; color: #fa8c16; }
.feedback-content { color: #333; }
</style>
