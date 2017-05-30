<template>
	<div class="margin">
		<div class="panel panel-transparent" :class="matchingTime(data.time)?'panel-info':'panel-warning'">
		<!-- Default panel contents -->
			<div class="panel-heading">服药时间：{{data.time}}<span style="float:right" class="glyphicon glyphicon-remove" @click="deletePanel()" v-if="edit"></span></div>
			  <!-- Table -->
		  	<div class="panel-body">
				<table class="table">
					<thead>
						<tr><td>#</td><td>药物</td><td>数量</td><td v-if="edit"></td></tr>
					</thead>
					<tbody>
						<tr v-for="(medical,$index) in data.medicalList" :key="medical.medical">
							<td>{{$index + 1}}</td>
							<td>{{medical.medical}}</td>
							<td>{{medical.amount}}</td>
							<td @click="deleteMedical(medical)" role="button" v-if="edit"><span>&times;</span></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="panel-footer text-right" v-if="matchingTime(data.time)">
				<button class="btn btn-info" @click="confirm()" >确认服药</button>
			</div>
		</div>
	</div>
</template>
<script>
import _ from "lodash";
import moment from 'moment';
import Vue from 'vue';
export default {
	name:'medical-panel',
	props:['data','edit'],
	methods:{
		deletePanel:function(medical){
			this.$emit('delete')
		},
		deleteMedical: function(medical){
			console.info("delete:" + JSON.stringify(medical))
			this.$emit('delete-medical', {name: medical.medical, time: this.data.time})
		},
		matchingTime(time){
			var hour = moment().hour();
			return Vue.matchingPredefineTime(hour).indexOf(time) >= 0;
		},
		confirm(){
			this.$emit('confirm-intake')
		}
	}

}	
</script>

<style>
.margin{
	margin-top:10px;
}
.panel-transparent {
    background: rgba(255, 255, 252, 0.6)!important;
}

</style>