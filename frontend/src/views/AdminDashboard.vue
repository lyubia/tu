<template>
  <div class="admin-dashboard">
    <div class="page-header">
      <h1>📊 运营看板</h1>
      <div class="date-range">
        <span>数据范围：</span>
        <select class="select">
          <option>最近7天</option>
          <option>最近30天</option>
          <option>本月</option>
          <option>本季度</option>
        </select>
      </div>
    </div>

    <!-- 核心指标 -->
    <div class="metrics-grid">
      <div class="metric-card">
        <div class="metric-icon" style="background: #e6f7ff;">👥</div>
        <div class="metric-content">
          <div class="metric-value">{{ metrics.totalUsers }}</div>
          <div class="metric-label">总用户数</div>
          <div class="metric-change" :class="metrics.userChange > 0 ? 'up' : 'down'">
            {{ metrics.userChange > 0 ? '↑' : '↓' }} {{ Math.abs(metrics.userChange) }}%
          </div>
        </div>
      </div>
      <div class="metric-card">
        <div class="metric-icon" style="background: #f6ffed;">💬</div>
        <div class="metric-content">
          <div class="metric-value">{{ metrics.totalConversations }}</div>
          <div class="metric-label">AI对话次数</div>
          <div class="metric-change up">↑ {{ metrics.conversationChange }}%</div>
        </div>
      </div>
      <div class="metric-card">
        <div class="metric-icon" style="background: #fff7e6;">📦</div>
        <div class="metric-content">
          <div class="metric-value">{{ metrics.totalProducts }}</div>
          <div class="metric-label">在售产品</div>
          <div class="metric-change up">↑ {{ metrics.productChange }}</div>
        </div>
      </div>
      <div class="metric-card">
        <div class="metric-icon" style="background: #fff1f0;">🧪</div>
        <div class="metric-content">
          <div class="metric-value">{{ metrics.totalTrials }}</div>
          <div class="metric-label">试用次数</div>
          <div class="metric-change up">↑ {{ metrics.trialChange }}%</div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <div class="chart-card">
        <h3>趋势分析</h3>
        <div class="chart-placeholder">
          <div class="fake-chart">
            <div class="bar" v-for="(v, i) in [40, 65, 45, 80, 55, 70, 90]" :key="i" :style="{ height: v + '%' }"></div>
          </div>
          <div class="chart-labels">
            <span v-for="d in ['周一','周二','周三','周四','周五','周六','周日']" :key="d">{{ d }}</span>
          </div>
        </div>
      </div>
      <div class="chart-card">
        <h3>行业分布</h3>
        <div class="industry-list">
          <div class="industry-item" v-for="ind in industries" :key="ind.name">
            <span class="ind-name">{{ ind.name }}</span>
            <div class="ind-bar"><div class="ind-fill" :style="{ width: ind.percent + '%' }"></div></div>
            <span class="ind-value">{{ ind.percent }}%</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 转化漏斗 -->
    <div class="funnel-card">
      <h3>转化漏斗</h3>
      <div class="funnel">
        <div class="funnel-step" v-for="(step, i) in funnel" :key="step.name" :style="{ width: (100 - i * 15) + '%' }">
          <span class="step-name">{{ step.name }}</span>
          <span class="step-value">{{ step.value }}</span>
          <span class="step-rate" v-if="i > 0">{{ step.rate }}%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'AdminDashboard',
  setup() {
    const metrics = ref({
      totalUsers: 156,
      userChange: 12,
      totalConversations: 892,
      conversationChange: 25,
      totalProducts: 45,
      productChange: 5,
      totalTrials: 234,
      trialChange: 18
    })

    const industries = ref([
      { name: '物流', percent: 45 },
      { name: '电商', percent: 28 },
      { name: '制造', percent: 15 },
      { name: '零售', percent: 12 }
    ])

    const funnel = ref([
      { name: '访问用户', value: 1560, rate: null },
      { name: '发起对话', value: 892, rate: 57 },
      { name: '获取方案', value: 456, rate: 51 },
      { name: '申请试用', value: 234, rate: 51 },
      { name: '提交意向', value: 78, rate: 33 }
    ])

    return { metrics, industries, funnel }
  }
}
</script>

<style scoped>
.admin-dashboard { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-header h1 { font-size: 24px; }
.date-range { display: flex; align-items: center; gap: 8px; color: #666; }
.select { padding: 8px 12px; border: 1px solid #d9d9d9; border-radius: 6px; }

.metrics-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.metric-card { background: #fff; border-radius: 12px; padding: 24px; display: flex; gap: 16px; }
.metric-icon { width: 56px; height: 56px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; }
.metric-value { font-size: 28px; font-weight: 600; }
.metric-label { font-size: 14px; color: #999; margin: 4px 0; }
.metric-change { font-size: 12px; }
.metric-change.up { color: #52c41a; }
.metric-change.down { color: #ff4d4f; }

.charts-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-bottom: 24px; }
.chart-card { background: #fff; border-radius: 12px; padding: 24px; }
.chart-card h3 { font-size: 16px; margin-bottom: 20px; }
.chart-placeholder { height: 200px; }
.fake-chart { display: flex; align-items: flex-end; justify-content: space-around; height: 160px; padding: 0 20px; }
.bar { width: 30px; background: linear-gradient(180deg, #1890ff, #40a9ff); border-radius: 4px 4px 0 0; }
.chart-labels { display: flex; justify-content: space-around; padding: 10px 20px 0; font-size: 12px; color: #999; }

.industry-list { display: flex; flex-direction: column; gap: 16px; }
.industry-item { display: flex; align-items: center; gap: 12px; }
.ind-name { width: 50px; font-size: 14px; }
.ind-bar { flex: 1; height: 8px; background: #f0f0f0; border-radius: 4px; }
.ind-fill { height: 100%; background: #1890ff; border-radius: 4px; }
.ind-value { width: 40px; text-align: right; font-size: 14px; color: #666; }

.funnel-card { background: #fff; border-radius: 12px; padding: 24px; }
.funnel-card h3 { font-size: 16px; margin-bottom: 24px; }
.funnel { display: flex; flex-direction: column; align-items: center; gap: 8px; }
.funnel-step { background: #e6f7ff; border-radius: 8px; padding: 16px; display: flex; align-items: center; justify-content: space-between; }
.step-name { font-size: 14px; color: #333; }
.step-value { font-size: 18px; font-weight: 600; color: #1890ff; }
.step-rate { font-size: 12px; color: #999; }
</style>
