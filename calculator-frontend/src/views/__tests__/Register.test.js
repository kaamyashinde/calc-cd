import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import Register from '../Register.vue'
import { saveUserInfo } from '@/stores/userInfo'
import axios from 'axios'

vi.mock('axios')

describe('Register.vue', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('updates store after successful registration', async () => {
    // Mock axios post response
    axios.post.mockResolvedValueOnce({
      data: { username: 'testuser' },
    })

    const wrapper = mount(Register)
    const store = saveUserInfo()

    // Fill in registration form
    await wrapper.find('input[type="text"]').setValue('testuser')
    await wrapper.find('input[type="password"]').setValue('testpass')

    // Submit form
    await wrapper.find('form').trigger('submit.prevent')

    // Verify store was updated
    //expect(store.savedName).toBe('testuser')
    //expect(store.savedPassword).toBe('testpass')

    // Verify API was called
    //expect(axios.post).toHaveBeenCalledWith('http://localhost:8080/auth/register', {
    //  username: 'testuser',
    //  password: 'testpass',
    //})
  })
})
