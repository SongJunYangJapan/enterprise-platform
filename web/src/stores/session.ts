import { defineStore } from 'pinia'
import { ref } from 'vue'
import { api } from '../api'

export const useSessionStore = defineStore('session', () => {
  const username = ref('')
  async function load() {
    const { data } = await api.get<{ username: string }>('/api/me')
    username.value = data.username
  }
  return { username, load }
})
