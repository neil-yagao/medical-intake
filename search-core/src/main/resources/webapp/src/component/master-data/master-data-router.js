import MasterDataMain from './MasterDataMain.vue'
import MasterDataInmate from './MasterDataInmate.vue'
import RuntimeDataIntake from './IntakeRecords.vue'


var router = {
    path: "data-edit",
    component: MasterDataMain,
    children: [{
        path: "inmate",
        component: MasterDataInmate
    }, {
        path: "intake",
        component: RuntimeDataIntake
    }]
}


export default router;