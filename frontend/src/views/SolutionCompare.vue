<template>
  <div class="solution-compare">
    <div class="page-header">
      <button class="back-btn" @click="$router.back()">← 返回</button>
      <h1>方案对比</h1>
    </div>

    <!-- 选择方案 -->
    <div class="select-section">
      <p class="tips">请选择要对比的方案（最多3个）：</p>
      <div class="solution-selector">
        <div 
          v-for="solution in allSolutions" 
          :key="solution.id"
          class="selector-item"
          :class="{ selected: selectedIds.includes(solution.id) }"
          @click="toggleSolution(solution.id)"
        >
          {{ solution.name }}
        </div>
      </div>
    </div>

    <!-- 对比表格 -->
    <div v-if="selectedSolutions.length > 0" class="compare-table-wrapper">
      <table class="compare-table">
        <thead>
          <tr>
            <th class="dimension">对比维度</th>
            <th v-for="s in selectedSolutions" :key="s.id">{{ s.name }}</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td class="dimension">行业</td>
            <td v-for="s in selectedSolutions" :key="s.id">{{ s.targetIndustry }}</td>
          </tr>
          <tr>
            <td class="dimension">适用场景</td>
            <td v-for="s in selectedSolutions" :key="s.id">{{ s.scenarios }}</td>
          </tr>
          <tr>
            <td class="dimension">架构</td>
            <td v-for="s in selectedSolutions" :key="s.id">{{ s.architecture }}</td>
          </tr>
          <tr>
            <td class="dimension">预计周期</td>
            <td v-for="s in selectedSolutions" :key="s.id">{{ s.estimatedDays }} 天</td>
          </tr>
          <tr>
            <td class="dimension">价格范围</td>
            <td v-for="s in selectedSolutions" :key="s.id">{{ s.priceRange }}</td>
          </tr>
          <tr>
            <td class="dimension">包含产品</td>
            <td v-for="s in selectedSolutions" :key="s.id">
              <div class="products-list">
                <span 
                  v-for="p in s.products" 
                  :key="p.id" 
                  class="product-tag"
                  @click="$router.push(`/products/${p.id}`)"
                >
                  {{ p.name }}
                </span>
              </div>
            </td>
          </tr>
          <tr>
            <td class="dimension">方案描述</td>
            <td v-for="s in selectedSolutions" :key="s.id">{{ s.description }}</td>
          </tr>
          <tr>
            <td class="dimension">操作</td>
            <td v-for="s in selectedSolutions" :key="s.id">
              <button class="btn btn-primary" @click="applyTrial(s)">申请试用</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else class="empty-state">
      <p>请选择至少一个方案进行对比</p>
    </div>
  </div>
</template>

<script>
import { solutionAPI } from '@/api'

export default {
  name: 'SolutionCompare',
  data() {
    return {
      allSolutions: [],
      selectedIds: []
    }
  },
  computed: {
    selectedSolutions() {
      return this.allSolutions.filter(s => this.selectedIds.includes(s.id))
    }
  },
  mounted() {
    this.loadSolutions()
    // 从 URL 参数获取预选的方案
    const ids = this.$route.query.ids
    if (ids) {
      this.selectedIds = ids.split(',').map(Number)
    }
  },
  methods: {
    async loadSolutions() {
      try {
        const res = await solutionAPI.list()
        this.allSolutions = res.data.data || []
      } catch (error) {
        console.error('加载方案失败:', error)
      }
    },
    toggleSolution(id) {
      const index = this.selectedIds.indexOf(id)
      if (index > -1) {
        this.selectedIds.splice(index, 1)
      } else {
        if (this.selectedIds.length < 3) {
          this.selectedIds.push(id)
        }
      }
    },
    applyTrial(solution) {
      this.$router.push({ path: '/ai-chat', query: { solutionId: solution.id } })
    }
  }
}
</script>

<style scoped>
.solution-compare {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.back-btn {
  background: none;
  border: none;
  color: #1890ff;
  cursor: pointer;
  font-size: 14px;
}

.page-header h1 {
  font-size: 24px;
  color: #333;
}

.select-section {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
}

.tips {
  color: #666;
  margin-bottom: 16px;
}

.solution-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.selector-item {
  padding: 10px 20px;
  border: 2px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.selector-item:hover {
  border-color: #1890ff;
}

.selector-item.selected {
  background: #1890ff;
  border-color: #1890ff;
  color: #fff;
}

.compare-table-wrapper {
  overflow-x: auto;
}

.compare-table {
  width: 100%;
  background: #fff;
  border-radius: 12px;
  border-collapse: collapse;
  overflow: hidden;
}

.compare-table th,
.compare-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.compare-table th {
  background: #f9f9f9;
  font-weight: 600;
}

.compare-table .dimension {
  background: #f9f9f9;
  font-weight: 500;
  width: 150px;
}

.products-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.product-tag {
  display: inline-block;
  padding: 4px 8px;
  background: #e6f7ff;
  color: #1890ff;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.product-tag:hover {
  background: #1890ff;
  color: #fff;
}

.btn {
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #1890ff;
  color: #fff;
  border: none;
}

.btn-primary:hover {
  background: #40a9ff;
}

.empty-state {
  text-align: center;
  padding: 60px;
  color: #999;
  background: #fff;
  border-radius: 12px;
}
</style>
