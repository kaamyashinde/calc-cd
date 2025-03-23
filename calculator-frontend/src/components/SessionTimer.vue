<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { saveUserInfo } from '@/stores/userInfo'
import { useRouter } from 'vue-router'
import axios from 'axios'

const notification = ref('')
const router = useRouter()
const userInfo = saveUserInfo()
let timerInterval: number | undefined

async function refreshToken() {
  try {
    const response = await axios.post(
      'http://localhost:5170/auth/refresh',
      {},
      {
        headers: {
          Authorization: `Bearer ${userInfo.savedToken}`,
        },
      },
    )

    if (response.data.token) {
      userInfo.setUserInfo(
        userInfo.savedName,
        userInfo.savedPassword,
        response.data.token,
      )
      notification.value = 'Session refreshed'
      setTimeout(() => {
        notification.value = ''
      }, 3000)
      return true
    }
  } catch (error) {
    console.error('Failed to refresh token:', error)
    notification.value = 'Session expired'
    setTimeout(() => {
      userInfo.clearUserInfo()
      router.push('/login')
    }, 3000)
  }
  return false
}

function checkToken() {
  const token = userInfo.savedToken
  if (!token) {
    notification.value = 'Session expired'
    setTimeout(() => {
      userInfo.clearUserInfo()
      router.push('/login')
    }, 3000)
    return
  }

  // Get expiration time from JWT token (payload is in the middle part between dots)
  const payload = JSON.parse(atob(token.split('.')[1]))
  const expirationTime = payload.exp * 1000 // Convert to milliseconds
  const now = Date.now()
  const remainingTime = expirationTime - now

  // If less than 30 seconds remaining, try to refresh the token
  if (remainingTime <= 30000 && remainingTime > 0) {
    refreshToken()
  }

  if (remainingTime <= 0) {
    notification.value = 'Session expired'
    setTimeout(() => {
      userInfo.clearUserInfo()
      router.push('/login')
    }, 3000)
  }
}

onMounted(() => {
  checkToken()
  timerInterval = setInterval(checkToken, 1000) as unknown as number
})

onUnmounted(() => {
  if (timerInterval) {
    clearInterval(timerInterval)
  }
})
</script>

<template>
  <div v-if="notification" class="notification">
    {{ notification }}
  </div>
</template>

<style scoped>
.notification {
  position: fixed;
  top: 1rem;
  right: 1rem;
  background-color: #4a90e2;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.9rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}
</style>
