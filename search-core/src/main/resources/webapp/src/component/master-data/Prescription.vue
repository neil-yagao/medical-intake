<template>
	<div class="panel panel-body panel-transparent">
		<template v-for="record in records">
			<div class="row">
				<div class="col-md-6">
					<b v-if="title == 'time'">{{record.time}}</b>
					<b v-if="title == 'code'">{{code}}</b>
				</div>
			</div>
			<div class="row" v-for="medical in record.medicals">
				<div class="col-md-6 col-md-offset-6">
					<p>{{medical.medical}}&nbsp;{{medical.incOrDesc?medical.incOrDesc:'&times'}}&nbsp;{{medical.amount}}</p>
				</div>
			</div>
		</template>
	</div>
</template>
<script>
import _ from 'lodash';
import Vue from 'vue'
export default {
	name:'prescription',
	props:['data','title'],
	data(){
		return {
			'code':''
		}
	},
	computed:{
		records:function(){
			var temp = _.groupBy(this.data,'time');
			var result = [];
			for(var time in temp){
				result.push({
					'time':time,
					'medicals':temp[time]
				})
			} 
			this.code = this.data[0].code;
			result = _.sortBy(result, function(m){
				var index = Vue.qualifiedTime().indexOf(m.time)
				return index
			})
			return result;
		}
	}
}
</script>
<style type="text/css">
.panel-transparent {
	 background: rgba(255, 255, 252, 0.6)!important;
}
</style>
