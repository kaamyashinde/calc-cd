<script setup>
const props = defineProps({
  resultArray: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['historyItemClick'])

const handleHistoryItemClick = (result) => {
  // Extract the expression part (before the '=')
  const expression = result.split('=')[0].trim()
  emit('historyItemClick', expression)
}
</script>

<template>
  <div class="calc-log">
    <ul>
      <li
        v-for="(result, index) in resultArray"
        :key="index"
        @click="handleHistoryItemClick(result)"
        class="history-item"
      >
        {{ result }}
      </li>
    </ul>
  </div>
</template>

<style scoped>
.calc-log {
  background: #ffffff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 400px;
  overflow-y: auto;
}

li {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #e2e8f0;
  color: #2c3e50;
  font-size: 1rem;
}

li:last-child {
  border-bottom: none;
}

.history-item {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.history-item:hover {
  background-color: #f8f9fa;
}
</style>
