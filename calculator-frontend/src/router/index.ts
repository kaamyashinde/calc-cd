import { createRouter, createWebHistory } from 'vue-router'
import Forms from '@/views/Forms.vue'
import { saveUserInfo } from '@/stores/userInfo'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Calculator.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/review',
      name: 'review',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Forms.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
      meta: { requiresAuth: false },
    },
  ],
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const store = saveUserInfo()
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)
  const isAuthenticated = store.savedName !== ''

  if (requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (!requiresAuth && isAuthenticated) {
    next('/')
  } else {
    next()
  }
})

export default router
