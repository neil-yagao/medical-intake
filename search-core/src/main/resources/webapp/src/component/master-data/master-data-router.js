import MasterDataMain from './MasterDataMain.vue'
import MasterDataInmate from './MasterDataInmate.vue'
import UploadFile from './UploadFile.vue'
import RuntimeDataIntake from './IntakeRecords.vue'

var router = {
    path: "data-edit",
    component: MasterDataMain,
    children: [{
        path: "",
        component: UploadFile
    }, {
        path: "inmate",
        component: MasterDataInmate
    }, {
        path: "intake",
        component: RuntimeDataIntake
    }]
}


export default router;