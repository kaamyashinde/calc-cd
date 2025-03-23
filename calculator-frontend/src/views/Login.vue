<script lang="ts">
import { useField, useForm } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import { saveUserInfo } from '@/stores/userInfo'
import { useCalculatorStore } from '@/stores/calculatorStore'
import axios from 'axios'
import { ref } from 'vue'

export default {
  setup(props, ctx) {
    const router = useRouter()
    const store = saveUserInfo()
    const calcStore = useCalculatorStore()
    const loginError = ref('')

    //Define the validation schema
    const schema = yup.object({
      username: yup.string().required('Username is required'),
      password: yup.string().required('Password is required'),
    })

    //Initialise the form and fields
    const { handleSubmit, errors, isSubmitting, meta } = useForm({
      validationSchema: schema,
    })

    const { value: username, errorMessage: usernameError } = useField('username')
    const { value: password, errorMessage: passwordError } = useField('password')

    //Form submit handler
    const loginUser = handleSubmit(async (values) => {
      console.log('sending data to server...')
      loginError.value = ''
      try {
        const response = await axios.post('http://128.251.48.227:8080/auth/login', values)
        console.log('Login successful')
        console.log(response.data)
        console.log(response.data.user)
        store.setUserInfo(
          response.data.user.username,
          '', // empty email
          response.data.user.password,
          response.data.token,
        )
        await updateHistory(
          response.data.user.username,
          response.data.user.password,
          response.data.token,
        )
        router.push('/')
      } catch (error: any) {
        console.error('Login failed:', error)
        if (error.response?.status === 401) {
          loginError.value = 'Invalid username or password. Please try again.'
        } else {
          loginError.value = 'An error occurred during login. Please try again.'
        }
      }
    })

    async function updateHistory(username: string, password: string, token: string) {
      console.log('Retrieving history from the server...')
      try {
        const response = await axios.get(
          'http://localhost:8080/api/history?username=' +
            username +
            '&password=' +
            password +
            '&page=0&size=10',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )
        console.log('History retrieved successfully')
        console.log(response.data.content)
        calcStore.setHistory(response.data.content)
      } catch (error) {
        console.error('History retrieval failed:', error)
      }
    }

    function switchToRegister() {
      router.push('/register')
    }

    return {
      username,
      usernameError,
      password,
      passwordError,
      loginUser,
      switchToRegister,
      isSubmitting,
      meta,
      loginError,
    }
  },
}
</script>

<template>
  <div class="auth-container">
    <button id="loginBtn" @click="switchToRegister">Don't have an account? Register here</button>
    <div id="formContainer">
      <form @submit.prevent="loginUser">
        <div id="formTitle">
          <h1>Login</h1>
        </div>
        <div v-if="loginError" class="error-message">
          {{ loginError }}
        </div>
        <div class="form-group">
          <label id="usernameLabel">Username</label>
          <input type="text" v-model="username" placeholder="Enter your username" />
          <span class="error" id="usernameErrSpan">{{ usernameError }}</span>
        </div>

        <div class="form-group">
          <label id="passwordLabel">Password</label>
          <input type="password" v-model="password" placeholder="Enter your password" />
          <span class="error" id="passwordErrSpan">{{ passwordError }}</span>
        </div>
        <button id="submit" type="submit" :disabled="isSubmitting || !meta.valid">Login</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background-color: #f8f9fa;
}

#formContainer {
  background: white;
  padding: 2rem 3rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

#formTitle {
  margin-bottom: 2rem;
  text-align: center;
}

h1 {
  color: #2c3e50;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0;
}

.form-group {
  margin-bottom: 1.5rem;
  width: 100%;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
}

.error {
  display: block;
  color: #e74c3c;
  font-size: 0.875rem;
  margin-top: 0.5rem;
}

#loginBtn {
  margin-bottom: 2rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  background-color: #4a90e2;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

#loginBtn:hover {
  background-color: #357abd;
}

#submit {
  width: 100%;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  background-color: #4a90e2;
  color: white;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 1rem;
}

#submit:hover:not(:disabled) {
  background-color: #357abd;
}

#submit:disabled {
  background-color: #cbd5e0;
  cursor: not-allowed;
}

.error-message {
  background-color: #fee2e2;
  border: 1px solid #ef4444;
  color: #dc2626;
  padding: 0.75rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  text-align: center;
}
</style>
