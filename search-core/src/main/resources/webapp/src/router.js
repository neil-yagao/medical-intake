import Login from './Login.vue'
import WorkingSpace from './WorkingPage.vue'
import ByNumberSearch from './component/nosearch/ByNumberSearch.vue'
import ByTimeSearch from './component/timesearch/ByTimeSearch.vue'
import VideoRecorder from './component/profile/VideoRecorder.vue'
import PrisonProfile from './component/profile/PrisonProfile.vue'
import PrisonMedicalInfo from './component/profile/PrisonMedicalInfo.vue'
import MasterDataMain from './component/master-data/MasterDataMain.vue'
import MasterDataMedical from './component/master-data/MasterDataMedical.vue'
import MasterDataInmate from './component/master-data/MasterDataInmate.vue'
import UploadFile from './component/master-data/UploadFile.vue'
import VueRouter from 'vue-router'

const routes = [{
    path: "/working",
    component: WorkingSpace,
    children: [{
        path: "by-number",
        component: ByNumberSearch
    }, {
        path: "by-time",
        component: ByTimeSearch
    }, {
        path: "data-edit",
        component: MasterDataMain,
        children: [{
            path: "",
            component: UploadFile
        }, {
            path: "medical",
            component: MasterDataMedical
        }, {
            path: "inmate",
            component: MasterDataInmate
        }]
    }, {
        path: 'detail/:id',
        component: PrisonProfile

    }, {
        path: 'add',
        component: PrisonMedicalInfo
    }, {
        path: 'edit/:id',
        component: PrisonMedicalInfo
    }]

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

router.beforeEach((to, from, next) => {
    var identity = window.localStorage.getItem('identity');
    if (!identity) {
        next(false)
    }
    /*var self = this;
    if (debounceToLogin) {
        debounceToLogin.cancel()
    }
    debounceToLogin = _.debounce(function() {
        console.info("calling debounce!")
        window.localStorage.setItem('identity', '');
        vedioRecording = false;
        window.location.href = "#/"
    }, 10 * 1000);
    if()
    debounceToLogin()*/
    if (identity != 'police') {
        if (to.fullPath.indexOf('working/detail') > 0 //to detail
            ||
            to.fullPath == "/" ||
            to.fullPath.indexOf('recording') > 0) { //to login
            if (to.fullPath.indexOf('working/detail') > 0) {
                showVideoRecordWindow()
            }
            next()
        } else {
            next(false)
        }
    } else {
        next();
    }

})

function showVideoRecordWindow() {
    var x = screen.width / 2 - 700 / 2;
    var y = screen.height / 2 - 450 / 2;
    console.info("start recording!")
    if (!vedioRecording) {
        vedioRecording = true;
        console.info("url:" + window.location.href)
        var child = window.open('http://localhost:18080/#/recording', '_blank',
            'location=yes,height=400,width=500,scrollbars=yes,status=yes,left=' + x + ',top=' + y);
        var timer = setInterval(
            () => {
                if (child && child.closed) {
                    vedioRecording = false;
                }
            }, 100)
    }
}

export default router;