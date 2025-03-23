<script setup>
import CalcBtn from '@/components/CalcBtn.vue'

const numbers = Array.from({ length: 9 }, (_, i) => i + 1)
</script>

<template>
  <div class="num-btn-container">
    <CalcBtn
      :custom-class="'num-btn'"
      v-for="number in numbers"
      :key="number"
      :placeholder="number.toString()"
      :btnType="'number'"
    />
  </div>
</template>

<style scoped>
.num-btn-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}

.num-btn {
  text-align: center;
}
</style>
