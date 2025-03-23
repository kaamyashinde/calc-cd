<script setup>
import CalcBtn from '@/components/CalcBtn.vue'
import { ref } from 'vue'
const clear = ref('C')
const ans = ref('ANS')
const del = ref('DEL')
</script>

<template>
  <div class="misc-btn-container">
    <CalcBtn :customClass="'misc-btn'" :btnType="clear" :placeholder="clear" />
    <CalcBtn :customClass="'misc-btn'" :btnType="ans" :placeholder="ans" />
    <CalcBtn :customClass="'misc-btn'" :btnType="del" :placeholder="del" />
  </div>
</template>

<style scoped>
.misc-btn-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}

.misc-btn {
  text-align: center;
}
</style>
