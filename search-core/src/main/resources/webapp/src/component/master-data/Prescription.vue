<template>
	<div>
		<template v-for="record in records">
			<div class="row">
				<div class="col-md-6">
					<b>{{record.time}}</b>
				</div>
			</div>
			<div class="row" v-for="medical in record.medicals">
				<div class="col-md-6 col-md-offset-6">
					<p>{{medical.medical}}&times;{{medical.amount}}</p>
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
	props:['data'],
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
			result = _.sortBy(result, function(m){
				var index = Vue.qualifiedTime().indexOf(m.time)
				return index
			})
			return result;
		}
	}
}
</script>
