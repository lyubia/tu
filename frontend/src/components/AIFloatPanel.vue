<template>
  <div class="ai-float-panel">
    <!-- 头部 -->
    <div class="panel-header">
      <div class="header-left">
        <span class="header-icon">🤖</span>
        <span class="header-title">AI 顾问</span>
      </div>
      <button class="close-btn" @click="emit('close')">✕</button>
    </div>
    
    <!-- 消息区域 -->
    <div class="panel-messages" ref="messagesRef">
      <div v-for="(msg, index) in messages" :key="index" class="message" :class="msg.role">
        <div class="message-avatar">
          {{ msg.role === 'user' ? '👤' : '🤖' }}
        </div>
        <div class="message-content">
          <div class="message-text" v-html="formatMessage(msg.content)"></div>
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
    
    <!-- 输入区域 -->
    <div class="panel-input">
      <input 
        v-model="inputMessage" 
        type="text" 
        placeholder="请描述您的需求..."
        @keyup.enter="sendMessage"
        :disabled="loading"
      />
      <button @click="sendMessage" :disabled="loading || !inputMessage.trim()">
        {{ loading ? '...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import { aiAPI } from '../api'

const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'update:loading'])

const messages = ref([
  {
    role: 'assistant',
    content: '您好！我是您的智能选型顾问 🤖\n\n请问有什么可以帮您？'
  }
])
const inputMessage = ref('')
const messagesRef = ref(null)

// 监听loading状态变化
watch(() => props.loading, (val) => {
  emit('update:loading', val)
})

async function sendMessage() {
  if (!inputMessage.value.trim() || props.loading) return
  
  const userMessage = inputMessage.value.trim()
  inputMessage.value = ''
  
  messages.value.push({ role: 'user', content: userMessage })
  scrollToBottom()
  
  emit('update:loading', true)
  
  try {
    const res = await aiAPI.chat({
      userId: 1,
      message: userMessage,
      history: messages.value.slice(-6).map(m => ({ role: m.role, content: m.content }))
    })
    
    const data = res.data.data
    messages.value.push({
      role: 'assistant',
      content: data.message?.message?.content || data.message?.content || '感谢您的咨询，请继续告诉我您的具体需求。'
    })
  } catch (e) {
    messages.value.push({
      role: 'assistant',
      content: '抱歉，服务暂时不可用，请稍后重试。'
    })
  }
  
  emit('update:loading', false)
  scrollToBottom()
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

// 暴露方法供父组件调用
defineExpose({
  messages,
  inputMessage
})
</script>

<style scoped>
.ai-float-panel {
  width: 400px;
  height: 600px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 1px solid #e8e8e8;
}

.panel-header {
  height: 56px;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-icon {
  font-size: 22px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 16px;
  color: #999;
  border-radius: 6px;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #f5f7fa;
  color: #333;
}

.panel-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.message {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

.message-avatar {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 50%;
  font-size: 16px;
  flex-shrink: 0;
}

.message.user {
  flex-direction: row-reverse;
}

.message.user .message-content {
  align-items: flex-end;
}

.message.user .message-avatar {
  background: #e8f4ff;
}

.message.assistant .message-avatar {
  background: #fef3e2;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-width: 80%;
}

.message-text {
  padding: 10px 14px;
  border-radius: 12px;
  line-height: 1.5;
  font-size: 14px;
  word-break: break-word;
}

.message.user .message-text {
  background: #0066ff;
  color: #fff;
}

.message.assistant .message-text {
  background: #f5f7fa;
  color: #333;
}

.typing {
  display: flex;
  gap: 4px;
  padding: 10px 14px;
  background: #f5f7fa;
  border-radius: 12px;
}

.dot {
  width: 6px;
  height: 6px;
  background: #999;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-4px);
  }
}

.panel-input {
  padding: 12px 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 10px;
  background: #fff;
  flex-shrink: 0;
}

.panel-input input {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  font-size: 14px;
}

.panel-input input:focus {
  outline: none;
  border-color: #0066ff;
}

.panel-input button {
  padding: 10px 18px;
  background: #0066ff;
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.panel-input button:hover:not(:disabled) {
  background: #0052cc;
}

.panel-input button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .ai-float-panel {
    width: 100%;
    height: 80%;
    border-radius: 16px 16px 0 0;
    position: fixed;
    bottom: 0;
    right: 0;
  }
}
</style>
