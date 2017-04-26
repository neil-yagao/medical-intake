<template>
<div>
	<input type="text" class="form-control" v-model="medical" placeholder="药物名称">

	<table class="table table-hover">
		<thead>
			<tr>
				<td>名称</td><td>数量</td>
			</tr>
		</thead>
		<tbody>
			<tr v-for="matching in matchingList">
				<td>{{matching.name}}</td>
				<td><editable-input :value="matching.num" @save="updateMedicalData(matching,$event)"></editable-input></td>
			</tr>
		</tbody>
	</table>

</div>
</template>
<script>
import _ from "lodash"
import EditableInput from "../general/EditableInput.vue"
export default {
	name: "medical-list",
	data(){
		return {
			medical:"",
			allList:[]
		}
	},
	computed:{
		matchingList: function(){
			var matchingList = _.filter(this.allList, (me)=>{
				if(!this.medical) return true;
				return me.name.indexOf(this.medical) >=0
			})
			return _.orderBy(matchingList,'num','asc').slice(0,10);
		}
	},
	methods:{
		updateMedicalData: function(medical, num){
			medical.num = num
		}
	},
	mounted:function(){
		this.$http.get("medicals").then((response) =>{
			this.allList = response.body; 
		})

	},
	components:{
		'editable-input':EditableInput
	}

}

</script>