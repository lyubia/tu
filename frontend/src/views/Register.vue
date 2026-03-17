<template>
  <div class="register-container">
    <div class="register-card">
      <h1>用户注册</h1>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名 <span class="required">*</span></label>
          <input v-model="form.username" type="text" required placeholder="请输入用户名" />
        </div>
        
        <div class="form-group">
          <label>姓名</label>
          <input v-model="form.name" type="text" placeholder="请输入姓名" />
        </div>
        
        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" type="email" placeholder="请输入邮箱" />
        </div>
        
        <div class="form-group">
          <label>行业 <span class="required">*</span></label>
          <select v-model="form.industry" required>
            <option value="" disabled>请选择所属行业</option>
            <option value="快递物流">快递物流</option>
            <option value="专线运输">专线运输</option>
            <option value="仓储服务">仓储服务</option>
            <option value="跨境物流">跨境物流</option>
            <option value="冷链物流">冷链物流</option>
            <option value="物流设备">物流设备</option>
          </select>
        </div>
        
        <div class="form-group">
          <label>密码 <span class="required">*</span></label>
          <input v-model="form.password" type="password" required placeholder="请输入密码" />
        </div>
        
        <div class="form-group">
          <label>确认密码 <span class="required">*</span></label>
          <input v-model="form.confirmPassword" type="password" required placeholder="请再次输入密码" />
        </div>
        
        <div class="error-message" v-if="error">{{ error }}</div>
        
        <button type="submit" class="btn-register" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
        
        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({
  username: '',
  name: '',
  email: '',
  industry: '',
  password: '',
  confirmPassword: ''
})

async function handleRegister() {
  error.value = ''
  
  // 验证
  if (!form.industry) {
    error.value = '请选择所属行业'
    return
  }
  
  if (form.password !== form.confirmPassword) {
    error.value = '两次输入的密码不一致'
    return
  }
  
  if (form.password.length < 6) {
    error.value = '密码长度至少6位'
    return
  }
  
  loading.value = true
  
  try {
    // 调用注册API
    const res = await axios.post('/api/users/register', {
      username: form.username,
      name: form.name,
      email: form.email,
      industry: form.industry,
      password: form.password
    })
    
    if (res.data.code === 200 || res.data.code === 0) {
      // 注册成功，保存用户信息到localStorage
      const userData = res.data.data || res.data
      localStorage.setItem('user', JSON.stringify(userData))
      
      alert('注册成功！')
      router.push('/')
    } else {
      error.value = res.data.message || '注册失败'
    }
  } catch (e) {
    // 如果后端API不存在，使用模拟方式（开发阶段）
    if (e.response?.status === 404) {
      // 模拟注册成功
      const mockUser = {
        id: Date.now(),
        username: form.username,
        name: form.name,
        email: form.email,
        industry: form.industry,
        role: 'BUYER'
      }
      localStorage.setItem('user', JSON.stringify(mockUser))
      alert('注册成功（模拟）！')
      router.push('/')
    } else {
      error.value = e.response?.data?.message || '注册失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: calc(100vh - 80px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.register-card h1 {
  text-align: center;
  margin-bottom: 32px;
  color: #333;
  font-size: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.required {
  color: #ff4d4f;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #0066ff;
  box-shadow: 0 0 0 3px rgba(0, 102, 255, 0.1);
}

.error-message {
  color: #ff4d4f;
  font-size: 14px;
  margin-bottom: 16px;
  padding: 10px;
  background: #fff1f0;
  border-radius: 6px;
}

.btn-register {
  width: 100%;
  padding: 14px;
  background: #0066ff;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-register:hover:not(:disabled) {
  background: #0052cc;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 102, 255, 0.3);
}

.btn-register:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #0066ff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
