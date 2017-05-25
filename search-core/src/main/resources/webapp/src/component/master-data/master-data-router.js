import MasterDataMain from './MasterDataMain.vue'
import MasterDataInmate from './MasterDataInmate.vue'
import UploadFile from './UploadFile.vue'
import RuntimeDataIntake from './IntakeRecords.vue'
import PrescriptionRecord from './PrescriptionRecord.vue'

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
    }, {
        path: 'prescription',
        component: PrescriptionRecord
    }]
}


export default router;