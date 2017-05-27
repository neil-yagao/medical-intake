import Login from './Login.vue'
import WorkingSpace from './WorkingPage.vue'
import ByNumberSearch from './component/nosearch/ByNumberSearch.vue'
import ByTimeSearch from './component/timesearch/ByTimeSearch.vue'
import VideoRecorder from './component/profile/VideoRecorder.vue'
import PrisonProfile from './component/profile/PrisonProfile.vue'
import PrisonMedicalInfo from './component/profile/PrisonMedicalInfo.vue'
import MasterDataRouter from './component/master-data/master-data-router.js'
import MedicalInventoryRouter from './component/medical-inventory/medical-inventory-router.js'
import VueRouter from 'vue-router'
import Security from './security.js'

const routes = [{
    path: "/working",
    component: WorkingSpace,
    children: [{
            path: "by-number",
            component: ByNumberSearch
        }, {
            path: "by-time",
            component: ByTimeSearch
        },
        MasterDataRouter,
        MedicalInventoryRouter, {
            path: 'detail/:id',
            component: PrisonProfile

        }, {
            path: 'add',
            component: PrisonMedicalInfo
        }, {
            path: 'edit/:id',
            component: PrisonMedicalInfo
        }
    ]

}, {
    path: '/',
    component: Login
}, {
    path: '/recording',
    component: VideoRecorder
}]

const router = new VueRouter({
    routes // short for routes: routes
})
var debounceToLogin = '';
var vedioRecording = false
var recordingWindow = '';
router.beforeEach((to, from, next) => {
    if (Security.isAllowTransfer(to.fullPath)) {
        if (to.fullPath.indexOf('working/detail') > 0) {
            showVideoRecordWindow()
        }
        next()
    } else {
        next(false)
    }

})

function showVideoRecordWindow() {
    var x = screen.width / 2 - 700 / 2;
    var y = screen.height / 2 - 450 / 2;
    if (!vedioRecording) {
        vedioRecording = true;
        console.info("url:" + window.location.href)
        recordingWindow = window.open('http://localhost:18080/#/recording', '_blank',
            'location=yes,height=400,width=500,scrollbars=yes,status=yes,left=' + x + ',top=' + y);
        var timer = setInterval(
            () => {
                if (recordingWindow && recordingWindow.closed) {
                    vedioRecording = false;
                    console.info("vedio recording is closed!")
                    clearInterval(timer)
                }
            }, 100)
    }
}

export default router;