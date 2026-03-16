<template>
  <div class="home">
    <div class="portal">
      <div class="portal-grid">
        <section class="portal-block span-12">
          <div class="hero">
            <div class="hero-content">
              <h1 class="hero-title">智能物流解决方案平台</h1>
              <p class="hero-subtitle">AI 驱动的智能推荐，让选型变得更简单</p>
              <div class="hero-actions">
                <button class="btn btn-primary" @click="$router.push('/ai-chat')">开始智能选型</button>
                <button class="btn btn-secondary" @click="$router.push('/trial')">免费试用</button>
              </div>
            </div>
          </div>
        </section>

        <section class="portal-block span-12">
          <div class="quick-entry">
            <div class="entry-card" @click="$router.push('/ai-chat')">
              <div class="entry-icon">🤖</div>
              <div class="entry-text">
                <h3>AI 顾问</h3>
                <p>智能推荐方案</p>
              </div>
            </div>
            <div class="entry-card" @click="$router.push('/trial')">
              <div class="entry-icon">🧪</div>
              <div class="entry-text">
                <h3>试用工作台</h3>
                <p>在线体验产品</p>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.portal { display: flex; flex-direction: column; gap: 20px; }
.portal-grid { display: grid; grid-template-columns: repeat(12, 1fr); gap: 20px; }
.portal-block { grid-column: span 12; }

.hero { text-align: center; padding: 60px 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; }
.hero-title { color: #fff; font-size: 36px; margin-bottom: 12px; }
.hero-subtitle { color: rgba(255,255,255,0.9); font-size: 18px; margin-bottom: 32px; }
.hero-actions { display: flex; gap: 16px; justify-content: center; }
.btn { padding: 12px 32px; border-radius: 8px; font-size: 16px; cursor: pointer; border: none; transition: all 0.2s; }
.btn-primary { background: #fff; color: #667eea; }
.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
.btn-secondary { background: rgba(255,255,255,0.2); color: #fff; border: 1px solid #fff; }
.btn-secondary:hover { background: rgba(255,255,255,0.3); }

.quick-entry { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; max-width: 800px; margin: 0 auto; }
.entry-card { background: #fff; padding: 24px; border-radius: 12px; display: flex; align-items: center; gap: 16px; cursor: pointer; transition: all 0.2s; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.entry-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.08); }
.entry-icon { font-size: 36px; }
.entry-text h3 { font-size: 16px; margin-bottom: 4px; }
.entry-text p { color: #999; font-size: 14px; }

@media (max-width: 768px) {
  .quick-entry { grid-template-columns: 1fr; }
}
</style>
