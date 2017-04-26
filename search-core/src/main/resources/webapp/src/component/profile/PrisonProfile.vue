<template>
	<div>
  		<hr>
		<div class="row">
			<div class="col-md-2">
				<a class="thumbnail">
					<img :src="img" data-holder-rendered="true"> 
					<div class="caption text-center">
			    		<p>{{currentCode}}</p>
			    		<p><button class="btn btn-info" @click="goBack()">返回</button></p>
			    	</div>
				</a>
			</div>
			<div class="col-md-8">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title" style="display:inline-block">需服用药物</h3>
						<button class="btn btn-warning btn-xs" style="float:right" @click="editProfile()" v-if='identity == "police"'>编辑</button>
					</div>
					<div class="panel-body">
						<div class="row" style="max-height: 500px;overflow-y: scroll;">
							<div class="col-md-6" v-for=" medicalInfo in medicalList" >
							<medical-panel :data="medicalInfo" :edit="false"></medical-panel>
						</div>
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</template>
<script>

import MedicalPanel from './MedicalPanel.vue'
import router from '../../router.js'
import moment from "moment"
var states = ""
export default {
    name: 'personal-profile',
    data() {
        return {
            img: '',
            medicalList: [],
            currentCode: '',
            identity: '',
            websocket: ''
        }
    },
    methods: {
        goBack: function() {
            window.history.back()
        },
        editProfile: function() {
            window.location.href = '#/working/edit/' + this.currentCode
        },
        queryCurrentProfile() {
            var id = this.$route.params.id
            this.currentCode = id;
            this.identity = window.localStorage.getItem('identity');
            this.$http.get('inmate/medical/' + id).then((res) => {
                this.img = 'img/head/' + id + ".png"
                var tempObject = _.groupBy(res.body, 'time')
                this.medicalList = []
                for (var time in tempObject) {
                    this.medicalList.push({
                        'time': time,
                        'medicalList': tempObject[time]
                    })
                }
                if (this.identity == 'prison') {
                    var matching = this.findMatchingMedicalList(tempObject);
                    console.info(matching)
                    this.$http.post('inmate/intake/' + id).then((res) => {
                        console.info("confirm:" + id)
                    })
                }

            })

        },
        findMatchingMedicalList(object) {
            var time = moment().hour()
            if (object[time]) {
                return object[time]
            }
            var range = "";
            if (time <= 9) {
                return _.concat(object["早餐前"], object["早餐后"])
            } else if (time <= 13) {
                return _.concat(object["午餐前"], object["午餐后"])
            } else if (time <= 19) {
                return _.concat(object["晚餐前"], object["晚餐后"])
            } else {
                return object["临睡前"]
            }

        }
    },
    created: function() {
        this.identity = window.localStorage.getItem('identity');
        console.info(this.currentCode)
        this.queryCurrentProfile(this.currentCode)
    },
    watch: {
        '$route': 'queryCurrentProfile'
    },
    components: {
        'medical-panel': MedicalPanel
    }
}
</script>

<style>
	
</style>