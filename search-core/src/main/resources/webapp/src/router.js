import Login from './Login.vue'
import WorkingSpace from './WorkingPage.vue'
import UploadFile from './component/general/UploadFile.vue'
import ByNumberSearch from './component/nosearch/ByNumberSearch.vue'
import ByTimeSearch from './component/timesearch/ByTimeSearch.vue'
import VideoRecorder from './component/profile/VideoRecorder.vue'
import PrisonProfile from './component/profile/PrisonProfile.vue'
import PrisonMedicalInfo from './component/profile/PrisonMedicalInfo.vue'
import MasterDataRouter from './component/master-data/master-data-router.js'
import MedicalInventoryRouter from './component/medical-inventory/medical-inventory-router.js'
import PrescriptionRecord from './component/master-data/PrescriptionRecord.vue'
import VueRouter from 'vue-router'
import Security from './security.js'

import VideoPlayer from './component/master-data/VideoPlayer.vue'

const routes = [{
    path: "/working",
    component: WorkingSpace,
    children: [{
            path: "prescription/by-number",
            component: ByNumberSearch
        }, {
            path: "by-time",
            component: ByTimeSearch
        }, {
            path: 'upload/:title',
            component: UploadFile
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
        }, {
            path: 'prescription/records',
            component: PrescriptionRecord
        }
    ]

}, {
    path: '/',
    component: Login
}, {
    path: '/recording',
    component: VideoRecorder
}, {
    path: '/intake-records/:time',
    component: VideoPlayer
}]

const router = new VueRouter({
    routes // short for routes: routes
})
var debounceToLogin = '';
var vedioRecording = false
var recordingWindow = '';
router.beforeEach((to, from, next) => {
    if (Security.isAllowTransfer(to.fullPath)) {
        var identity = Security.currentIdentity();
        if (to.fullPath.indexOf('working/detail') > 0 && identity == 'prison') {
            showVideoRecordWindow()
        }
        console.info("to" + to.fullPath)
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
        recordingWindow = window.open('http://localhost:8080/#/recording', '_blank',
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