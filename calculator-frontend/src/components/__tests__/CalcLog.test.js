import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import CalcLog from '@/components/CalcLog.vue'

describe('CalcLog.vue', () => {
  it('renders the component', () => {
    const wrapper = mount(CalcLog, {
      props: {
        resultArray: [],
      },
    })
    expect(wrapper.exists()).toBe(true)
  })

  it('renders the correct number of results', () => {
    const results = ['Result 1', 'Result 2', 'Result 3']
    const wrapper = mount(CalcLog, {
      props: {
        resultArray: results,
      },
    })
    const listItems = wrapper.findAll('li')
    expect(listItems.length).toBe(results.length)
  })

  it('renders the correct results', () => {
    const results = ['Result 1', 'Result 2', 'Result 3']
    const wrapper = mount(CalcLog, {
      props: {
        resultArray: results,
      },
    })
    const listItems = wrapper.findAll('li')
    listItems.forEach((item, index) => {
      expect(item.text()).toBe(results[index])
    })
  })
})
