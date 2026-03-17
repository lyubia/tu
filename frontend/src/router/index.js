import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import TrialWorkbench from '../views/TrialWorkbench.vue'
import Requirements from '../views/Requirements.vue'
import Register from '../views/Register.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/trial', name: 'TrialWorkbench', component: TrialWorkbench },
  { path: '/requirements', name: 'Requirements', component: Requirements },
  { path: '/register', name: 'Register', component: Register }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
