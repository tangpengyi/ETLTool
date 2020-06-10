// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import router from './router'
import axios from 'axios'

import Vuex from 'vuex'
import store from './store'

Vue.use(Vuex);

Vue.prototype.axios = axios;
Vue.prototype.HOST = '/api';

Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.config.productionTip = false;


router.beforeEach((to, form, next) => {

  // 如果请求的是登录页
  if (to.path == '/login') {
    // 跳转到首页
    next({path: '/'});
  } else if (to.path == '/index') {
    next({path: '/'});
  }

  // 下一个路由
  next();
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
});
