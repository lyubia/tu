<template>
  <div class="solution-detail">
    <div class="page-header">
      <button class="back-btn" @click="$router.back()">← 返回</button>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
    </div>

    <template v-else-if="solution">
      <!-- 方案基本信息 -->
      <div class="solution-info">
        <span class="industry-tag">{{ solution.targetIndustry }}</span>
        <h1>{{ solution.name }}</h1>
        <p class="description">{{ solution.description }}</p>
        
        <div class="meta-grid">
          <div class="meta-card">
            <div class="meta-icon">🏗️</div>
            <div class="meta-content">
              <span class="meta-label">架构</span>
              <span class="meta-value">{{ solution.architecture }}</span>
            </div>
          </div>
          <div class="meta-card">
            <div class="meta-icon">📅</div>
            <div class="meta-content">
              <span class="meta-label">预计周期</span>
              <span class="meta-value">{{ solution.estimatedDays }} 天</span>
            </div>
          </div>
          <div class="meta-card">
            <div class="meta-icon">💰</div>
            <div class="meta-content">
              <span class="meta-label">价格范围</span>
              <span class="meta-value">{{ solution.priceRange }}</span>
            </div>
          </div>
          <div class="meta-card">
            <div class="meta-icon">🎯</div>
            <div class="meta-content">
              <span class="meta-label">适用场景</span>
              <span class="meta-value">{{ solution.scenarios }}</span>
            </div>
          </div>
        </div>

        <div class="action-buttons">
          <button class="btn btn-primary btn-large" @click="startTrial">
            立即试用
          </button>
          <button class="btn btn-outline btn-large" @click="contactConsult">
            联系咨询
          </button>
          <button class="btn btn-compare btn-large" @click="compareWithOthers">
            对比其他方案
          </button>
        </div>
      </div>

      <!-- 包含产品 -->
      <div class="section" v-if="products.length">
        <h2>包含产品</h2>
        <div class="products-grid">
          <div 
            v-for="product in products" 
            :key="product.id" 
            class="product-card"
            @click="$router.push(`/products/${product.id}`)"
          >
            <h3>{{ product.name }}</h3>
            <p>{{ product.description }}</p>
            <div class="product-footer">
              <span class="provider">{{ product.providerName }}</span>
              <span class="price">¥{{ product.price }}/月</span>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-else class="error">
      方案不存在
    </div>
  </div>
</template>

<script>
import { solutionAPI } from '@/api'

export default {
  name: 'SolutionDetail',
  data() {
    return {
      solution: null,
      products: [],
      loading: false
    }
  },
  mounted() {
    this.loadSolution()
  },
  methods: {
    async loadSolution() {
      this.loading = true
      try {
        const id = this.$route.params.id
        const res = await solutionAPI.detail(id)
        this.solution = res.data.data
        this.products = res.data.products || []
      } catch (error) {
        console.error('加载方案详情失败:', error)
      } finally {
        this.loading = false
      }
    },
    startTrial() {
      this.$router.push({ path: '/ai-chat', query: { solutionId: this.solution.id } })
    },
    contactConsult() {
      this.$message.info('请联系客服：400-888-8888')
    },
    compareWithOthers() {
      this.$router.push({ path: '/solutions/compare', query: { ids: this.solution.id } })
    }
  }
}
</script>

<style scoped>
.solution-detail {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.back-btn {
  background: none;
  border: none;
  color: #1890ff;
  cursor: pointer;
  font-size: 14px;
}

.loading {
  text-align: center;
  padding: 60px;
}

.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #1890ff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.solution-info {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 32px;
}

.industry-tag {
  display: inline-block;
  padding: 4px 12px;
  background: #e6f7ff;
  color: #1890ff;
  border-radius: 4px;
  font-size: 12px;
  margin-bottom: 16px;
}

.solution-info h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 16px;
}

.solution-info .description {
  font-size: 16px;
  color: #666;
  line-height: 1.8;
  margin-bottom: 24px;
}

.meta-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 32px;
}

.meta-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.meta-icon {
  font-size: 24px;
}

.meta-content {
  display: flex;
  flex-direction: column;
}

.meta-label {
  font-size: 12px;
  color: #999;
}

.meta-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.btn {
  padding: 12px 32px;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-large {
  padding: 14px 48px;
}

.btn-primary {
  background: #1890ff;
  color: #fff;
  border: none;
}

.btn-primary:hover {
  background: #40a9ff;
}

.btn-outline {
  background: #fff;
  color: #1890ff;
  border: 1px solid #1890ff;
}

.btn-outline:hover {
  background: #e6f7ff;
}

.btn-compare {
  background: #fff;
  color: #52c41a;
  border: 1px solid #52c41a;
}

.btn-compare:hover {
  background: #f6ffed;
}

.section {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
}

.section h2 {
  font-size: 20px;
  color: #333;
  margin-bottom: 24px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.product-card {
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.product-card:hover {
  border-color: #1890ff;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.1);
}

.product-card h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.product-card p {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.provider {
  color: #999;
}

.price {
  color: #ff4d4f;
  font-weight: 500;
}

.error {
  text-align: center;
  padding: 60px;
  color: #999;
}
</style>
