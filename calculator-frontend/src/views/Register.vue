<script lang="ts">
import { useField, useForm } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import { saveUserInfo } from '@/stores/userInfo'
import axios from 'axios'
import { ref } from 'vue'

export default {
  setup(props, ctx) {
    const router = useRouter()
    const store = saveUserInfo()
    const registrationError = ref('')

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
    const registerUser = handleSubmit(async (values) => {
      console.log('sending data to server...')
      registrationError.value = '' // Clear any previous error
      try {
        const response = await axios.post('http://128.251.48.227:8080/auth/register', values)
        console.log('Registration successful')
        store.setUserInfo(
          response.data.user.username,
          '', // empty email
          response.data.user.password,
          response.data.token,
        )
        router.push('/')
      } catch (error: any) {
        console.error('Registration failed:', error)
        if (error.response?.data === 'Username already exists') {
          registrationError.value = 'This username is already taken. Please choose another one.'
        } else {
          registrationError.value = 'Registration failed. Please try again.'
        }
      }
    })

    function switchToLogin() {
      router.push('/login')
    }

    return {
      username,
      usernameError,
      password,
      passwordError,
      registerUser,
      switchToLogin,
      isSubmitting,
      meta,
      registrationError,
    }
  },
}
</script>

<template>
  <div class="auth-container">
    <button id="registerBtn" @click="switchToLogin">Already have an account? Login here</button>
    <div id="formContainer">
      <form @submit.prevent="registerUser">
        <div id="formTitle">
          <h1>Register</h1>
        </div>
        <div v-if="registrationError" class="error-message">
          {{ registrationError }}
        </div>
        <div class="form-group">
          <label id="usernameLabel">Username</label>
          <input type="text" v-model="username" placeholder="Choose a username" />
          <span class="error" id="usernameErrSpan">{{ usernameError }}</span>
        </div>

        <div class="form-group">
          <label id="passwordLabel">Password</label>
          <input type="password" v-model="password" placeholder="Choose a password" />
          <span class="error" id="passwordErrSpan">{{ passwordError }}</span>
        </div>
        <button id="submit" type="submit" :disabled="isSubmitting || !meta.valid">Register</button>
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

.error-message {
  background-color: #fee2e2;
  border: 1px solid #ef4444;
  color: #dc2626;
  padding: 0.75rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  text-align: center;
}

#registerBtn {
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

#registerBtn:hover {
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
</style>
