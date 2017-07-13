<template>
<div>
    <div class="row">
        <div class="col-md-4">
            <h4>服药人</h4>
        </div>
        <div class="col-md-4">
            <h4>修改日期</h4>
        </div>
        <div class="col-md-1">
            <button class="btn btn-default" @click="startDownload()" :disabled="downloadFlag">
                <span class="glyphicon glyphicon-download-alt"></span>
            </button>
        </div>
        <div class="col-md-2 col-offset-md1" v-if="identity == 'docter'">
        	<button class="btn btn-warning" @click="confirmPrescriptionChanges()" :disabled="downloadFlag">
                确认显示的修改
            </button>
        </div>
    </div>
    <code-and-timespan @condition-query="passingCondition($event)" :showTime="false"></code-and-timespan>
    <div style="max-height: 500px;overflow-y: scroll;">
    <table class="table table-hover table-condensed">
    	<thead>
    		<tr>
    			<th class="col-md-2">
    				<h4>服药人</h4>
    			</th>
    			<th class="col-md-4">
    				<h4>修改内容</h4>
    			</th>
    			<th class="col-md-2">
    				<h4>修改时间</h4>
    			</th>
    			<th class="col-md-2">
    				<h4>修改人</h4>
    			</th>
    			<th class="col-md-2">
    				<h4>确认人</h4>
    			</th>
    		</tr>
    	</thead>
    	<tbody>
    		<tr v-for="record in records" :class="record.confirmer?'':'danger'">
    			<td>
             {{record.changee}}   
            </td>
            <td>
                <prescription :data="record.changes" title="time"></prescription>
            </td>
            <td>
               {{record.date}}
            </td>
            <td>
                {{record.changer}}
            </td>
            <td>
            	<h4>{{record.confirmer}}
            		<span class="label label-info">
            		@{{new Date(record.confirm_timestamp).toLocaleString()}}</span>
            	</h4>
            </td>
    		</tr>
    	</tbody>
    </table>
    </div>
</div>
</template>
<script>
import CodeAndTimespan from '../general/CodeAndTimespan.vue';
import Prescription from './Prescription.vue'
export default {
	name:'prescription-records',
	data(){
		return {
			records:[],
			identity:window.localStorage.getItem("identity"),
			currentCondition:{}
		}
	},
    methods:{
        passingCondition(condition){
        	this.currentCondition = condition;
            this.$http.get('prescription/history/' + condition.code + "/" + condition.timespan)
            .then((res) =>{
                console.info(res.body)
                this.records = res.body
            })
        },
        confirmPrescriptionChanges(){
			this.$http.post('prescription/confirm/' + window.localStorage.getItem('code'), this.records)
			.then((res) =>{
				this.passingCondition(this.currentCondition);
			})
        }
    },
    mounted:function(){
    	this.identity = window.localStorage.getItem("identity")
    },
    components:{
        Prescription,
        'code-and-timespan': CodeAndTimespan
    }
}
</script>