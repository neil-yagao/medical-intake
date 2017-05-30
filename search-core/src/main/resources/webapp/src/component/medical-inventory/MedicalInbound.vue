<template>
<div>
    <div class="row">
        <div class="col-md-4">
            <h4>药物名称</h4>
        </div>
        <div class="col-md-4">
            <h4>入库时间</h4>
        </div>
        <div class="col-md-1">
        	<button class="btn btn-default" @click="downloadRecords()"><span class="glyphicon glyphicon-download-alt"></span></button>
        </div>
    </div>
    <code-and-timespan @condition-query="passingCondition($event)"></code-and-timespan>
    <div style="max-height: 500px;overflow-y: scroll">
	    <table class="table table-hover">
	    	<thead>
	    		<tr>
	    			<td>药物名称</td>
	    			<td>入库数量</td>
	    			<td>入库日期</td>
	    		</tr>
	    	</thead>
	    	<tbody>
	    		<tr v-for="record in inboundRecords">
	    			<td>
	    				{{record.medical}}
	    			</td>
	    			<td>
	    				{{record.amount}}
	    			</td>
	    			<td>
	    				{{record.date}}
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
    </div>
</div>
</template>
<script>
import CodeAndTimespan from '../general/CodeAndTimespan.vue';
import moment from 'moment'
import Vue from 'vue'
export default {
	name:'medical-inbound',
	data(){
		return {
			inboundRecords:[]
		}
	},
	methods:{
		passingCondition(condition){
			this.$http.get('medicals/inbound/' + condition.code + "/" + condition.timespan)
			.then((res)=>{
				this.inboundRecords = res.body;
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