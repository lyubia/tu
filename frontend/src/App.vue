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
          <router-link to="/trial" class="nav-link trial-link" :class="{ active: $route.path === '/trial' }">试用工作台</router-link>
        </nav>
      </div>
    </header>
    
    <main class="main">
      <router-view :key="$route.fullPath" />
    </main>
    
    <AIFloatingBar />
  </div>
</template>

<script setup>
import AIFloatingBar from './components/AIFloatingBar.vue'
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
.main { max-width: 1400px; margin: 0 auto; padding: 24px; }
</style>
