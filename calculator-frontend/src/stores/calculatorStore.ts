import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { saveUserInfo } from './userInfo'
import { API_BASE_URL } from '../config'

export const useCalculatorStore = defineStore('calculator', () => {
  // State
  const clear = ref('C')
  const del = ref('DEL')
  const equal = ref('=')
  const ans = ref('ANS')
  const text = ref('')
  const ansValue = ref<string | number>('')
  const history = ref<string[]>([])
  const userInfo = saveUserInfo()

  // Actions
  const handleBtnClick = (value: string): void => {
    if (value === clear.value) {
      text.value = ''
    } else if (value === del.value) {
      text.value = text.value.slice(0, -1)
    } else if (value === equal.value) {
      // Use the async method to calculate via the REST backend.
      performCalculationAsync(text.value)
      text.value = ''
    } else if (value === ans.value) {
      if (ansValue.value === null || ansValue.value === '') {
        alert('Error: ANS value is null or empty')
        return
      }
      text.value += ansValue.value.toString()
    } else {
      text.value += value
    }
  }

  // Synchronous calculation method (kept for reference)
  const performCalculation = (expression: string): void => {
    try {
      if (!expression.trim()) {
        console.log('Error: Empty expression')
        return
      }
      const sanitizedExpression = expression.replace(/--/g, '+')
      if (/[+\-*/]$/.test(sanitizedExpression)) {
        console.log('Error: Invalid expression format:', sanitizedExpression)
        return
      }

      // Check for division by zero
      const match = sanitizedExpression.match(/(.*?)\/(0(?!\d))/)
      if (match) {
        const numerator = match[1]
        if (numerator.trim()) {
          try {
            const value = eval(numerator)
            combineResults(expression, value >= 0 ? 'Infinity' : '-Infinity')
          } catch (e) {
            combineResults(expression, 'Infinity')
          }
        } else {
          combineResults(expression, 'Infinity')
        }
        return
      }

      console.log('Evaluating:', sanitizedExpression)
      ansValue.value = new Function(`'use strict'; return (${sanitizedExpression})`)()
      combineResults(expression, ansValue.value)
    } catch (error) {
      console.error('Calculation error:', error)
    }
  }

  // New asynchronous method that uses the REST backend to perform the calculation
  const performCalculationAsync = async (expression: string): Promise<void> => {
    try {
      if (!expression.trim()) {
        console.log('Error: Empty expression')
        return
      }
      const payload = {
        expression: expression,
      }
      console.log('Sending expression to backend:', payload)
      const response = await axios.post(`${API_BASE_URL}/api/calculate`, payload, {
        headers: {
          Authorization: `Bearer ${userInfo.savedToken}`,
        },
      })
      const calculation = response.data
      ansValue.value = calculation.result
      combineResults(calculation.expression, calculation.result)

      // Refresh history after calculation
      try {
        const historyResponse = await axios.get(`${API_BASE_URL}/api/history`, {
          headers: {
            Authorization: `Bearer ${userInfo.savedToken}`,
          },
        })
        if (historyResponse.data.content) {
          setHistory(historyResponse.data.content)
        }
      } catch (historyError) {
        console.error('Error refreshing history:', historyError)
      }
    } catch (error: any) {
      console.error('Calculation error with backend:', error)
      if (error.response?.status === 401) {
        alert('Session expired. Please login again.')
        window.location.href = '/login'
      }
    }
  }

  function setHistory(
    historyData: Array<{
      id: number
      expression: string
      result: number | string
      timestamp: string
    }>,
  ) {
    history.value = historyData.map((item) => `${item.expression} = ${item.result}`)
  }

  const clearHistory = () => {
    history.value = []
  }

  const combineResults = (expression: string, result: string | number): void => {
    const resultString = `${expression} = ${result}`
    history.value.unshift(resultString)
  }

  const updateText = (newText: string): void => {
    text.value = newText
    console.log('updated text in store')
  }

  return {
    clear,
    del,
    equal,
    ans,
    text,
    ansValue,
    history,
    handleBtnClick,
    performCalculation,
    performCalculationAsync,
    combineResults,
    updateText,
    setHistory,
    clearHistory,
  }
})
