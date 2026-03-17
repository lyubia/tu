<template>
  <div class="ai-float-button" @click="emit('click')" :class="{ 'has-unread': hasUnread }">
    <span class="ai-icon">🤖</span>
    <div v-if="hasUnread" class="unread-badge">
      <span v-if="unreadCount > 0">{{ unreadCount > 9 ? '9+' : unreadCount }}</span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  hasUnread: {
    type: Boolean,
    default: false
  },
  unreadCount: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['click'])
</script>

<style scoped>
.ai-float-button {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  position: relative;
}

.ai-float-button:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.ai-icon {
  font-size: 24px;
}

.unread-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 20px;
  height: 20px;
  border-radius: 10px;
  background: #ff4d4f;
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
  border: 2px solid #fff;
  animation: breathe 2s infinite;
}

@keyframes breathe {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@media (max-width: 768px) {
  .ai-float-button {
    width: 48px;
    height: 48px;
  }
  
  .ai-icon {
    font-size: 20px;
  }
}
</style>
