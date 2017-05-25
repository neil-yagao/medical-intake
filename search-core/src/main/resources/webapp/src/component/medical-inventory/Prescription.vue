<template>
	<div>
		<template v-for="record in records">
			<div class="row">
				<div class="col-md-2">
					<b>{{record.time}}</b>
				</div>
			</div>
			<div class="row" v-for="medical in record.medicals">
				<div class="col-md-3 col-md-offset-2">
					<p>{{medical.medical}}&times;{{medical.amount}}</p>
				</div>
			</div>
		</template>
	</div>
</template>
<script>
import _ from 'lodash';

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
			console.info(result);
			return result;
		}
	}
}
</script>
