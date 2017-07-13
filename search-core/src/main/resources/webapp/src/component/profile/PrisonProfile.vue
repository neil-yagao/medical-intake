<template>
	<div>
  		<hr>
		<div class="row">
			<div class="col-md-2">
				<a class="thumbnail" style="background-color:rgba(255, 255, 252, 0.6)">
					<img :src="img" data-holder-rendered="true"> 
					<div class="caption text-center">
			    		<p>{{currentCode}}</p>
			    		<div id="operation-panel" v-if='identity != "prison"'>
				    		<button class="btn btn-info" @click="goBack()">
				    			<span class="glyphicon glyphicon-arrow-left"></span>
				    		</button>
				    		<button class="btn btn-danger btn-small"  @click="editProfile()">
								<span class="glyphicon glyphicon-edit"></span>
							</button>
						</div>
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
import Security from '../../security.js'
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
            tempObject: {},
            confirmIntakeFlag:true
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
        	if (Security.currentIdentity() != 'police') {
                //start to recording
                window.localStorage.setItem('last-usage', Math.floor(Date.now() / 1000))
            }
            console.info('query profile!')
            var id = this.$route.params.id
            this.currentCode = id;
            this.identity = Security.currentIdentity();
            this.$http.get('intake/miss/' + id + '/' + moment().format('YYYY-MM-DD'))
                .then((res) => {
                    var refused = []
                    for(var i in res.body){
                    	refused.push(res.body[i].need)
                    }
                    this.$http.get('inmate/medical/' + id).then((res) => {
                        this.tempObject = _.groupBy(res.body, 'time')
                        this.medicalList = []
                        var needConfirm = false;

                        for (var time in this.tempObject) {
                            var hour = moment().hour();
                            if (Vue.matchingPredefineTime(hour).indexOf(time) >= 0) {
            					needConfirm = true;
                            } 
                            var eachObject = {
                                'time': time,
                                'medicalList': this.tempObject[time]
                            }
                            if(refused.indexOf(time) >=0){
                            	eachObject['miss'] = true
                            }else {
                            	eachObject['miss'] = false
                            }
                            this.medicalList.push(eachObject)
                        }
                        if(needConfirm){
                        	window.localStorage.setItem('confirm-intake', false);
                        }else {
                        	window.localStorage.setItem('confirm-intake', true);
                        }
                        this.medicalList = _.sortBy(this.medicalList, function(m) {
                            return Vue.qualifiedTime().indexOf(m.time)
                        })
                    })
                })

            this.$http.get('inmates/' + id).then((res) => {
            	console.info(res.body)
                this.img = res.body[0].headPic
            })
        },
        confirmIntake() {
            var id = this.$route.params.id
            var matching = this.findMatchingMedicalList(this.tempObject);
            this.$http.post('inmate/intake/' + id, JSON.stringify(_.compact(matching))).then((res) => {
                console.info("confirm:" + id);
                window.localStorage.setItem('confirm-intake', true)
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
    mounted:function(){
    	this.queryCurrentProfile();
    },
    watch: {
        '$route': function(val){
        	if(val.path.indexOf('detail') > 0){
        		this.queryCurrentProfile();
        	}
        }
    },
    components: {
        'medical-panel': MedicalPanel
    }
}
</script>

<style>
</style>