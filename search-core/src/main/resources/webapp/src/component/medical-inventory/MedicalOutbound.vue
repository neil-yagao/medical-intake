<template>
<div>
    <div class="row">
        <div class="col-md-4">
            <h4>药物名称</h4>
        </div>
        <div class="col-md-4">
            <h4>出库时间</h4>
        </div>
        <div class="col-md-1">
        	<button class="btn btn-default"<!--  @click="downloadRecords()" -->><span class="glyphicon glyphicon-download-alt"></span></button>
        </div>
    </div>
    <code-and-timespan @condition-query="passingCondition($event)"></code-and-timespan>
    <div style="max-height: 500px;overflow-y: scroll">
	    <table class="table table-hover">
	    	<thead>
	    		<tr>
	    			<td>药物名称</td>
	    			<td>出库数量</td>
	    			<td>出库日期</td>
	    			<td>使用人员</td>
	    		</tr>
	    	</thead>
	    	<tbody>
	    		<tr v-for="record in outboundRecords">
	    			<td>
	    				{{record.medical}}
	    			</td>
	    			<td>
	    				{{record.amount}}
	    			</td>
	    			<td>
	    				{{record.date}}
	    			</td>
	    			<td>
	    				{{record.prison}}
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
    </div>
</div>
</template>
<script>
import CodeAndTimespan from '../general/CodeAndTimespan.vue';
import moment from 'moment';
import Vue from 'vue';
import _ from "lodash";
export default {
	name:'medical-inbound',
	data(){
		return {
			outboundRecords:[]
		}
	},
	methods:{
		passingCondition(condition){
			this.$http.get('medicals/outbound/' + condition.code + "/" + condition.timespan)
			.then((res)=>{
				this.outboundRecords = [];
				var groupedRecords = _.groupBy(res.body,function(m){
					return m.medical + ":" + m.date
				})
				console.info(groupedRecords)
				/*for(var i in res.body){
					var rawRecord = 
				}*/
			})
		},
		downloadRecords(){
			if(this.inboundRecords.length == 0){
				return
			}
			var sheet = [["药物名称","入库数量","入库日期"]]
			for(var i in this.inboundRecords){
				var record = this.inboundRecords[i];
				sheet.push([record.medical, record.amount, record.date])
			}
			var wb = {};
			wb['入库记录'] = sheet;
			var now = moment().format("YYYY-MM-DD")
            Vue.exportToExcel(wb,"入库记录" + now + ".xlsx")
		}
	},
    components: {
        'code-and-timespan': CodeAndTimespan
    }
}
</script>