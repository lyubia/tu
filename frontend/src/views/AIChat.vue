<template>
  <div class="ai-chat">
    <div class="chat-container">
      <div class="chat-sidebar">
        <h3>💡 AI 智能顾问</h3>
        <p class="chat-intro">告诉我您的需求，AI 将为您推荐合适的产品和方案</p>
        
        <!-- 需求清单组件 -->
        <RequirementForm 
          :requirements="currentRequirements" 
          @generate="finishAndGenerate"
        />
        
        <div class="quick-questions">
          <h4>快捷入口</h4>
          <button v-for="q in quickQuestions" :key="q.label" class="quick-btn" @click="sendQuickQuestion(q.text)">
            {{ q.label }}
          </button>
        </div>

        <div class="quick-questions">
          <h4>快捷操作</h4>
          <button class="quick-btn finish-btn" @click="finishAndGenerate" :disabled="loading">
            结束对话并生成方案
          </button>
        </div>
      </div>
      
      <div class="chat-main">
        <div class="messages" ref="messagesRef">
          <div v-for="(msg, index) in messages" :key="index" class="message" :class="msg.role">
            <div class="message-avatar">
              {{ msg.role === 'user' ? '👤' : '🤖' }}
            </div>
            <div class="message-content">
              <div class="message-text" v-html="formatMessage(msg.content)"></div>

              <div v-if="msg.requirements && Object.keys(msg.requirements).length" class="requirements">
                <div class="requirements-title">需求要点</div>
                <div class="requirements-list">
                  <span v-for="(v, k) in msg.requirements" :key="k" class="req-tag">
                    {{ requirementLabel(k) }}：{{ v }}
                  </span>
                </div>
              </div>
              
              <div v-if="msg.recommendedProducts?.length" class="recommend-section">
                <h4>当前推荐/热门产品</h4>
                <div class="product-grid">
                  <ProductCard
                    v-for="p in msg.recommendedProducts"
                    :key="p.id"
                    :product="p"
                    :showTry="true"
                    :showFeedback="true"
                    @select="goProduct"
                    @try="(product) => startTrial(product, null)"
                    @feedback="(product, type) => submitFeedback(product, type)"
                  />
                </div>
              </div>
              
              <div v-if="msg.recommendedSolutions?.length" class="recommend-section">
                <h4>推荐方案</h4>
                <div class="solution-list">
                  <div v-for="s in msg.recommendedSolutions" :key="s.id" class="solution-item">
                    <div class="solution-name">{{ s.name }}</div>
                    <div class="solution-info">{{ s.description }}</div>
                    <div class="solution-meta">
                      <span>⏱ {{ s.estimatedDays }}天</span>
                      <span>💰 {{ s.priceRange }}</span>
                    </div>
                    <div class="solution-actions">
                      <button class="feedback-btn interested" @click="submitFeedback({id: s.id, name: s.name}, 'INTERESTED')">感兴趣</button>
                      <button class="feedback-btn not-match" @click="submitFeedback({id: s.id, name: s.name}, 'NOT_MATCH')">不匹配</button>
                    </div>
                  </div>
                </div>
              </div>

              <div v-if="msg.bundles?.length" class="recommend-section">
                <h4>可对比方案（选择后可直接试用）</h4>
                <div class="bundle-grid">
                  <div v-for="b in msg.bundles" :key="b.solution?.id" class="bundle-card">
                    <div class="bundle-header">
                      <div class="bundle-name">{{ b.solution?.name }}</div>
                      <div class="bundle-meta">
                        <span v-if="b.solution?.estimatedDays != null">⏱ {{ b.solution.estimatedDays }}天</span>
                        <span v-if="b.solution?.priceRange">💰 {{ b.solution.priceRange }}</span>
                      </div>
                    </div>
                    <div v-if="b.solution?.description" class="bundle-desc">{{ b.solution.description }}</div>
                    <div v-if="b.highlights && Object.keys(b.highlights).length" class="bundle-highlights">
                      <span v-for="(v, k) in b.highlights" :key="k" class="highlight-tag">{{ k }}：{{ v }}</span>
                    </div>
                    <div class="bundle-actions">
                      <button class="btn-bundle" type="button" @click="chooseBundle(b)">
                        选择这套方案
                      </button>
                    </div>
                    <div v-if="b.products?.length" class="bundle-products">
                      <div class="bundle-products-title">可试用产品</div>
                      <div class="product-grid">
                        <ProductCard
                          v-for="p in b.products"
                          :key="p.id"
                          :product="p"
                          :showTry="true"
                          @select="goProduct"
                          @try="(product) => startTrial(product, b.solution?.id || null)"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div v-if="msg.nextQuestion" class="next-question">
                <button class="next-question-btn" @click="sendMessage(msg.nextQuestion)">
                  {{ msg.nextQuestion }} →
                </button>
              </div>
            </div>
          </div>
          
          <div v-if="loading" class="message assistant">
            <div class="message-avatar">🤖</div>
            <div class="message-content">
              <div class="typing">
                <span class="dot"></span>
                <span class="dot"></span>
                <span class="dot"></span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="chat-input">
          <input 
            v-model="inputMessage" 
            type="text" 
            placeholder="请描述您的需求，如：我是物流企业，需要仓储管理解决方案"
            @keyup.enter="sendMessage"
            :disabled="loading"
          />
          <button @click="sendMessage" :disabled="loading || !inputMessage.trim()">
            {{ loading ? '发送中...' : '发送' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { aiAPI, feedbackAPI, conversationAPI, messageAPI } from '../api'
import ProductCard from '../components/ProductCard.vue'
import RequirementForm from '../components/RequirementForm.vue'

// 首次用户开场白
const firstTimeGreeting = {
  role: 'assistant',
  content: `您好！欢迎体验智能选型顾问 🤖\n\n我是您的专属AI顾问，可以帮您推荐最合适的物流管理系统。\n\n请先告诉我您所在的**行业**，或者直接点击下方的快捷入口了解我们的解决方案：`
}

// 老用户开场白（基于历史）
const returningGreeting = (lastTopic) => ({
  role: 'assistant',
  content: `您好！很高兴再次见到您 👋\n\n上次您提到${lastTopic}，请问现在有新的进展吗？或者您想了解其他方面的方案？`
})

const messages = ref([
  firstTimeGreeting
])
const inputMessage = ref('')
const loading = ref(false)
const messagesRef = ref(null)
const router = useRouter()

// 对话相关状态
const currentConversationId = ref(null)
const userId = 1 // 默认用户ID
const isReturningUser = ref(false)
const userProfile = ref({
  industry: '',
  scenario: '',
  budget: '',
  scale: '',
  painPoints: []
})

// 收集当前所有需求
const currentRequirements = computed(() => {
  const reqs = {}
  messages.value.forEach(m => {
    if (m.requirements) {
      Object.assign(reqs, m.requirements)
    }
  })
  return reqs
})

const quickQuestions = [
  { label: '🚚 车队管理', text: '我想了解车队管理解决方案' },
  { label: '📦 仓储物流', text: '我想了解仓储管理解决方案' },
  { label: '🛣️ TMS运输', text: '我想了解TMS运输管理系统' },
  { label: '💰 询价预约', text: '我想预约免费咨询' },
  { label: '❓ 常见问题', text: '请告诉我你们能解决什么问题' }
]

onMounted(async () => {
  await initConversation()
  scrollToBottom()
})

// 初始化对话 - 创建新对话或获取最近对话
async function initConversation() {
  // 加载本地保存的用户画像
  loadUserProfile()
  
  try {
    // 获取用户对话历史
    const historyRes = await conversationAPI.history(userId)
    const conversations = historyRes.data.data || []
    
    if (conversations.length > 0) {
      // 有历史对话，加载最近的对话
      const latestConv = conversations[0]
      currentConversationId.value = latestConv.id
      isReturningUser.value = true
      
      // 加载该对话的消息历史
      const messagesRes = await messageAPI.list(latestConv.id)
      const historicalMessages = messagesRes.data.data || []
      
      if (historicalMessages.length > 0) {
        // 从历史消息中提取用户画像信息
        extractProfileFromHistory(historicalMessages)
        
        // 根据是否有历史消息决定显示什么
        const lastUserMsg = historicalMessages
          .filter(m => m.role === 'user')
          .pop()
        
        // 提取最后一个话题
        const lastTopic = lastUserMsg?.content?.slice(0, 20) || '相关需求'
        
        // 设置老用户开场白
        messages.value = [returningGreeting(lastTopic)]
      }
    } else {
      // 没有历史对话，创建新对话
      isReturningUser.value = false
      messages.value = [firstTimeGreeting]
      const createRes = await conversationAPI.create(userId)
      currentConversationId.value = createRes.data.data?.id || createRes.data.id
    }
  } catch (e) {
    console.error('初始化对话失败:', e)
    // 失败时创建新对话
    isReturningUser.value = false
    messages.value = [firstTimeGreeting]
    try {
      const createRes = await conversationAPI.create(userId)
      currentConversationId.value = createRes.data.data?.id || createRes.data.id
    } catch (e2) {
      console.error('创建对话失败:', e2)
    }
  }
}

// 从历史消息中提取用户画像信息
function extractProfileFromHistory(historicalMessages) {
  historicalMessages.forEach(m => {
    if (m.requirements) {
      if (m.requirements.industry) userProfile.value.industry = m.requirements.industry
      if (m.requirements.scenario) userProfile.value.scenario = m.requirements.scenario
      if (m.requirements.budget) userProfile.value.budget = m.requirements.budget
      if (m.requirements.scale) userProfile.value.scale = m.requirements.scale
      if (m.requirements.painPoints) userProfile.value.painPoints = m.requirements.painPoints
    }
  })
  // 保存到本地
  saveUserProfileLocal()
}

async function sendMessage(text = inputMessage.value) {
  if (!text.trim() || loading.value) return
  
  const userMessage = text.trim()
  inputMessage.value = ''
  
  messages.value.push({ role: 'user', content: userMessage })
  scrollToBottom()
  
  loading.value = true
  
  try {
    const res = await aiAPI.chat({
      userId: userId,
      message: userMessage,
      history: messages.value.slice(-6).map(m => ({ role: m.role, content: m.content }))
    })
    
    const data = res.data.data
    
    // 提取并保存用户画像信息
    if (data.requirements) {
      Object.keys(data.requirements).forEach(key => {
        if (data.requirements[key]) {
          userProfile.value[key] = data.requirements[key]
        }
      })
      saveUserProfileLocal()
    }
    
    // 如果AI没有返回追问问题，则检查缺失字段并追问
    let nextQuestion = data.needsMoreInfo ? data.nextQuestion : null
    if (!nextQuestion && data.requirements) {
      nextQuestion = extractAndAsk(data)
    }
    
    const assistantMsg = {
      role: 'assistant',
      content: data.message?.message?.content || data.message?.content || "" || '',
      recommendedProducts: data.recommendedProducts,
      recommendedSolutions: data.recommendedSolutions,
      bundles: data.bundles,
      requirements: data.requirements,
      nextQuestion: nextQuestion
    }
    messages.value.push(assistantMsg)
    
    // 保存用户消息和AI回复到后端
    await saveMessages(userMessage, assistantMsg)
  } catch (e) {
    messages.value.push({
      role: 'assistant',
      content: '抱歉，服务暂时不可用，请稍后重试。'
    })
  }
  
  loading.value = false
  scrollToBottom()
}

// 保存消息到后端
async function saveMessages(userContent, assistantMsg) {
  if (!currentConversationId.value) return
  
  try {
    // 保存用户消息
    await messageAPI.save({
      conversationId: currentConversationId.value,
      role: 'user',
      content: userContent,
      requirements: currentRequirements.value,
      userProfile: userProfile.value
    })
    
    // 保存AI回复
    await messageAPI.save({
      conversationId: currentConversationId.value,
      role: 'assistant',
      content: assistantMsg.content,
      requirements: assistantMsg.requirements,
      recommendedProducts: assistantMsg.recommendedProducts,
      recommendedSolutions: assistantMsg.recommendedSolutions,
      bundles: assistantMsg.bundles,
      nextQuestion: assistantMsg.nextQuestion
    })
  } catch (e) {
    console.error('保存消息失败:', e)
  }
}

function sendQuickQuestion(text) {
  sendMessage(text)
}

async function finishAndGenerate() {
  // 保存当前需求到用户画像
  await updateUserProfile()
  
  // 发送结束对话消息
  sendMessage('请根据以上对话内容，生成适配我的方案，给出1-2套可对比方案，并推荐可试用的产品。')
  
  // 对话发送后，等待回复，然后刷新首页推荐
  setTimeout(() => {
    refreshHomeRecommendations()
  }, 1500)
}

// 更新用户画像 - 从对话中提取行业等信息
async function updateUserProfile() {
  const reqs = currentRequirements.value
  
  // 更新本地画像
  if (reqs.industry) userProfile.value.industry = reqs.industry
  if (reqs.scenario) userProfile.value.scenario = reqs.scenario
  if (reqs.budget) userProfile.value.budget = reqs.budget
  if (reqs.scale) userProfile.value.scale = reqs.scale
  if (reqs.painPoints) userProfile.value.painPoints = reqs.painPoints
  
  saveUserProfileLocal()
  
  // 从需求中提取行业
  if (reqs.industry) {
    try {
      // 获取当前用户
      let user = null
      const userStr = localStorage.getItem('user')
      if (userStr) {
        user = JSON.parse(userStr)
      }
      
      if (user) {
        // 更新本地用户信息
        user.industry = reqs.industry
        localStorage.setItem('user', JSON.stringify(user))
        
        // 调用API更新用户画像（如果后端支持）
        try {
          await axios.post('/api/users/update-industry', {
            userId: user.id,
            industry: reqs.industry,
            tags: reqs
          })
        } catch (e) {
          // API不存在时静默失败，本地已保存
          console.log('用户画像API不可用，使用本地存储')
        }
      }
    } catch (e) {
      console.error('更新用户画像失败:', e)
    }
  }
}

// 本地保存用户画像
function saveUserProfileLocal() {
  localStorage.setItem('ai_chat_user_profile', JSON.stringify(userProfile.value))
}

// 加载本地用户画像
function loadUserProfile() {
  const saved = localStorage.getItem('ai_chat_user_profile')
  if (saved) {
    try {
      userProfile.value = { ...userProfile.value, ...JSON.parse(saved) }
    } catch (e) {
      console.error('加载用户画像失败:', e)
    }
  }
}

// 从对话中动态提取信息并追问
function extractAndAsk(data) {
  const missingFields = []
  const reqs = { ...userProfile.value, ...data.requirements }
  
  // 检查缺失字段
  if (!reqs.scenario && !userProfile.value.scenario) {
    missingFields.push('scenario')
  }
  if (!reqs.budget && !userProfile.value.budget) {
    missingFields.push('budget')
  }
  if (!reqs.scale && !userProfile.value.scale) {
    missingFields.push('scale')
  }
  if (!reqs.painPoints?.length && !userProfile.value.painPoints?.length) {
    missingFields.push('painPoints')
  }
  
  // 每次只问一个问题
  if (missingFields.length > 0) {
    const nextField = missingFields[0]
    const questions = {
      scenario: '请问您主要想解决哪些场景的问题？比如：仓储管理、运输调度、订单处理等',
      budget: '请问您的预算范围是多少？例如：10万以下、10-50万、50万以上',
      scale: '请问您的企业规模是？例如：小型（50人以下）、中型（50-500人）、大型（500人以上）',
      painPoints: '请问您目前最大的痛点是什么？例如：效率低、成本高、数据不准等'
    }
    return questions[nextField] || null
  }
  return null
}

// 刷新首页推荐
function refreshHomeRecommendations() {
  // 发送自定义事件通知首页刷新推荐
  window.dispatchEvent(new CustomEvent('refresh-recommendations'))
  
  // 同时保存标记，下次首页加载时也会刷新
  localStorage.setItem('recommendations-needs-refresh', 'true')
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

function formatMessage(text) {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

function requirementLabel(key) {
  const map = {
    industry: '行业',
    scenario: '场景',
    capability: '能力点',
    budget: '预算',
    scale: '规模',
    version: '版本',
    needCase: '案例'
  }
  return map[key] || key
}

function goProduct(product) {
  router.push(`/products/${product.id}`)
}

function startTrial(product, solutionId) {
  localStorage.setItem('trialStart', JSON.stringify({
    productId: product.id,
    solutionId
  }))
  router.push('/trial')
}

function chooseBundle(bundle) {
  const solutionId = bundle?.solution?.id || null
  localStorage.setItem('selectedBundle', JSON.stringify({
    solutionId,
    time: Date.now()
  }))
}

async function submitFeedback(item, type) {
  try {
    await feedbackAPI.submit({
      userId: 1,
      productId: item.id,
      feedbackType: type,
      feedbackText: ''
    })
    
    messages.value.push({
      role: 'assistant',
      content: `感谢您的反馈！${type === 'INTERESTED' ? '我们会重点推荐这类产品。' : '抱歉这个方案不适合您，让我们继续完善需求。'}`
    })
    scrollToBottom()
  } catch (e) {
    console.error('提交反馈失败:', e)
  }
}
</script>

<style scoped>
.ai-chat { height: calc(100vh - 140px); }

.chat-container { display: grid; grid-template-columns: 280px 1fr; gap: 24px; height: 100%; }

.chat-sidebar { background: #fff; border-radius: 12px; padding: 24px; height: fit-content; }
.chat-sidebar h3 { font-size: 18px; margin-bottom: 12px; }
.chat-intro { color: #666; font-size: 14px; line-height: 1.6; margin-bottom: 24px; }

.quick-questions h4 { font-size: 14px; color: #999; margin-bottom: 12px;  margin-top: 60px; }
.quick-btn { display: block; width: 100%; text-align: left; padding: 10px 12px; background: #f5f7fa; border: none; border-radius: 8px; margin-bottom: 8px; cursor: pointer; font-size: 13px; transition: all 0.2s; }
.quick-btn:hover { background: #e8f4ff; color: #0066ff; }
.quick-btn.finish-btn { background: #f6ffed; color: #52c41a; }
.quick-btn.finish-btn:hover { background: #d9f7be; }

.chat-main { background: #fff; border-radius: 12px; display: flex; flex-direction: column; overflow: hidden; }

.messages { flex: 1; overflow-y: auto; padding: 24px; }

.message { display: flex; gap: 12px; margin-bottom: 20px; }
.message-avatar { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; background: #f5f7fa; border-radius: 50%; font-size: 18px; flex-shrink: 0; }
.message.user { flex-direction: row-reverse; }
.message.user .message-content { align-items: flex-end; }
.message.user .message-avatar { background: #e8f4ff; }
.message.assistant .message-avatar { background: #fef3e2; }

.message-content { display: flex; flex-direction: column; gap: 8px; max-width: 70%; }
.message-text { padding: 12px 16px; border-radius: 12px; line-height: 1.6; font-size: 14px; }
.message.user .message-text { background: #0066ff; color: #fff; }
.message.assistant .message-text { background: #f5f7fa; color: #333; }

.requirements { margin-top: 10px; }
.requirements-title { font-size: 13px; color: #999; margin-bottom: 6px; }
.requirements-list { display: flex; flex-wrap: wrap; gap: 8px; }
.req-tag { font-size: 12px; color: #666; background: #f5f7fa; padding: 4px 8px; border-radius: 999px; }

.recommend-section { margin-top: 12px; }
.recommend-section h4 { font-size: 13px; color: #999; margin-bottom: 8px; }

.product-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }

.solution-list { display: flex; flex-direction: column; gap: 8px; }
.solution-item { padding: 12px; background: #f9fafb; border-radius: 8px; }
.solution-name { font-size: 14px; font-weight: 500; margin-bottom: 4px; }
.solution-info { font-size: 12px; color: #666; margin-bottom: 8px; }
.solution-meta { display: flex; gap: 16px; font-size: 12px; color: #999; }
.solution-actions { display: flex; gap: 8px; margin-top: 12px; }
.feedback-btn { padding: 6px 12px; border-radius: 4px; font-size: 12px; cursor: pointer; border: none; }
.feedback-btn.interested { background: #f6ffed; color: #52c41a; }
.feedback-btn.interested:hover { background: #d9f7be; }
.feedback-btn.not-match { background: #fff1f0; color: #ff4d4f; }
.feedback-btn.not-match:hover { background: #ffccc7; }

.bundle-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
.bundle-card { background: #f9fafb; border-radius: 10px; padding: 12px; display: flex; flex-direction: column; gap: 10px; }
.bundle-header { display: flex; align-items: flex-start; justify-content: space-between; gap: 10px; }
.bundle-name { font-size: 14px; font-weight: 600; }
.bundle-meta { display: flex; gap: 12px; font-size: 12px; color: #999; white-space: nowrap; }
.bundle-desc { font-size: 12px; color: #666; line-height: 1.5; }
.bundle-highlights { display: flex; flex-wrap: wrap; gap: 8px; }
.highlight-tag { font-size: 12px; color: #0066ff; background: #e8f4ff; padding: 4px 8px; border-radius: 999px; }
.bundle-actions { display: flex; justify-content: flex-end; }
.btn-bundle { padding: 8px 14px; border: 1px solid #0066ff; background: #fff; color: #0066ff; border-radius: 8px; cursor: pointer; font-size: 12px; }
.btn-bundle:hover { background: #0066ff; color: #fff; }
.bundle-products { display: flex; flex-direction: column; gap: 10px; }
.bundle-products-title { font-size: 13px; color: #999; }

.next-question { margin-top: 8px; }
.next-question-btn { padding: 8px 16px; background: #fff; border: 1px solid #0066ff; color: #0066ff; border-radius: 20px; cursor: pointer; font-size: 13px; transition: all 0.2s; }
.next-question-btn:hover { background: #0066ff; color: #fff; }

.typing { display: flex; gap: 4px; padding: 12px 16px; background: #f5f7fa; border-radius: 12px; }
.dot { width: 8px; height: 8px; background: #999; border-radius: 50%; animation: typing 1.4s infinite; }
.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-4px); }
}

.chat-input { padding: 16px 24px; border-top: 1px solid #f0f0f0; display: flex; gap: 12px; }
.chat-input input { flex: 1; padding: 12px 16px; border: 1px solid #e0e0e0; border-radius: 8px; font-size: 14px; }
.chat-input input:focus { outline: none; border-color: #0066ff; }
.chat-input button { padding: 12px 24px; background: #0066ff; color: #fff; border: none; border-radius: 8px; cursor: pointer; font-size: 14px; }
.chat-input button:disabled { background: #ccc; cursor: not-allowed; }

@media (max-width: 1100px) {
  .product-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .bundle-grid { grid-template-columns: 1fr; }
}
</style>
