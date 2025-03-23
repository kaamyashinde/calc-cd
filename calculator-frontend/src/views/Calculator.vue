<script setup>
import CalcBtn from '@/components/CalcBtn.vue'
import { ref, onMounted } from 'vue'
import Input from '@/components/Input.vue'
import NumBtnDisplay from '@/displays/NumBtnDisplay.vue'
import MiscBtnDisplay from '@/displays/MiscBtnDisplay.vue'
import OperatorBtnDisplay from '@/displays/OperatorBtnDisplay.vue'
import LastRowDisplay from '@/displays/LastRowDisplay.vue'
import CalcLog from '@/components/CalcLog.vue'
import { useCalculatorStore } from '@/stores/calculatorStore.js'
import { saveUserInfo } from '@/stores/userInfo'
import { useRouter } from 'vue-router'
import SessionTimer from '@/components/SessionTimer.vue'

const router = useRouter()
const store = useCalculatorStore()
const userInfo = saveUserInfo()

const handleBtnClick = (value) => {
  store.handleBtnClick(value)
}

const handleHistoryItemClick = (expression) => {
  store.text = expression
}

function goToReview() {
  router.push({ name: 'review' })
}

function logOut() {
  userInfo.clearUserInfo()
  router.push({ name: 'login' })
  store.setHistory([])
}

onMounted(async () => {
  try {
    const response = await fetch('http://128.251.48.227:8080/api/history', {
      headers: {
        Authorization: `Bearer ${userInfo.savedToken}`,
      },
    })
    if (response.ok) {
      const data = await response.json()
      store.setHistory(data.content)
    } else {
      console.error('Failed to fetch history:', response.statusText)
      if (response.status === 401) {
        userInfo.clearUserInfo()
        router.push('/login')
      }
    }
  } catch (error) {
    console.error('Error fetching history:', error)
  }
})
</script>

<template>
  <SessionTimer />
  <div>
    <button id="goToReview" @click="goToReview">Leave us a review?</button>
    <button id="logOut" @click="logOut">Log out</button>
  </div>
  <h1 class="welcome-message">Welcome, {{ userInfo.savedName }}!</h1>
  <div class="custom-container">
    <div class="calc-container">
      <div>
        <h1>Calculator</h1>
      </div>
      <div>
        <Input
          v-model:text="store.text"
          @update:text="store.updateText()"
          @perform-calculation="store.performCalculation()"
        />
      </div>
      <div class="btns-container">
        <div>
          <MiscBtnDisplay @miscBtnClick="handleBtnClick" />
          <NumBtnDisplay @numBtnClick="handleBtnClick" />
        </div>
        <div>
          <OperatorBtnDisplay @operatorBtnClick="handleBtnClick" />
        </div>
        <div class="add-span">
          <LastRowDisplay @lastRowClick="handleBtnClick" />
        </div>
      </div>
    </div>
    <div>
      <h1>Calculation Log</h1>
      <button id="clearLog" @click="store.clearHistory()">Clear Log</button>
      <CalcLog :resultArray="store.history" @history-item-click="handleHistoryItemClick" />
    </div>
  </div>
</template>

<style scoped>
.custom-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.calc-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
  background: #f8f9fa;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.btns-container {
  display: grid;
  grid-template-columns: 3fr 1fr;
  gap: 1rem;
}

.add-span {
  grid-column: span 2;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 1rem;
  font-weight: 600;
}

#goToReview,
#logOut {
  padding: 0.5rem 1rem;
  margin: 0.5rem;
  border: none;
  border-radius: 4px;
  background-color: #4a90e2;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

#goToReview:hover,
#logOut:hover {
  background-color: #357abd;
}

#logOut {
  background-color: #e74c3c;
}

#logOut:hover {
  background-color: #c0392b;
}

#clearLog {
  padding: 0.5rem 1rem;
  margin: 0.5rem auto;
  display: block;
  border: none;
  border-radius: 4px;
  background-color: #e74c3c;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

#clearLog:hover {
  background-color: #c0392b;
}

.welcome-message {
  text-align: center;
  color: #2c3e50;
  margin: 1rem 0;
  font-size: 2rem;
  font-weight: 600;
}
</style>
