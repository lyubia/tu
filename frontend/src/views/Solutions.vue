<template>
  <div class="solutions-page">
    <div class="page-header">
      <h1>解决方案</h1>
      <p class="subtitle">为您量身定制的行业解决方案</p>
    </div>

    <!-- 行业筛选 -->
    <div class="filter-bar">
      <button 
        v-for="ind in industries" 
        :key="ind"
        class="filter-btn"
        :class="{ active: selectedIndustry === ind }"
        @click="selectIndustry(ind)"
      >
        {{ ind }}
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 方案列表 -->
    <div v-else class="solutions-grid">
      <div 
        v-for="solution in solutions" 
        :key="solution.id" 
        class="solution-card"
        @click="goToDetail(solution.id)"
      >
        <div class="solution-header">
          <span class="industry-tag">{{ solution.targetIndustry }}</span>
        </div>
        <h3>{{ solution.name }}</h3>
        <p class="description">{{ solution.description }}</p>
        <div class="solution-meta">
          <div class="meta-item">
            <span class="label">适用场景</span>
            <span class="value">{{ solution.scenarios }}</span>
          </div>
          <div class="meta-item">
            <span class="label">预计周期</span>
            <span class="value">{{ solution.estimatedDays }}天</span>
          </div>
          <div class="meta-item">
            <span class="label">价格范围</span>
            <span class="value">{{ solution.priceRange }}</span>
          </div>
        </div>
        <div class="solution-products" v-if="solution.products && solution.products.length">
          <span class="products-label">包含产品：</span>
          <span 
            v-for="product in solution.products.slice(0, 3)" 
            :key="product.id" 
            class="product-tag"
          >
            {{ product.name }}
          </span>
          <span v-if="solution.products.length > 3" class="more-products">
            +{{ solution.products.length - 3 }} 更多
          </span>
        </div>
        <div class="card-actions">
          <button class="btn btn-primary">查看详情</button>
          <button class="btn btn-outline" @click.stop="startTrial(solution)">申请试用</button>
          <button class="btn btn-compare" @click.stop="compareSolution(solution)">对比</button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && solutions.length === 0" class="empty-state">
      <p>暂无解决方案</p>
    </div>
  </div>
</template>

<script>
import { solutionAPI } from '@/api'

export default {
  name: 'Solutions',
  data() {
    return {
      solutions: [],
      industries: [],
      selectedIndustry: '',
      loading: false
    }
  },
  mounted() {
    this.loadSolutions()
    this.loadIndustries()
  },
  methods: {
    async loadSolutions() {
      this.loading = true
      try {
        const params = this.selectedIndustry ? { industry: this.selectedIndustry } : {}
        const res = await solutionAPI.list(params)
        this.solutions = res.data.data || []
      } catch (error) {
        console.error('加载方案失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadIndustries() {
      try {
        const res = await solutionAPI.industries()
        this.industries = ['全部', ...(res.data.data || [])]
      } catch (error) {
        console.error('加载行业失败:', error)
      }
    },
    selectIndustry(industry) {
      this.selectedIndustry = industry === '全部' ? '' : industry
      this.loadSolutions()
    },
    goToDetail(id) {
      this.$router.push(`/solutions/${id}`)
    },
    startTrial(solution) {
      this.$router.push({ path: '/ai-chat', query: { solutionId: solution.id } })
    },
    compareSolution(solution) {
      this.$router.push({ path: '/solutions/compare', query: { ids: solution.id } })
    }
  }
}
</script>

<style scoped>
.solutions-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 8px;
}

.subtitle {
  color: #666;
  font-size: 16px;
}

.filter-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 24px;
  justify-content: center;
}

.filter-btn {
  padding: 8px 20px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.filter-btn.active {
  background: #1890ff;
  border-color: #1890ff;
  color: #fff;
}

.solutions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
}

.solution-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  cursor: pointer;
  transition: all 0.3s;
}

.solution-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
}

.solution-header {
  margin-bottom: 12px;
}

.industry-tag {
  display: inline-block;
  padding: 4px 12px;
  background: #e6f7ff;
  color: #1890ff;
  border-radius: 4px;
  font-size: 12px;
}

.solution-card h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
}

.description {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.solution-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 16px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.meta-item {
  flex: 1;
  min-width: 80px;
}

.meta-item .label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.meta-item .value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.solution-products {
  margin-bottom: 16px;
}

.products-label {
  font-size: 12px;
  color: #999;
}

.product-tag {
  display: inline-block;
  padding: 2px 8px;
  background: #f0f0f0;
  border-radius: 4px;
  font-size: 12px;
  margin-left: 4px;
  margin-bottom: 4px;
}

.more-products {
  font-size: 12px;
  color: #1890ff;
  margin-left: 4px;
}

.card-actions {
  display: flex;
  gap: 12px;
}

.btn {
  flex: 1;
  padding: 10px 16px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
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

.loading, .empty-state {
  text-align: center;
  padding: 60px;
  color: #999;
}

.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto 16px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #1890ff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
