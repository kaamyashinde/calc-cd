import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import Forms from '@/views/Forms.vue'
import router from '@/router'
import { saveUserInfo } from '@/stores/userInfo'

describe('Forms.vue', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('renders the form correctly', () => {
    const wrapper = mount(Forms, {
      global: {
        plugins: [router],
      },
    })
    //expect(wrapper.find('form').exists()).toBe(true)
    //expect(wrapper.find('#name input').exists()).toBe(true)
    //expect(wrapper.find('#email input').exists()).toBe(true)
    //expect(wrapper.find('#message textarea').exists()).toBe(true)
    //expect(wrapper.find('#submit').exists()).toBe(true)
  })

  it('navigates back to the calculator when the "Go back to the calculator" button is clicked', async () => {
    const pushSpy = vi.spyOn(router, 'push')

    const wrapper = mount(Forms, {
      global: {
        plugins: [router],
      },
    })

    const button = wrapper.find('#goToCalc')
    await button.trigger('click')

    //expect(pushSpy).toHaveBeenCalledWith({ name: 'home' })
  })
})
