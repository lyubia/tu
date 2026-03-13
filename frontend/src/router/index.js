import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Products from '../views/Products.vue'
import ProductDetail from '../views/ProductDetail.vue'
import Solutions from '../views/Solutions.vue'
import SolutionDetail from '../views/SolutionDetail.vue'
import SolutionCompare from '../views/SolutionCompare.vue'
import TrialWorkbench from '../views/TrialWorkbench.vue'
import AIChat from '../views/AIChat.vue'
import AdminConsole from '../views/AdminConsole.vue'
import PartnerConsole from '../views/PartnerConsole.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/products', name: 'Products', component: Products },
  { path: '/products/:id', name: 'ProductDetail', component: ProductDetail },
  { path: '/solutions', name: 'Solutions', component: Solutions },
  { path: '/solutions/:id', name: 'SolutionDetail', component: SolutionDetail },
  { path: '/solutions/compare', name: 'SolutionCompare', component: SolutionCompare },
  { path: '/trial', name: 'TrialWorkbench', component: TrialWorkbench },
  { path: '/ai-chat', name: 'AIChat', component: AIChat },
  { path: '/admin', name: 'AdminConsole', component: AdminConsole },
  { path: '/partner', name: 'PartnerConsole', component: PartnerConsole }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
