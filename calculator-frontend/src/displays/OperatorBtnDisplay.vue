<script setup>
import CalcBtn from '@/components/CalcBtn.vue'

const operators = ['+', '-', '*', '/']
</script>

<template>
  <div class="operator-btn-container">
    <CalcBtn
      :customClass="'operator-btn'"
      v-for="oprt in operators"
      :key="oprt"
      :placeholder="oprt"
      :btnType="'operator'"
    />
  </div>
</template>

<style scoped>
.operator-btn-container {
  display: grid;
  grid-template-columns: 1fr;
}

.operator-btn {
  text-align: center;
}
</style>
