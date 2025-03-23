<script setup>
import { useCalculatorStore } from '@/stores/calculatorStore'
const store = useCalculatorStore()

const handleBtnClick = (value) => {
  store.handleBtnClick(value)
}
const props = defineProps({
  placeholder: {
    type: String,
    required: true,
  },
  btnType: {
    type: String,
    required: true,
  },
  customClass: {
    type: String,
    required: false,
  },
  btnClass: {
    type: String,
    required: false,
  },
})
</script>

<template>
  <div :class="customClass">
    <button :class="btnClass" @click="handleBtnClick(props.placeholder)">
      {{ props.placeholder }}
    </button>
  </div>
</template>

<style scoped>
button {
  width: 100%;
  height: 40px;
  border: none;
  border-radius: 8px;
  color: #2c3e50;
  background-color: #ffffff;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin: 4px;
}

button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.15);
  background-color: #f8f9fa;
}

button:active {
  transform: translateY(1px);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* Operator buttons */
button[class*='operator'] {
  background-color: #4a90e2;
  color: white;
}

button[class*='operator']:hover {
  background-color: #357abd;
}

/* Special buttons (C, DEL, ANS) */
button[class*='misc'] {
  background-color: #e74c3c;
  color: white;
}

button[class*='misc']:hover {
  background-color: #c0392b;
}

/* Equal button */
button.yellow {
  background-color: #f1c40f;
  color: #2c3e50;
}

button.yellow:hover {
  background-color: #f39c12;
}
</style>
