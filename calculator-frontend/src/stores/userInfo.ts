import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

export const saveUserInfo = defineStore('userInfo', () => {
  // Initialize state from localStorage if available
  const savedName = ref(localStorage.getItem('user_name') || '')
  const savedPassword = ref('')
  const savedToken = ref(localStorage.getItem('user_token') || '')
  const statusMessage = ref('hi')

  async function setUserInfo(
    username: string,
    _email: string,
    password: string,
    token: string = '',
  ) {
    savedName.value = username
    savedPassword.value = password
    savedToken.value = token

    // Save to localStorage
    localStorage.setItem('user_name', username)
    localStorage.setItem('user_token', token)
  }

  function clearUserInfo() {
    savedName.value = ''
    savedPassword.value = ''
    savedToken.value = ''

    // Clear localStorage
    localStorage.removeItem('user_name')
    localStorage.removeItem('user_token')
  }

  return {
    statusMessage,
    setUserInfo,
    clearUserInfo,
    savedName,
    savedPassword,
    savedToken,
  }
})
