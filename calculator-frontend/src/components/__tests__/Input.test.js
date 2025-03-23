import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import Input from '@/components/Input.vue'
import { useCalculatorStore } from '@/stores/calculatorStore.js'

describe('Input tests', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('renders the input field', () => {
    const wrapper = mount(Input, {
      props: {
        text: '',
      },
    })
    const input = wrapper.find('input')
    expect(input.exists()).toBe(true)
  })

  it('displays the correct initial value', () => {
    const wrapper = mount(Input, {
      props: {
        text: '123',
      },
    })
    const input = wrapper.find('input')
    expect(input.element.value).toBe('123')
  })

  it('prevents invalid input', async () => {
    const wrapper = mount(Input, {
      props: {
        text: '',
      },
    })
    const input = wrapper.find('input')
    await input.setValue('abc')
    expect(input.element.value).toBe('')
  })
})
