import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import Calculator from '@/views/Calculator.vue'
import Input from '@/components/Input.vue'
import NumBtnDisplay from '@/displays/NumBtnDisplay.vue'
import MiscBtnDisplay from '@/displays/MiscBtnDisplay.vue'
import OperatorBtnDisplay from '@/displays/OperatorBtnDisplay.vue'
import LastRowDisplay from '@/displays/LastRowDisplay.vue'
import CalcLog from '@/components/CalcLog.vue'
import { createRouter } from 'vue-router'
import router from '@/router'

describe('Calculator.vue', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('renders the Calculator component', () => {
    const wrapper = mount(Calculator)
    expect(wrapper.exists()).toBe(true)
  })

  it('renders the Input component', () => {
    const wrapper = mount(Calculator)
    const inputComponent = wrapper.findComponent(Input)
    expect(inputComponent.exists()).toBe(true)
  })

  it('renders the NumBtnDisplay component', () => {
    const wrapper = mount(Calculator)
    const numBtnDisplayComponent = wrapper.findComponent(NumBtnDisplay)
    expect(numBtnDisplayComponent.exists()).toBe(true)
  })

  it('renders the MiscBtnDisplay component', () => {
    const wrapper = mount(Calculator)
    const miscBtnDisplayComponent = wrapper.findComponent(MiscBtnDisplay)
    expect(miscBtnDisplayComponent.exists()).toBe(true)
  })

  it('renders the OperatorBtnDisplay component', () => {
    const wrapper = mount(Calculator)
    const operatorBtnDisplayComponent = wrapper.findComponent(OperatorBtnDisplay)
    expect(operatorBtnDisplayComponent.exists()).toBe(true)
  })

  it('renders the LastRowDisplay component', () => {
    const wrapper = mount(Calculator)
    const lastRowDisplayComponent = wrapper.findComponent(LastRowDisplay)
    expect(lastRowDisplayComponent.exists()).toBe(true)
  })

  it('renders the CalcLog component', () => {
    const wrapper = mount(Calculator)
    const calcLogComponent = wrapper.findComponent(CalcLog)
    expect(calcLogComponent.exists()).toBe(true)
  })

  it('renders the leave us a review button', () => {
    const wrapper = mount(Calculator)
    const button = wrapper.find('#goToReview')
    expect(button.exists()).toBe(true)
  })

  it('navigates to the review page when the leave us a review button is clicked', async () => {
    const pushSpy = vi.spyOn(router, 'push')
    const wrapper = mount(Calculator, {
      global: {
        plugins: [router],
      },
    })
    const button = wrapper.find('#goToReview')

    await button.trigger('click')
    expect(pushSpy).toHaveBeenCalledWith({ name: 'review' })
  })
})
