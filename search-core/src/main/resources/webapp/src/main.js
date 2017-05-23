// The following line loads the standalone build of Vue instead of the runtime-only build,
// so you don't have to do: import Vue from 'vue/dist/vue'
// This is done with the browser options. For the config, see package.json
import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import router from './router.js'
import ExcelHelper from './component/general/ExcelHelper.js'
import PredefineTimeMatch from './component/general/PredefineTimeMatch.js'
require("xlsx")
window.jQuery = require("jquery")
require("bootstrap")

Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(ExcelHelper)
Vue.use(PredefineTimeMatch)
window._ = require('lodash');


window.Vue = new Vue({ // eslint-disable-line no-new
    router
}).$mount('#app')