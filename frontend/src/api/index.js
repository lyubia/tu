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

// AI API
export const aiAPI = {
  chat: (data) => api.post('/ai/chat', data)
}

// 为兼容现有代码，导出空对象
export const productAPI = {
  list: () => Promise.resolve({ data: { data: [] } }),
  popular: () => Promise.resolve({ data: { data: [] } }),
  detail: () => Promise.resolve({ data: { data: {} } }),
  categories: () => Promise.resolve({ data: { data: [] } })
}

export const feedbackAPI = {
  submit: () => Promise.resolve({ data: { success: true } }),
  listAll: () => Promise.resolve({ data: { data: [] } }),
  userFeedback: () => Promise.resolve({ data: { data: [] } }),
  update: () => Promise.resolve({ data: { success: true } })
}

export default api
