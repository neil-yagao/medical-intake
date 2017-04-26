<template>
<div>
	<div class="row">
		<div class="col-md-9">
			<div class="row">
				<div class="col-md-5">
					<div class="input-group">
					  <span class="input-group-addon">服刑人员编号</span>
					  <div :class="code?'':'has-error'" class="form-group "><input type="text" class="form-control" v-model="code"></div>
					</div>
				</div>

				<div class="col-md-3 col-md-offset-1" style="visibility:hidden">
					<label>上传头像:</label><input id="fileUpload" ref="fileUpload" type="file" @change="onFileChange" style="display:inline-block;max-width:180px;margin-left:10px">
				</div>
			</div>
			<div class="panel panel-default margint">
				<div class="panel-heading"><label>所需使用的药物</label></div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-4">
							<div class="input-group">
								<div :class=" medicalInfo.time?'':'has-error'" class="form-group "><input class="form-control" v-model="medicalInfo.time"></input></div>
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">服药时间 
									<span class="caret"></span></button>
									<ul class="dropdown-menu">
										<li v-for="value in qualifiedTime" @click="selectDropDown(value)"><a>{{value}}</a></li>
									</ul>
								</div><!-- /btn-group -->
								
							</div>
						</div>
						<div class="col-md-4">
						    <medical-selector @selected="selectedMedicals($event)" ></medical-selector>
						</div>
						<div class="col-md-2">
							<div :class=" medicalInfo.num?'':'has-error'" class="form-group">
								<input type="text" class="form-control" placeholder="数量" v-model="medicalInfo.num">
							</div>	
						</div>
						<div class="col-md-2">
							<button class="btn btn-default" @click="pushInfo()">添加</button>
						</div>
					</div>
				</div>
			</div>	
			
			<div class="row margint" style="max-height: 350px;overflow-y: scroll;">
				<div class="col-md-4" v-for=" medicalInfo in medicalList" >
					<medical-panel :data="medicalInfo" :edit="true" @delete="deletePanel(medicalInfo)" @delete-medical="deleteMedical(medicalInfo, $event)"></medical-panel>
				</div>
			</div>
		</div>	
		<div class="col-md-3">
			<a class="thumbnail" @click="uploadFile()"> <img id="header" src="img/panda.jpg" style="height: 180px; width:100%" data-holder-rendered="true"> </a>
		</div>
	</div>
	<hr>
	<div class="row margint" v-if="medicalList.length > 0">
		<div class="col-md-2 col-md-offset-10" >
			<button class="btn btn-success" @click="saveMedicalInfo()">保存</button>
		</div>
	</div>
	<div class="alert" :class="alertClass" role="alert"v-show="showAlert">{{alertMsg}}</div>
</div>
</template>
<script type="text/javascript">
import MedicalPanel from './MedicalPanel.vue'
import MedicalSelector from './MedicalSelectionModal.vue'
import _ from "lodash"
export default {
	name :'add-prison-medical',
	data(){
		return {
			qualifiedTime:["早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","临睡前"],
			medicalInfo:{
				time:"",
				num:""
			},
			medicalList: this.prison.medicalList? this.prison.medicalList:[]/*[
					*
						{
							time:""
							medicals:[
								{
									name:"",
									num:""
								}
							]
						}
					
			]*/,
			code:this.prison.code,
			imageData:this.prison.img,
			showAlert:false,
			alertClass:  "alert-danger",
			alertMsg :''
		}
	},
	methods:{
		onFileChange: function(event){
			var input = event.target;
            // Ensure that you have a file before attempting to read it
            if (input.files && input.files[0]) {
                // create a new FileReader to read this image and convert to base64 format
                var reader = new FileReader();
                // Define a callback function to run, when FileReader finishes its job
                reader.onload = (e) => {
                    // Note: arrow function used here, so that "this.imageData" refers to the imageData of Vue component
                    // Read image as base64 and set to imageData
                    this.imageData = e.target.result;
                    //use formData to upload file
                }
                // Start the reader job - read file as a data url (base64 format)
                reader.readAsDataURL(input.files[0]);
            }
		},
		deleteMedical: function(medicalInfo, data){
			console.info("handle delete medical:" + JSON.stringify(data));
			medicalInfo.medicalList = _.filter(medicalInfo.medicalList, function(m){
				return m.name != data.name
			});
			if(medicalInfo.medicalList.length == 0){
				this.medicalList = _.filter(this.medicalList, function(m){
					return m.time != medicalInfo.time
				})
			}
		},
		uploadFile:function(){
			this.$refs.fileUpload.click()
		},
		selectDropDown: function(value){
			this.medicalInfo.time = value;
		},
		selectedMedicals:function(medicals){
			this.medicalInfo.name = medicals;
		},
		deletePanel:function(medicals){
			this.medicalList = _.filter(this.medicalList, function(m){
				return m.time != medicals.time
			})
		},
		pushInfo:function(){
			var self = this;
			var added = false;
			var medcialsForEachTime = []
			if(!self.medicalInfo.name || !self.medicalInfo.time || !self.medicalInfo.num){
				return
			}
			_.each(_.split(self.medicalInfo.name,','),function(n){
				if(self.medicalInfo.num > 0){
					medcialsForEachTime.push({
						medical:n,
						amount:self.medicalInfo.num
					})	
				}
			});
			var tempMedicalInfo = {
				'time':self.medicalInfo.time,
				'medicalList': medcialsForEachTime
			}
			_.each(this.medicalList, function(mInfo){
				if(mInfo.time == self.medicalInfo.time){
					mInfo.medicalList = _.unionBy(medcialsForEachTime, mInfo.medicalList, 'medical')
					added = true;
				}
			});
			if(!added){
				this.medicalList.push(tempMedicalInfo)
			}
			this.medicalList = _.sortBy(this.medicalList, function(m){
				return self.qualifiedTime.indexOf(m.time)
			})
		},
		saveMedicalInfo: function(){
			if(!this.code){
				this.showAlert = true
				this.alertMsg = "服刑人员编号为空，无法保存!"
				var self = this
				setTimeout(function(){
					self.showAlert = false;
				}, 3000);
				return
			}
			var self = this;
			//flattern the list
			var flatternPrisonMedicalList = []
			var prisonMedicalInfo = _.each(this.medicalList, function(m){
				_.each(m.medicalList, function(xm){
					flatternPrisonMedicalList.push(
						{
							'code':self.code,
							'time':m.time,
							'medical': xm.medical,
							'amount':xm.amount
						})
					});
				})
			console.info(flatternPrisonMedicalList)
			this.$http.post('inmate/medical', JSON.stringify(flatternPrisonMedicalList)).then((res) =>{
				if(res.body.success){
					this.showAlert = true
					this.alertMsg = "添加成功"
					this.alertClass = 'alert-success'
					var self = this
					setTimeout(function(){
						self.showAlert = false;
					}, 3000)
				}
			})
		}
	},
	components: {
		'medical-panel': MedicalPanel,
		MedicalSelector
	},
	props:{
		'prison':{
			type:Object,
			default:function(){
				return {
				'medicalList':[]
			}}
		}
		
	},
	mounted:function(){
		var id = this.$route.params['id'];
		if(id){
			this.code = id;
			this.$http.get('inmate/medical/' + id).then((res)=>{
			this.imageData = 'img/panda.jpg'
			var tempObject = _.groupBy(res.body, 'time')
			for(var time in tempObject){
				this.medicalList.push({
					'time':time,
					'medicalList': tempObject[time]
				})
			}
		})
		}
	}
}
</script>
<style>
.margint{
  margin-top:10px;
}
.text-green {
	color: springgreen;
}
</style>
