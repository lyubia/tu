<template>
  <div class="recommendation-center">
    <div class="page-header">
      <h1>🎯 推荐中心</h1>
      <p class="subtitle">基于您的需求，为您推荐的解决方案</p>
    </div>

    <!-- 筛选 -->
    <div class="filter-bar">
      <button 
        v-for="f in filters" 
        :key="f.key"
        class="filter-btn"
        :class="{ active: activeFilter === f.key }"
        @click="activeFilter = f.key"
      >
        {{ f.name }}
      </button>
    </div>

    <!-- 推荐列表 -->
    <div class="recommendation-list">
      <div v-for="rec in recommendations" :key="rec.id" class="recommendation-card">
        <div class="rec-header">
          <span class="rec-rank">Top {{ rec.rank }}</span>
          <span class="rec-match">{{ rec.matchRate }}% 匹配</span>
        </div>
        
        <div class="rec-content">
          <h3>{{ rec.name }}</h3>
          <p class="rec-desc">{{ rec.description }}</p>
          
          <div class="rec-metrics">
            <div class="metric">
              <span class="metric-label">实施周期</span>
              <span class="metric-value">{{ rec.timeline }}天</span>
            </div>
            <div class="metric">
              <span class="metric-label">价格范围</span>
              <span class="metric-value">{{ rec.priceRange }}</span>
            </div>
            <div class="metric">
              <span class="metric-label">匹配度</span>
              <span class="metric-value">{{ rec.matchRate }}%</span>
            </div>
          </div>

          <div class="rec-products">
            <span class="products-label">包含产品：</span>
            <span v-for="p in rec.products" :key="p.id" class="product-tag" @click="$router.push(`/products/${p.id}`)">
              {{ p.name }}
            </span>
          </div>

          <div class="rec-highlights">
            <span class="highlight-label">推荐理由：</span>
            <span class="highlight-item" v-for="h in rec.highlights" :key="h">{{ h }}</span>
          </div>
        </div>

        <div class="rec-actions">
          <button class="btn btn-primary" @click="viewDetail(rec)">查看详情</button>
          <button class="btn" @click="compare(rec)">对比</button>
          <button class="btn btn-outline" @click="startTrial(rec)">申请试用</button>
        </div>
      </div>
    </div>

    <!-- 对比栏 -->
    <div v-if="compareList.length > 0" class="compare-bar">
      <div class="compare-info">
        <span>已选择 {{ compareList.length }} 个方案对比</span>
        <button class="btn" @click="clearCompare">清空</button>
      </div>
      <button class="btn btn-primary" @click="goToCompare">开始对比</button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'RecommendationCenter',
  setup() {
    const activeFilter = ref('all')

    const filters = [
      { key: 'all', name: '全部' },
      { key: '物流', name: '物流' },
      { key: '电商', name: '电商' },
      { key: '制造', name: '制造' }
    ]

    const recommendations = ref([
      {
        id: 1,
        rank: 1,
        name: '智慧物流解决方案',
        description: '面向物流企业的端到端数字化方案，覆盖仓储、运输、配送全流程',
        timeline: 90,
        priceRange: '50-200万',
        matchRate: 95,
        industry: '物流',
        products: [
          { id: 1, name: '智能仓储管理系统' },
          { id: 2, name: '物流运输管理平台' }
        ],
        highlights: ['匹配仓储+运输需求', '支持多式联运', '高性价比']
      },
      {
        id: 2,
        rank: 2,
        name: '供应链协同方案',
        description: '连接供应商、制造商、经销商的供应链协同平台',
        timeline: 60,
        priceRange: '30-100万',
        matchRate: 82,
        industry: '物流',
        products: [
          { id: 3, name: '供应链协同平台' },
          { id: 5, name: '数据分析平台' }
        ],
        highlights: ['供应链可视化', '智能采购管理']
      }
    ])

    const compareList = ref([])

    const compare = (rec) => {
      if (!compareList.value.find(r => r.id === rec.id)) {
        compareList.value.push(rec)
      }
    }

    const clearCompare = () => {
      compareList.value = []
    }

    const goToCompare = () => {
      const ids = compareList.value.map(r => r.id).join(',')
      console.log('跳转对比:', ids)
    }

    const viewDetail = (rec) => {
      console.log('查看详情:', rec)
    }

    const startTrial = (rec) => {
      console.log('申请试用:', rec)
    }

    return {
      activeFilter, filters, recommendations, compareList,
      compare, clearCompare, goToCompare, viewDetail, startTrial
    }
  }
}
</script>

<style scoped>
.recommendation-center { max-width: 1000px; margin: 0 auto; padding-bottom: 80px; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 28px; color: #333; margin-bottom: 8px; }
.subtitle { color: #666; }
.filter-bar { display: flex; gap: 12px; margin-bottom: 24px; }
.filter-btn { padding: 8px 20px; border: 1px solid #ddd; border-radius: 20px; background: #fff; cursor: pointer; }
.filter-btn.active { background: #1890ff; border-color: #1890ff; color: #fff; }
.recommendation-list { display: flex; flex-direction: column; gap: 20px; }
.recommendation-card { background: #fff; border-radius: 12px; padding: 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.rec-header { display: flex; justify-content: space-between; margin-bottom: 16px; }
.rec-rank { font-size: 18px; font-weight: 600; color: #1890ff; }
.rec-match { padding: 4px 12px; background: #f6ffed; color: #52c41a; border-radius: 4px; font-size: 14px; }
.rec-content h3 { font-size: 20px; margin-bottom: 8px; }
.rec-desc { color: #666; margin-bottom: 16px; }
.rec-metrics { display: flex; gap: 24px; margin-bottom: 16px; }
.metric { display: flex; flex-direction: column; }
.metric-label { font-size: 12px; color: #999; }
.metric-value { font-size: 16px; font-weight: 500; color: #333; }
.rec-products { margin-bottom: 12px; }
.products-label { font-size: 12px; color: #999; }
.product-tag { display: inline-block; padding: 4px 12px; background: #e6f7ff; color: #1890ff; border-radius: 4px; font-size: 12px; margin-left: 8px; cursor: pointer; }
.rec-highlights { margin-bottom: 16px; }
.highlight-label { font-size: 12px; color: #999; }
.highlight-item { display: inline-block; padding: 4px 10px; background: #fff7e6; color: #fa8c16; border-radius: 4px; font-size: 12px; margin-left: 8px; }
.rec-actions { display: flex; gap: 12px; padding-top: 16px; border-top: 1px solid #f0f0f0; }
.btn { padding: 10px 20px; border-radius: 6px; cursor: pointer; border: none; font-size: 14px; }
.btn-primary { background: #1890ff; color: #fff; }
.btn-outline { background: #fff; border: 1px solid #ddd; }
.compare-bar { position: fixed; bottom: 0; left: 0; right: 0; background: #fff; padding: 16px 24px; box-shadow: 0 -2px 8px rgba(0,0,0,0.1); display: flex; justify-content: space-between; align-items: center; }
.compare-info { display: flex; align-items: center; gap: 12px; }
</style>
