import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getProfile } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const user = ref<any>(null)
  const token = ref(localStorage.getItem('token') || '')

  function setToken(t: string) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
  }

  async function fetchProfile() {
    try {
      user.value = await getProfile()
    } catch {
      logout()
    }
  }

  return { user, token, setToken, logout, fetchProfile }
})