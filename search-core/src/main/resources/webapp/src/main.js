// The following line loads the standalone build of Vue instead of the runtime-only build,
// so you don't have to do: import Vue from 'vue/dist/vue'
// This is done with the browser options. For the config, see package.json
import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import router from './router.js'
import ExcelHelper from './component/general/ExcelHelper.js'
require("xlsx")
window.jQuery = require("jquery")
require("bootstrap")

Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(ExcelHelper)
window._ = require('lodash');

window.qualifiedTime = ["早餐前", "早餐后", "午餐前", "午餐后", "晚餐前", "晚餐后", "临睡前"]

window.Vue = new Vue({ // eslint-disable-line no-new
    router
}).$mount('#app')