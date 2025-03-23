import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import CalcBtn from '@/components/CalcBtn.vue'
import { useCalculatorStore } from '@/stores/calculatorStore.ts'
import { wrap } from 'module'

describe('CalcBtn tests', () => {
  let wrapper
  beforeEach(() => {
    setActivePinia(createPinia())
    wrapper = mount(CalcBtn, {
      props: {
        placeholder: '1',
        btnType: 'number',
        customClass: 'custom-class',
        btnClass: 'btn-class',
      },
    })
  })

  it('reenders the button with the correct placeholder', () => {
    const button = wrapper.find('button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('1')
  })

  it('applies the correct classes to the button', () => {
    const button = wrapper.find('button')
    expect(wrapper.classes()).toContain('custom-class')
    expect(button.classes()).toContain('btn-class')
  })

  it('calls handleBtnClick with the correct value when clicked', async () => {
    const store = useCalculatorStore()
    const handleBtnClickSpy = vi.spyOn(store, 'handleBtnClick')

    await wrapper.find('button').trigger('click')
    expect(handleBtnClickSpy).toHaveBeenCalledWith('1')
  })
})
