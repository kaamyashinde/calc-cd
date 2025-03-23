<script lang="ts">
import { useField, useForm } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import { saveUserInfo } from '@/stores/userInfo'

export default {
  setup() {
    const router = useRouter()
    const store = saveUserInfo()

    // Define the validation schema
    const schema = yup.object({
      name: yup.string().required('Name is required'),
      email: yup.string().email('Invalid email').required('Email is required'),
      message: yup
        .string()
        .min(10, 'Message must be at least 10 characters long')
        .required('Message is required'),
    })

    // Initialize the form and fields
    const { handleSubmit, errors, isSubmitting, meta } = useForm({
      validationSchema: schema,
    })

    const { value: name, errorMessage: nameError } = useField('name')
    const { value: email, errorMessage: emailError } = useField('email')
    const { value: message, errorMessage: messageError } = useField<string>('message')

    // Form submit handler
    const submitReviewForm = handleSubmit(async (values) => {
      alert(store.statusMessage)
      router.push({ name: 'home' })
    })

    function goToCalc() {
      router.push({ name: 'home' }) // Navigate to the home route
    }

    return {
      name,
      nameError,
      email,
      emailError,
      message,
      messageError,
      submitReviewForm,
      errors,
      isSubmitting,
      meta,
      goToCalc,
    }
  },
}
</script>

<template>
  <div class="review-container">
    <button id="goToCalc" @click="goToCalc">Go back to the calculator</button>
    <div id="formContainer">
      <form @submit.prevent="submitReviewForm">
        <div id="formTitle">
          <h1>Leave us a review!</h1>
        </div>
        <div class="form-group">
          <label id="nameLabel">Name</label>
          <input type="text" v-model="name" placeholder="Enter your name" />
          <span class="error" id="nameErrSpan">{{ nameError }}</span>
        </div>
        <div class="form-group">
          <label id="emailLabel">Email</label>
          <input type="email" v-model="email" placeholder="Enter your email" />
          <span class="error" id="emailErrSpan">{{ emailError }}</span>
        </div>
        <div class="form-group">
          <label id="messageLabel">Message</label>
          <textarea v-model="message" placeholder="Tell us what you think..."></textarea>
          <span class="error" id="messageErrSpan">{{ messageError }}</span>
        </div>
        <button id="submit" type="submit" :disabled="isSubmitting || !meta.valid">
          Submit and Go back
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.review-container {
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
  max-width: 600px;
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

input,
textarea {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
  box-sizing: border-box;
  font-family: inherit;
}

textarea {
  min-height: 150px;
  resize: vertical;
}

input:focus,
textarea:focus {
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

#goToCalc {
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

#goToCalc:hover {
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

::placeholder {
  color: #a0aec0;
}
</style>
