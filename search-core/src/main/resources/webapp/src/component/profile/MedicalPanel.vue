<template>
	<div class="margin">
		<div class="panel panel-default">
		<!-- Default panel contents -->
			<div class="panel-heading">服药时间：{{data.time}}<span style="float:right" class="glyphicon glyphicon-remove" @click="deletePanel()" v-if="edit"></span></div>
			  <!-- Table -->
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
	</div>
</template>
<script>
import _ from "lodash"
export default {
	name:'medical-panel',
	props:['data','edit'],
	methods:{
		deletePanel:function(medical){
			this.$emit('delete')
		},
		deleteMedical: function(medical){
			console.info("delete:" + JSON.stringify(medical))
			this.$emit('delete-medical', {name: medical.medical})
		}
	}
}	
</script>

<style>
.margin{
	margin-top:10px;
}
</style>