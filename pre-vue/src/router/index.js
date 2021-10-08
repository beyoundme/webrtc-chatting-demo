import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login.vue')
  },
  {
    path: '/play',
    name: 'play',
    component: () => import('../views/play.vue'),
  },
  {
    path: '/vdo',
    name: 'vdo',
    component: () => import('../views/vdo.vue'),
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach(function (to, from, next) {
  if (to.path !== '/vdo'&&!sessionStorage.getItem('username')) {
    if (to.path !== '/login') {
      next('/login')
    }
  }
  next()
})

export default router
