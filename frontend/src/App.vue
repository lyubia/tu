<template>
  <div class="app">
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <span class="logo-icon">🏢</span>
          <span class="logo-text">产品体验平台</span>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }">首页</router-link>
          <router-link to="/products" class="nav-link" :class="{ active: $route.path.startsWith('/products') }">产品库</router-link>
          <router-link to="/solutions" class="nav-link" :class="{ active: $route.path.startsWith('/solutions') }">解决方案</router-link>
          <router-link to="/ai-chat" class="nav-link" :class="{ active: $route.path === '/ai-chat' }">AI 顾问</router-link>
          <router-link to="/trial" class="nav-link trial-link" :class="{ active: $route.path === '/trial' }">试用工作台</router-link>
          <router-link to="/partner" class="nav-link partner-link" :class="{ active: $route.path === '/partner' }" v-if="currentRole === '提供方'">提供方工作台</router-link>
          <router-link to="/admin" class="nav-link admin-link" :class="{ active: $route.path === '/admin' }" v-if="currentRole === '管理方'">管理后台</router-link>
        </nav>
        <div class="user-info">
          <div class="role-switcher" @click="showRoleMenu = !showRoleMenu">
            <span class="role-badge" :class="roleClass">{{ currentRole }}</span>
            <span class="arrow">▼</span>
            <div class="role-menu" v-if="showRoleMenu">
              <div class="role-option" @click.stop="switchRole('购买方')">🛒 购买方</div>
              <div class="role-option" @click.stop="switchRole('提供方')">🏭 提供方</div>
              <div class="role-option" @click.stop="switchRole('管理方')">⚙️ 管理方</div>
            </div>
          </div>
        </div>
      </div>
    </header>
    
    <main class="main">
      <router-view :key="$route.fullPath" />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentRole = ref('购买方')
const showRoleMenu = ref(false)

const roleClass = computed(() => {
  return {
    'role-buyer': currentRole.value === '购买方',
    'role-provider': currentRole.value === '提供方',
    'role-admin': currentRole.value === '管理方'
  }
})

const switchRole = (role) => {
  currentRole.value = role
  showRoleMenu.value = false
  // 根据角色跳转不同首页
  if (role === '提供方') {
    router.push('/partner')
  } else if (role === '管理方') {
    router.push('/admin')
  } else {
    router.push('/')
  }
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
.app { min-height: 100vh; background: #f5f7fa; }
.header { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.08); position: sticky; top: 0; z-index: 100; }
.header-content { max-width: 1400px; margin: 0 auto; padding: 0 24px; height: 60px; display: flex; align-items: center; justify-content: space-between; }
.logo { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.logo-icon { font-size: 24px; }
.logo-text { font-size: 18px; font-weight: 600; color: #1a1a2e; }
.nav { display: flex; gap: 8px; }
.nav-link { padding: 8px 16px; border-radius: 6px; text-decoration: none; color: #666; font-size: 14px; transition: all 0.2s; }
.nav-link:hover { background: #f5f7fa; color: #333; }
.nav-link.active { background: #e8f4ff; color: #0066ff; }
.trial-link { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff !important; }
.trial-link:hover { opacity: 0.9; }
.partner-link { background: #fff1d6; color: #fa8c16 !important; border: 1px solid #ffe7ba; }
.partner-link:hover { background: #ffe7ba; }
.admin-link { background: #fff7e6; color: #fa8c16 !important; border: 1px solid #ffe7ba; }
.admin-link:hover { background: #fff1d6; }
.user-info { position: relative; }
.role-switcher { display: flex; align-items: center; gap: 6px; cursor: pointer; padding: 4px 12px; border-radius: 16px; background: #f0f0f0; }
.role-badge { font-size: 12px; font-weight: 500; }
.role-buyer { color: #1890ff; }
.role-provider { color: #52c41a; }
.role-admin { color: #fa8c16; }
.arrow { font-size: 10px; color: #999; }
.role-menu { position: absolute; top: 100%; right: 0; margin-top: 8px; background: #fff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); overflow: hidden; z-index: 1000; }
.role-option { padding: 12px 20px; cursor: pointer; transition: all 0.2s; white-space: nowrap; }
.role-option:hover { background: #f5f7fa; }
.main { max-width: 1400px; margin: 0 auto; padding: 24px; }
</style>
