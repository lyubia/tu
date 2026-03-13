<template>
  <div class="requirement-form">
    <div class="form-header">
      <h3>📋 需求清单</h3>
      <span class="tip">AI 正在收集您的需求</span>
    </div>
    
    <div class="form-content">
      <div v-if="Object.keys(requirements).length === 0" class="empty-state">
        <div class="empty-icon">📝</div>
        <p>开始对话后，AI 将自动提取您的需求</p>
      </div>
      
      <div v-else class="requirements-list">
        <div class="req-item" v-for="(value, key) in requirements" :key="key">
          <div class="req-label">{{ formatLabel(key) }}</div>
          <div class="req-value">{{ value }}</div>
          <button class="req-edit" @click="$emit('edit', key)">编辑</button>
        </div>
      </div>
    </div>
    
    <div class="form-progress" v-if="Object.keys(requirements).length > 0">
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: progressWidth }"></div>
      </div>
      <span class="progress-text">已收集 {{ Object.keys(requirements).length }} 项</span>
    </div>
    
    <div class="form-actions" v-if="Object.keys(requirements).length >= 3">
      <button class="btn-primary" @click="$emit('generate')">
        生成方案报告
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  requirements: {
    type: Object,
    default: () => ({})
  }
})

defineEmits(['edit', 'generate'])

const formatLabel = (key) => {
  const map = {
    industry: '行业',
    scenario: '场景',
    capability: '能力点',
    budget: '预算',
    scale: '规模',
    version: '版本',
    needCase: '案例',
    timeline: '时间要求',
    companySize: '企业规模',
    currentSystem: '现有系统'
  }
  return map[key] || key
}

const progressWidth = computed(() => {
  const keys = Object.keys(props.requirements)
  const maxItems = 6
  return Math.min((keys.length / maxItems) * 100, 100) + '%'
})
</script>

<style scoped>
.requirement-form {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  height: fit-content;
}

.form-header {
  margin-bottom: 20px;
}

.form-header h3 {
  font-size: 16px;
  margin-bottom: 4px;
}

.tip {
  font-size: 12px;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 30px 10px;
  color: #999;
}

.empty-icon {
  font-size: 40px;
  margin-bottom: 12px;
}

.requirements-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.req-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
  gap: 8px;
}

.req-label {
  font-size: 12px;
  color: #999;
  min-width: 60px;
}

.req-value {
  flex: 1;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.req-edit {
  font-size: 12px;
  color: #1890ff;
  background: none;
  border: none;
  cursor: pointer;
}

.form-progress {
  margin-top: 20px;
}

.progress-bar {
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #1890ff, #52c41a);
  transition: width 0.3s;
}

.progress-text {
  display: block;
  text-align: center;
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.form-actions {
  margin-top: 20px;
}

.btn-primary {
  width: 100%;
  padding: 12px;
  background: #1890ff;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary:hover {
  background: #40a9ff;
}
</style>
