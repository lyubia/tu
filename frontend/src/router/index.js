import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import TrialWorkbench from '../views/TrialWorkbench.vue'
import AIChat from '../views/AIChat.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/trial', name: 'TrialWorkbench', component: TrialWorkbench },
  { path: '/ai-chat', name: 'AIChat', component: AIChat }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
