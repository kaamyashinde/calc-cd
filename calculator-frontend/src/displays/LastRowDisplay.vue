<script setup>
import CalcBtn from '@/components/CalcBtn.vue'
import { ref } from 'vue'

const dot = ref('.')
const zero = ref('0')
const equal = ref('=')
const free = ref(' ')

const yellowClass = 'yellow'
</script>

<template>
  <div class="last-row-container">
    <CalcBtn :btnType="free" :placeholder="free" />
    <CalcBtn :btnType="zero" :placeholder="zero" />
    <CalcBtn :btnType="dot" :placeholder="dot" />
    <CalcBtn :btnType="equal" :placeholder="equal" :btnClass="yellowClass" />
  </div>
</template>

<style scoped>
.last-row-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
}

.last-row-container {
  text-align: center;
}
</style>
