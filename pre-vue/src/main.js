import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import clickoutside from './util/clickoutside'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.prototype.$http = axios
Vue.directive('clickoutside', clickoutside) // 全局注册指令 v-clickoutside
axios.defaults.baseURL = 'https://' + require('os').hostname() + ':9443/'


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
