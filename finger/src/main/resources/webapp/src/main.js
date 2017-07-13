// The following line loads the standalone build of Vue instead of the runtime-only build,
// so you don't have to do: import Vue from 'vue/dist/vue'
import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import App from './App.vue'

window.jQuery = require("jquery")
require("bootstrap")


Vue.use(VueRouter)
Vue.use(VueResource)

window._ = require('lodash');

new Vue({ // eslint-disable-line no-new
    el: '#app',
    render: (h) => h(App)
})