<template>
<div>
    <div style="max-height: 500px;overflow-y: scroll">
        <div class="list-group" style="margin-top:10px">
            <a class="list-group-item" role="button" v-for="candidate in candidates">
                <div class="row" @click="candidate.selected = !candidate.selected">
                    <div class="col-md-4">
                        <span>{{candidate.code}}</span>
                    </div>
                    <div class="col-md-4">
                        <span>{{candidate.date}}</span>
                    </div>
                    <div class="col-md-4">
                        <span>{{candidate.time}}</span>
                    </div>
                </div>
                <div class="panel panel-info" v-if="candidate.selected" style="margin-top:10px">
                    <div class="panel-heading">服用药物详情</div>
                    <table class="table">
                    	<thead>
                    		<tr>
                    			<th>#</th>
                    			<th>药物名称</th>
                    			<th>数量</th>
                    			<th>服药时间</th>
                    		</tr>
                    	</thead>
                    	<tbody>
                    		<tr v-for="(medical,index) in candidate.medicals">
                    			<td>
                    				{{index + 1}}
                    			</td>
                    			<td>
                    				{{medical.medical}}
                    			</td>
                    			<td>
                    				{{medical.amount}}
                    			</td>
                    			<td>
                    				{{medical.time}}
                    			</td>
                    		</tr>
                    	</tbody>
                    </table>
                </div>
            </a>
        </div>
    </div>
    <div v-if="candidate.selected">
    </div>
</div>

</template>
<script type="text/javascript">
import moment from 'moment'
export default {
	name:'intake-record-query-result',
	data(){
		return {
			candidates: [],
			candidate:{}
		}
	},
	props:['condition'],
	watch:{
		condition: function(){
			console.info(this.condition)
			var timespan = this.condition.timespan.split(":");
			var start, end = ""
			if(timespan[0]){
				start = moment(timespan[0], 'YYYY-MM-DD')
			}else {
				return 
			}
			if(timespan[1]){
				end = moment(timespan[1] , 'YYYY-MM-DD')
			}
			timespan = start + ":" + end
			this.$http.get('intake/'+ this.condition.code + "/" + timespan).then((res) =>{
				console.info(res.body)
				this.candidates = []
				_.forEach(res.body, (r)=>{
					this.candidates.push({
						'code': r.code,
						'date': moment(r.timestamp).format('YYYY-MM-DD'),
						'time': moment(r.timestamp).format("HH:mm"),
						'timestamp': r.timestamp,
						'selected':false,
						'medicals': r.medicals
					})
				})
			})
		}
	}
}
</script>
