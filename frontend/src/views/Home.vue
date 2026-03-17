<template>
  <div class="home">
    <div class="portal">
      <div class="portal-grid">
        <section class="portal-block span-12">
          <div class="hero">
            <div class="hero-content">
              <h1 class="hero-title">智能物流解决方案平台</h1>
              <p class="hero-subtitle">AI 驱动的智能推荐，让选型变得更简单</p>
              <div class="hero-actions">
                <button class="btn btn-primary" @click="openAIChat">开始智能选型</button>
                <button class="btn btn-secondary" @click="$router.push('/trial')">免费试用</button>
              </div>
            </div>
          </div>
        </section>

        <section class="portal-block span-12">
          <div class="quick-entry">
            <div class="entry-card" @click="openAIChat">
              <div class="entry-icon">🤖</div>
              <div class="entry-text">
                <h3>AI 顾问</h3>
                <p>智能推荐方案</p>
              </div>
            </div>
            <div class="entry-card" @click="$router.push('/trial')">
              <div class="entry-icon">🧪</div>
              <div class="entry-text">
                <h3>试用工作台</h3>
                <p>在线体验产品</p>
              </div>
            </div>
          </div>
        </section>

        <!-- 猜你喜欢模块 -->
        <section class="portal-block span-12">
          <div class="section">
            <div class="section-header">
              <h2>猜你喜欢 <span v-if="userIndustry" class="industry-tag">{{ userIndustry }}</span></h2>
              <router-link to="/products" class="more-link">查看更多 →</router-link>
            </div>
            <div v-if="loading" class="loading">加载中...</div>
            <div v-else-if="recommendProducts.length === 0" class="empty">暂无推荐产品</div>
            <div v-else class="product-grid">
              <ProductCard
                v-for="product in recommendProducts"
                :key="product.id"
                :product="product"
                :show-try="true"
                @select="openProduct"
              />
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { productAPI } from '../api'
import ProductCard from '../components/ProductCard.vue'

const router = useRouter()
const recommendProducts = ref([])
const loading = ref(true)
const userIndustry = ref('')

// 获取当前用户行业
function getUserIndustry() {
  try {
    const userStr = localStorage.getItem('user')
    if (userStr) {
      const user = JSON.parse(userStr)
      userIndustry.value = user.industry || ''
    }
  } catch (e) {
    console.error('获取用户信息失败:', e)
  }
}

// 打开AI聊天
function openAIChat() {
  window.dispatchEvent(new CustomEvent('open-ai-chat'))
}

// 加载推荐产品
async function loadRecommendations() {
  loading.value = true
  try {
    // 根据用户行业获取推荐，无行业则获取热门产品
    let res
    if (userIndustry.value) {
      res = await productAPI.list({ industry: userIndustry.value, limit: 8 })
    } else {
      res = await productAPI.popular(8)
    }
    
    // 如果API返回数据，使用API数据；否则使用模拟数据
    if (res.data.data && res.data.data.length > 0) {
      recommendProducts.value = res.data.data
    } else {
      // 使用模拟数据展示
      recommendProducts.value = getMockProducts()
    }
  } catch (e) {
    console.error('加载推荐产品失败:', e)
    // 使用模拟数据
    recommendProducts.value = getMockProducts()
  } finally {
    loading.value = false
  }
}

// 模拟产品数据
function getMockProducts() {
  const industryProducts = {
    '快递物流': [
      { id: 1, name: '智能快递柜系统', description: '末端配送智能解决方案', industry: '快递物流' },
      { id: 2, name: '快递轨迹追踪平台', description: '全链路物流可视化', industry: '快递物流' },
      { id: 3, name: '智能分拣系统', description: '自动分拣提高效率', industry: '快递物流' },
    ],
    '专线运输': [
      { id: 4, name: '专线物流管理系统', description: '专线运输全流程管理', industry: '专线运输' },
      { id: 5, name: '车辆调度平台', description: '智能调度降低空驶率', industry: '专线运输' },
    ],
    '仓储服务': [
      { id: 6, name: 'WMS仓储管理系统', description: '智能化仓储解决方案', industry: '仓储服务' },
      { id: 7, name: '库存预警系统', description: '实时库存监控预警', industry: '仓储服务' },
    ],
    '跨境物流': [
      { id: 8, name: '跨境报关系统', description: '一键通关解决方案', industry: '跨境物流' },
      { id: 9, name: '海外仓管理', description: '海外仓储配送一体化', industry: '跨境物流' },
    ],
    '冷链物流': [
      { id: 10, name: '冷链温控系统', description: '全程温湿度监控', industry: '冷链物流' },
      { id: 11, name: '冷链追溯平台', description: '食品医药全程追溯', industry: '冷链物流' },
    ],
    '物流设备': [
      { id: 12, name: 'AGV搬运机器人', description: '无人化智能搬运', industry: '物流设备' },
      { id: 13, name: '智能货架系统', description: '自动化仓储货架', industry: '物流设备' },
    ]
  }
  
  const hotProducts = [
    { id: 101, name: 'TMS运输管理系统', description: '运输全流程数字化', industry: '物流' },
    { id: 102, name: '物流大数据分析平台', description: '智能决策支持', industry: '物流' },
    { id: 103, name: '智能调度系统', description: 'AI优化路线规划', industry: '物流' },
    { id: 104, name: '电子合同签约平台', description: '物流电子合同', industry: '物流' },
  ]
  
  // 如果有行业，返回对应行业产品；否则返回热门
  if (userIndustry.value && industryProducts[userIndustry.value]) {
    return industryProducts[userIndustry.value]
  }
  return hotProducts
}

onMounted(() => {
  getUserIndustry()
  loadRecommendations()
  
  // 监听AI对话结束事件，刷新推荐
  window.addEventListener('refresh-recommendations', loadRecommendations)
  
  // 检查是否需要刷新（从注册页或AI对话回来）
  const needsRefresh = localStorage.getItem('recommendations-needs-refresh')
  if (needsRefresh === 'true') {
    localStorage.removeItem('recommendations-needs-refresh')
    loadRecommendations()
  }
})

onUnmounted(() => {
  window.removeEventListener('refresh-recommendations', loadRecommendations)
})

function openProduct(product) {
  if (!product?.id) return
  router.push(`/products/${product.id}`)
}
</script>

<style scoped>
.portal { display: flex; flex-direction: column; gap: 20px; }
.portal-grid { display: grid; grid-template-columns: repeat(12, 1fr); gap: 20px; }
.portal-block { grid-column: span 12; }

.hero { text-align: center; padding: 60px 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; }
.hero-title { color: #fff; font-size: 36px; margin-bottom: 12px; }
.hero-subtitle { color: rgba(255,255,255,0.9); font-size: 18px; margin-bottom: 32px; }
.hero-actions { display: flex; gap: 16px; justify-content: center; }
.btn { padding: 12px 32px; border-radius: 8px; font-size: 16px; cursor: pointer; border: none; transition: all 0.2s; }
.btn-primary { background: #fff; color: #667eea; }
.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
.btn-secondary { background: rgba(255,255,255,0.2); color: #fff; border: 1px solid #fff; }
.btn-secondary:hover { background: rgba(255,255,255,0.3); }

.quick-entry { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; max-width: 800px; margin: 0 auto; }
.entry-card { background: #fff; padding: 24px; border-radius: 12px; display: flex; align-items: center; gap: 16px; cursor: pointer; transition: all 0.2s; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.entry-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.08); }
.entry-icon { font-size: 36px; }
.entry-text h3 { font-size: 16px; margin-bottom: 4px; }
.entry-text p { color: #999; font-size: 14px; }

@media (max-width: 768px) {
  .quick-entry { grid-template-columns: 1fr; }
}

.section { background: #fff; padding: 24px; border-radius: 12px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.section-header h2 { font-size: 20px; margin: 0; display: flex; align-items: center; gap: 10px; }
.industry-tag { font-size: 12px; padding: 4px 10px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff; border-radius: 12px; font-weight: normal; }
.more-link { color: #0066ff; text-decoration: none; font-size: 14px; }
.more-link:hover { text-decoration: underline; }

.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
@media (max-width: 1024px) { .product-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .product-grid { grid-template-columns: 1fr; } }

.loading, .empty { text-align: center; padding: 40px; color: #999; }
</style>
