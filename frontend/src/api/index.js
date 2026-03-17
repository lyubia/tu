import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 试用 API - v2.0 简化版
export const trialAPI = {
  create: (data) => api.post('/trials', data),
  detail: (id) => api.get(`/trials/${id}`),
  userTrials: (userId) => api.get(`/trials/user/${userId}`),
  feedback: (id, data) => api.post(`/trials/${id}/feedback`, data),
  extend: (id, days) => api.post(`/trials/${id}/extend`, null, { params: { days } }),
  stats: () => api.get('/trials/stats')
}

// 产品 API
export const productAPI = {
  list: (params) => api.get('/products', { params }),
  detail: (id) => api.get(`/products/${id}`),
  categories: () => api.get('/products/categories'),
  popular: (limit) => api.get('/products/popular', { params: { limit } })
}

// 反馈 API
export const feedbackAPI = {
  submit: (data) => api.post('/feedback', data),
  list: (params) => api.get('/feedback', { params })
}

// AI API
export const aiAPI = {
  chat: (data) => api.post('/ai/chat', data)
}

// 对话 API
export const conversationAPI = {
  create: (userId) => api.post('/api/conversation/create', { userId }),
  history: (userId) => api.get(`/api/conversation/history/${userId}`)
}

// 消息 API
export const messageAPI = {
  save: (data) => api.post('/api/message/save', data),
  list: (conversationId) => api.get(`/api/message/${conversationId}`)
}

// 需求 API
export const requirementAPI = {
  list: (conversationId) => api.get(`/api/requirement/${conversationId}`),
  save: (data) => api.post('/api/requirement/save', data)
}

export default api
