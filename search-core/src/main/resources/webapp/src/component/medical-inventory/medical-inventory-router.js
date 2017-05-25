import MedicalInventoryMain from './MedicalInventoryMain.vue'
import MasterDataMedical from './MasterDataMedical.vue'
import MedicalInbound from './MedicalInbound.vue'
import MedicalOutbound from './MedicalOutbound.vue'

var router = {
    path: 'medical-inventory',
    component: MedicalInventoryMain,
    children: [{
            path: 'overall',
            component: MasterDataMedical
        }, {
            path: 'inbound',
            component: MedicalInbound
        }, {
            path: 'outbound',
            component: MedicalOutbound
        }
        /*,{
                   path: 'prescription',
                   component: PrescriptionRecord
               }*/


    ]
}
export default router