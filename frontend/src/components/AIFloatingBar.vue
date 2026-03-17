<template>
  <Teleport to="body">
    <div v-if="showFloatBar" class="ai-floating-bar">
      <!-- 展开状态：显示面板 -->
      <Transition name="panel-slide">
        <AIFloatPanel 
          v-if="isExpanded" 
          :loading="loading"
          @close="closePanel"
        />
      </Transition>
      
      <!-- 收起状态：显示悬浮球 -->
      <Transition name="button-fade">
        <AIFloatButton 
          v-if="!isExpanded" 
          :has-unread="hasUnread"
          :unread-count="unreadCount"
          @click="openPanel"
        />
      </Transition>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import AIFloatButton from './AIFloatButton.vue'
import AIFloatPanel from './AIFloatPanel.vue'

const route = useRoute()

// 状态
const isExpanded = ref(false)
const hasUnread = ref(false)
const unreadCount = ref(0)
const loading = ref(false)

// 计算是否显示悬浮栏（始终显示，因为已取消独立页面）
const showFloatBar = computed(() => {
  return true
})

// 打开面板
function openPanel() {
  isExpanded.value = true
  // 清未读
  hasUnread.value = false
  unreadCount.value = 0
}

// 关闭面板
function closePanel() {
  isExpanded.value = false
}

// ESC键关闭
function handleKeydown(e) {
  if (e.key === 'Escape' && isExpanded.value) {
    closePanel()
  }
}

onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
  // 监听自定义事件来打开AI面板
  window.addEventListener('open-ai-chat', openPanel)
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
  window.removeEventListener('open-ai-chat', openPanel)
})
</script>

<style scoped>
.ai-floating-bar {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

/* 面板展开动画 */
.panel-slide-enter-active {
  animation: panel-in 0.3s ease-out;
}

.panel-slide-leave-active {
  animation: panel-out 0.2s ease-in;
}

@keyframes panel-in {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes panel-out {
  from {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
  to {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }
}

/* 悬浮球淡入动画 */
.button-fade-enter-active {
  animation: button-in 0.3s ease-out;
}

.button-fade-leave-active {
  animation: button-out 0.2s ease-in;
}

@keyframes button-in {
  from {
    opacity: 0;
    transform: scale(0);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes button-out {
  from {
    opacity: 1;
    transform: scale(1);
  }
  to {
    opacity: 0;
    transform: scale(0);
  }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .ai-floating-bar {
    right: 16px;
    bottom: 16px;
  }
}
</style>
