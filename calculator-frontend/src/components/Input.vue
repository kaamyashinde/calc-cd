<script setup>
import { ref, watch } from 'vue'
import { useCalculatorStore } from '@/stores/calculatorStore.js'

const props = defineProps({
  text: {
    type: String,
    required: true,
  },
})

const store = useCalculatorStore()
const localText = ref(props.text)

watch(
  () => props.text,
  (newVal) => {
    localText.value = newVal
  },
)

function onInput(e) {
  const allowedKeys = /[0-9+\-*/.]|Backspace/
  if (!allowedKeys.test(e.data) && e.inputType !== 'deleteContentBackward') {
    e.target.value = localText.value
    console.log('inside if statement in OnInput')
    return
  }
  console.log('outside if statement in onInput')
  console.log('localtext: ' + localText)
  localText.value = e.target.value
  store.updateText(localText.value)
}

function getInputValue() {
  return localText.value
}
</script>

<template>
  <input @input="onInput" :value="localText" placeholder="Type here" />
</template>

<style scoped>
input {
  width: 100%;
  height: 50px;
  background-color: #ffffff;
  color: #2c3e50;
  border-radius: 8px;
  border: 2px solid #e2e8f0;
  padding: 0 1rem;
  font-size: 1.2rem;
  transition: all 0.3s ease;
  margin-bottom: 1rem;
}

input:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
}

input::placeholder {
  color: #a0aec0;
}
</style>
