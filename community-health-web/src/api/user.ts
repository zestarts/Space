import request from '@/utils/request'

export function login(data: { username: string; password: string }) {
  return request.post('/api/user/login', data)
}

export function register(data: { username: string; password: string; phone: string; realName?: string }) {
  return request.post('/api/user/register', data)
}

export function getProfile() {
  return request.get('/api/user/profile')
}

export function updateProfile(data: any) {
  return request.put('/api/user/profile', data)
}

export function getHealthProfile() {
  return request.get('/api/user/health')
}

export function updateHealthProfile(data: any) {
  return request.put('/api/user/health', data)
}

export function listUsers(params: { page: number; size: number; keyword?: string }) {
  return request.get('/api/admin/users', { params })
}

export function updateUserStatus(id: number, status: number) {
  return request.put(`/api/admin/users/${id}/status`, { status })
}

export function getTodayRecord() {
  return request.get('/api/user/health/daily/today')
}

export function saveDailyRecord(data: any) {
  return request.post('/api/user/health/daily', data)
}

export function getDailyHistory(params?: { days?: number }) {
  return request.get('/api/user/health/daily/recent', { params })
}