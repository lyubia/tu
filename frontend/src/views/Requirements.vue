<template>
  <div class="requirements-page">
    <div class="page-header">
      <h1>📋 我的需求库</h1>
      <p>查看和管理您的历史需求记录</p>
    </div>

    <!-- 需求列表 -->
    <div class="section">
      <div class="section-header">
        <h2>需求历史</h2>
        <span class="count">共 {{ requirements.length }} 条记录</span>
      </div>

      <div class="requirements-list" v-if="requirements.length > 0">
        <div 
          v-for="req in requirements" 
          :key="req.id" 
          class="requirement-card"
          @click="viewDetail(req)"
        >
          <div class="req-header">
            <div class="req-title">{{ req.title || '需求记录' }}</div>
            <span class="req-time">{{ formatDate(req.createdAt) }}</span>
          </div>
          
          <div class="req-tags">
            <span v-if="req.industry" class="tag">{{ req.industry }}</span>
            <span v-if="req.scenario" class="tag">{{ req.scenario }}</span>
            <span v-if="req.budget" class="tag">{{ req.budget }}</span>
          </div>
          
          <div v-if="req.summary" class="req-summary">
            {{ req.summary }}
          </div>
          
          <div class="req-footer">
            <span class="conversation-id">对话ID: {{ req.conversationId }}</span>
            <button class="btn-detail">查看详情 →</button>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-icon">📋</div>
        <p>暂无需求记录</p>
        <router-link to="/trial" class="btn-start">开始对话创建需求</router-link>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="showDetailModal = false">
      <div class="modal modal-lg" @click.stop>
        <div class="modal-header">
          <h3>需求详情</h3>
          <button class="close-btn" @click="showDetailModal = false">×</button>
        </div>
        
        <div class="modal-body" v-if="currentDetail">
          <div class="detail-section">
            <h4>基本信息</h4>
            <div class="detail-grid">
              <div class="detail-item">
                <label>行业</label>
                <span>{{ currentDetail.industry || '-' }}</span>
              </div>
              <div class="detail-item">
                <label>场景</label>
                <span>{{ currentDetail.scenario || '-' }}</span>
              </div>
              <div class="detail-item">
                <label>预算</label>
                <span>{{ currentDetail.budget || '-' }}</span>
              </div>
              <div class="detail-item">
                <label>创建时间</label>
                <span>{{ formatDateTime(currentDetail.createdAt) }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="currentDetail.products?.length" class="detail-section">
            <h4>推荐产品</h4>
            <div class="product-list">
              <div v-for="p in currentDetail.products" :key="p.id" class="product-item">
                <span class="product-name">{{ p.name }}</span>
                <span class="product-category">{{ p.category }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="currentDetail.summary" class="detail-section">
            <h4>需求摘要</h4>
            <p class="detail-summary">{{ currentDetail.summary }}</p>
          </div>
        </div>
        
        <div class="modal-footer">
          <button class="btn-cancel" @click="showDetailModal = false">关闭</button>
          <button class="btn-confirm" @click="startNewTrial">基于此需求创建试用</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { conversationAPI, requirementAPI } from '../api'

const router = useRouter()

const requirements = ref([])
const showDetailModal = ref(false)
const currentDetail = ref(null)
const userId = 1 // 默认用户ID

onMounted(async () => {
  await loadRequirements()
})

async function loadRequirements() {
  try {
    // 获取用户所有对话
    const historyRes = await conversationAPI.history(userId)
    const conversations = historyRes.data.data || []
    
    // 获取每个对话的需求
    const allRequirements = []
    for (const conv of conversations) {
      try {
        const reqRes = await requirementAPI.list(conv.id)
        const reqs = reqRes.data.data || []
        for (const req of reqs) {
          allRequirements.push({
            ...req,
            conversationId: conv.id,
            title: req.industry ? `${req.industry}行业需求` : '需求记录'
          })
        }
      } catch (e) {
        console.error('获取需求失败:', e)
      }
    }
    
    // 按时间倒序排列
    requirements.value = allRequirements.sort((a, b) => 
      new Date(b.createdAt) - new Date(a.createdAt)
    )
  } catch (e) {
    console.error('加载需求失败:', e)
  }
}

function viewDetail(req) {
  currentDetail.value = req
  showDetailModal.value = true
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

function formatDateTime(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

function startNewTrial() {
  if (currentDetail.value) {
    // 将需求信息存储到本地，供试用页面使用
    localStorage.setItem('trialRequirements', JSON.stringify({
      industry: currentDetail.value.industry,
      scenario: currentDetail.value.scenario,
      budget: currentDetail.value.budget
    }))
  }
  showDetailModal.value = false
  router.push('/trial')
}
</script>

<style scoped>
.requirements-page { padding: 0; }

.page-header { margin-bottom: 32px; }
.page-header h1 { font-size: 28px; margin-bottom: 8px; }
.page-header p { color: #666; }

.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.section-header h2 { font-size: 20px; }
.count { color: #999; font-size: 14px; }

.requirements-list { display: flex; flex-direction: column; gap: 16px; }

.requirement-card { 
  background: #fff; 
  padding: 20px; 
  border-radius: 12px; 
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.requirement-card:hover { 
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  transform: translateY(-2px);
}

.req-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.req-title { font-size: 16px; font-weight: 600; }
.req-time { font-size: 13px; color: #999; }

.req-tags { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 12px; }
.tag { font-size: 12px; color: #0066ff; background: #e8f4ff; padding: 4px 10px; border-radius: 12px; }

.req-summary { font-size: 14px; color: #666; line-height: 1.5; margin-bottom: 12px; }

.req-footer { display: flex; justify-content: space-between; align-items: center; padding-top: 12px; border-top: 1px solid #f0f0f0; }
.conversation-id { font-size: 12px; color: #999; }
.btn-detail { background: none; border: none; color: #0066ff; font-size: 13px; cursor: pointer; }

.empty-state { text-align: center; padding: 60px; background: #fff; border-radius: 12px; }
.empty-icon { font-size: 48px; margin-bottom: 16px; }
.empty-state p { color: #999; margin-bottom: 24px; }
.btn-start { display: inline-block; padding: 12px 24px; background: #0066ff; color: #fff; text-decoration: none; border-radius: 8px; }

/* 弹窗样式 */
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: #fff; border-radius: 12px; width: 90%; max-width: 500px; max-height: 90vh; overflow-y: auto; }
.modal-lg { max-width: 600px; }
.modal-header { padding: 20px 24px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center; }
.modal-header h3 { font-size: 18px; }
.close-btn { background: none; border: none; font-size: 24px; cursor: pointer; color: #999; }
.modal-body { padding: 24px; }
.modal-footer { padding: 16px 24px; border-top: 1px solid #f0f0f0; display: flex; justify-content: flex-end; gap: 12px; }

.detail-section { margin-bottom: 24px; }
.detail-section h4 { font-size: 15px; font-weight: 600; margin-bottom: 12px; color: #333; }

.detail-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.detail-item { display: flex; flex-direction: column; gap: 4px; }
.detail-item label { font-size: 12px; color: #999; }
.detail-item span { font-size: 14px; color: #333; }

.product-list { display: flex; flex-direction: column; gap: 8px; }
.product-item { display: flex; justify-content: space-between; padding: 10px; background: #f9fafb; border-radius: 8px; }
.product-name { font-size: 14px; font-weight: 500; }
.product-category { font-size: 12px; color: #999; }

.detail-summary { font-size: 14px; line-height: 1.6; color: #666; }

.btn-cancel { padding: 10px 20px; border: 1px solid #e0e0e0; background: #fff; border-radius: 8px; cursor: pointer; }
.btn-confirm { padding: 10px 20px; background: #0066ff; color: #fff; border: none; border-radius: 8px; cursor: pointer; }
</style>
