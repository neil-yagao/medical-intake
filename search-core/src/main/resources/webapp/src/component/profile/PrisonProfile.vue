<template>
	<div>
  		<hr>
		<div class="row">
			<div class="col-md-2">
				<a class="thumbnail" style="background-color:rgba(255, 255, 252, 0.6)">
					<img :src="img" data-holder-rendered="true"> 
					<div class="caption text-center">
			    		<p>{{currentCode}}</p>
			    		<button class="btn btn-info" @click="goBack()" v-if="this.identity == 'police'">
			    			<span class="glyphicon glyphicon-arrow-left"></span>
			    		</button>
			    		<button class="btn btn-danger btn-small"  @click="editProfile()" v-if='identity == "police"'>
							<span class="glyphicon glyphicon-edit"></span>
						</button>
			    	</div>
				</a>
			</div>
			<div class="col-md-8">
				<div class="row" style="max-height: 500px;overflow-y: scroll;">
					<div class="col-md-4" v-for=" medicalInfo in medicalList">
						<medical-panel :data="medicalInfo" :edit="false" @confirm-intake="confirmIntake()"></medical-panel>
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
import Vue from 'vue'
var states = ""
export default {
    name: 'personal-profile',
    data() {
        return {
            img: '',
            medicalList: [],
            currentCode: '',
            identity: '',
            websocket: '',
            tempObject:{}
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
                this.tempObject = _.groupBy(res.body, 'time')
                this.medicalList = []
                for (var time in  this.tempObject) {
                    this.medicalList.push({
                        'time': time,
                        'medicalList':  this.tempObject[time]
                    })
                }
                this.medicalList = _.sortBy(this.medicalList, function(m){
					return Vue.qualifiedTime().indexOf(m.time)
				})
               
            })
            window.localStorage.setItem('last-usage', Math.floor(Date.now() / 1000))
        },
        confirmIntake(){
        	var id = this.$route.params.id
            var matching = this.findMatchingMedicalList(this.tempObject);
            this.$http.post('inmate/intake/' + id, JSON.stringify(_.compact(matching))).then((res) => {
                console.info("confirm:" + id)
            })
        },
        findMatchingMedicalList(object) {
            var time = moment().hour()
            if (object[time]) {
                return object[time]
            }
            if (time <= 9) {
                return _.concat(object["早餐前"], object["早餐后"])
            } else if (time <= 14) {
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
        this.queryCurrentProfile()
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