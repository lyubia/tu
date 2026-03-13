<template>
  <div class="admin-audit">
    <div class="page-header">
      <h1>📋 审核中心</h1>
    </div>

    <div class="tabs">
      <button class="tab" :class="{ active: activeTab === 'products' }" @click="activeTab = 'products'">
        产品审核 <span class="badge">{{ pendingProducts }}</span>
      </button>
      <button class="tab" :class="{ active: activeTab === 'solutions' }" @click="activeTab = 'solutions'">方案审核</button>
      <button class="tab" :class="{ active: activeTab === 'logs' }" @click="activeTab = 'logs'">操作日志</button>
    </div>

    <div class="card">
      <!-- 产品审核 -->
      <div v-if="activeTab === 'products'">
        <table class="table">
          <thead>
            <tr>
              <th>产品名称</th>
              <th>提供方</th>
              <th>提交时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in productReviews" :key="item.id">
              <td>{{ item.name }}</td>
              <td>{{ item.provider }}</td>
              <td>{{ item.submitTime }}</td>
              <td><span class="tag tag-warning">待审核</span></td>
              <td>
                <button class="btn btn-sm btn-success" @click="approve(item, 'products')">通过</button>
                <button class="btn btn-sm btn-danger" @click="reject(item, 'products')">驳回</button>
                <button class="btn btn-sm" @click="viewDetail(item)">详情</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 方案审核 -->
      <div v-if="activeTab === 'solutions'">
        <table class="table">
          <thead>
            <tr>
              <th>方案名称</th>
              <th>产品组合</th>
              <th>提交时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in solutionReviews" :key="item.id">
              <td>{{ item.name }}</td>
              <td>{{ item.products }}</td>
              <td>{{ item.submitTime }}</td>
              <td><span class="tag tag-warning">待审核</span></td>
              <td>
                <button class="btn btn-sm btn-success" @click="approve(item, 'solutions')">通过</button>
                <button class="btn btn-sm btn-danger" @click="reject(item, 'solutions')">驳回</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 操作日志 -->
      <div v-if="activeTab === 'logs'">
        <div class="log-filters">
          <select class="select" style="width: 150px;">
            <option value="">全部操作</option>
            <option value="login">登录</option>
            <option value="create">创建</option>
            <option value="update">修改</option>
            <option value="delete">删除</option>
          </select>
          <input class="input" style="width: 200px;" placeholder="搜索用户" />
        </div>
        <table class="table">
          <thead>
            <tr>
              <th>时间</th>
              <th>用户</th>
              <th>操作</th>
              <th>对象</th>
              <th>详情</th>
              <th>IP</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="log in logs" :key="log.id">
              <td>{{ log.time }}</td>
              <td>{{ log.user }}</td>
              <td><span class="tag" :class="'tag-' + log.actionType">{{ log.action }}</span></td>
              <td>{{ log.target }}</td>
              <td>{{ log.details }}</td>
              <td>{{ log.ip }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'AdminAudit',
  setup() {
    const activeTab = ref('products')
    const pendingProducts = ref(3)

    const productReviews = ref([
      { id: 1, name: '智能分单系统', provider: '智分科技', submitTime: '2026-03-13 14:30' },
      { id: 2, name: '无人仓管理系统', provider: '无人科技', submitTime: '2026-03-13 10:15' },
      { id: 3, name: '跨境电商物流平台', provider: '跨境物流', submitTime: '2026-03-12 16:45' }
    ])

    const solutionReviews = ref([
      { id: 1, name: '跨境电商物流方案', products: '跨境平台+报关系统+海外仓', submitTime: '2026-03-13 11:20' }
    ])

    const logs = ref([
      { id: 1, time: '2026-03-13 17:50', user: 'admin', action: '登录', actionType: 'primary', target: '系统', details: '成功', ip: '192.168.0.114' },
      { id: 2, time: '2026-03-13 17:45', user: '李经理', action: '创建产品', actionType: 'success', target: '智能分单系统', details: '提交审核', ip: '192.168.0.105' },
      { id: 3, time: '2026-03-13 17:30', user: 'admin', action: '审核通过', actionType: 'success', target: '仓储管理系统', details: '审核通过', ip: '192.168.0.114' }
    ])

    const approve = (item, type) => { console.log('通过:', item) }
    const reject = (item, type) => { console.log('驳回:', item) }
    const viewDetail = (item) => { console.log('详情:', item) }

    return { activeTab, pendingProducts, productReviews, solutionReviews, logs, approve, reject, viewDetail }
  }
}
</script>

<style scoped>
.admin-audit { max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 24px; }
.tabs { display: flex; gap: 8px; margin-bottom: 20px; background: #fff; padding: 8px; border-radius: 12px; }
.tab { padding: 10px 20px; border: none; background: transparent; cursor: pointer; border-radius: 8px; display: flex; align-items: center; gap: 8px; }
.tab.active { background: #1890ff; color: #fff; }
.badge { background: #ff4d4f; color: #fff; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
.card { background: #fff; border-radius: 12px; padding: 24px; }
.log-filters { display: flex; gap: 12px; margin-bottom: 16px; }
.tag { padding: 4px 10px; border-radius: 4px; font-size: 12px; }
.tag-warning { background: #fff7e6; color: #fa8c16; }
.tag-success { background: #f6ffed; color: #52c41a; }
.tag-primary { background: #e6f7ff; color: #1890ff; }
.tag-danger { background: #fff1f0; color: #ff4d4f; }
.btn-sm { padding: 6px 12px; font-size: 12px; border-radius: 4px; border: 1px solid #d9d9d9; background: #fff; cursor: pointer; margin-right: 8px; }
.btn-success { background: #f6ffed; border-color: #52c41a; color: #52c41a; }
.btn-danger { background: #fff1f0; border-color: #ff4d4f; color: #ff4d4f; }
.select, .input { padding: 8px 12px; border: 1px solid #d9d9d9; border-radius: 6px; }
</style>
