<template>
  <div class="trial-report">
    <div class="page-header">
      <button class="back-btn" @click="$router.back()">← 返回</button>
      <h1>试用报告</h1>
    </div>

    <div class="report-content">
      <!-- 试用基本信息 -->
      <div class="report-section">
        <h2>试用信息</h2>
        <div class="info-grid">
          <div class="info-item">
            <span class="label">试用产品</span>
            <span class="value">{{ trial?.productName }}</span>
          </div>
          <div class="info-item">
            <span class="label">试用时间</span>
            <span class="value">{{ trial?.startTime }} - {{ trial?.endTime }}</span>
          </div>
          <div class="info-item">
            <span class="label">环境地址</span>
            <a :href="trial?.environmentUrl" target="_blank" class="link">{{ trial?.environmentUrl }}</a>
          </div>
        </div>
      </div>

      <!-- 测试摘要 -->
      <div class="report-section">
        <h2>测试摘要</h2>
        <textarea v-model="report.testSummary" class="form-textarea" placeholder="描述您的测试过程和主要发现..." rows="4"></textarea>
      </div>

      <!-- 发现的问题 -->
      <div class="report-section">
        <h2>发现的问题</h2>
        <textarea v-model="report.issuesFound" class="form-textarea" placeholder="记录发现的问题或改进建议..." rows="3"></textarea>
      </div>

      <!-- 总体评分 -->
      <div class="report-section">
        <h2>总体评分</h2>
        <div class="rating-stars">
          <span 
            v-for="star in 5" 
            :key="star" 
            class="star"
            :class="{ active: star <= report.overallRating }"
            @click="report.overallRating = star"
          >
            ⭐
          </span>
          <span class="rating-text">{{ report.overallRating }} / 5 分</span>
        </div>
      </div>

      <!-- 建议 -->
      <div class="report-section">
        <h2>建议与反馈</h2>
        <textarea v-model="report.suggestions" class="form-textarea" placeholder="写下您的建议..." rows="3"></textarea>
      </div>

      <!-- 提交按钮 -->
      <div class="report-actions">
        <button class="btn btn-primary" @click="submitReport">提交报告</button>
        <button class="btn btn-outline" @click="$router.back()">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  name: 'TrialReport',
  setup() {
    const trial = ref(null)
    const report = ref({
      testSummary: '',
      issuesFound: '',
      overallRating: 5,
      suggestions: ''
    })

    onMounted(() => {
      // 模拟加载试用数据
      const trialData = localStorage.getItem('currentTrial')
      if (trialData) {
        trial.value = JSON.parse(trialData)
      }
    })

    const submitReport = () => {
      console.log('提交报告:', report.value)
      alert('报告提交成功！')
      // 这里可以调用 API 保存报告
    }

    return { trial, report, submitReport }
  }
}
</script>

<style scoped>
.trial-report { max-width: 800px; margin: 0 auto; }
.page-header { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.back-btn { background: none; border: none; color: #1890ff; cursor: pointer; font-size: 14px; }
.page-header h1 { font-size: 24px; }
.report-content { background: #fff; border-radius: 12px; padding: 32px; }
.report-section { margin-bottom: 24px; }
.report-section h2 { font-size: 16px; margin-bottom: 12px; color: #333; }
.info-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
.info-item { display: flex; flex-direction: column; gap: 4px; }
.info-item .label { font-size: 12px; color: #999; }
.info-item .value { font-size: 14px; color: #333; }
.link { color: #1890ff; text-decoration: none; }
.form-textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; resize: vertical; }
.form-textarea:focus { outline: none; border-color: #1890ff; }
.rating-stars { display: flex; align-items: center; gap: 8px; }
.star { font-size: 28px; cursor: pointer; opacity: 0.3; transition: all 0.2s; }
.star.active { opacity: 1; }
.star:hover { transform: scale(1.2); }
.rating-text { margin-left: 12px; color: #666; }
.report-actions { display: flex; gap: 12px; margin-top: 32px; }
.btn { padding: 12px 32px; border-radius: 8px; font-size: 14px; cursor: pointer; }
.btn-primary { background: #1890ff; color: #fff; border: none; }
.btn-primary:hover { background: #40a9ff; }
.btn-outline { background: #fff; border: 1px solid #ddd; }
.btn-outline:hover { border-color: #1890ff; color: #1890ff; }
</style>
