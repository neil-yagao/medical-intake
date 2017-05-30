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
    </div>
    <code-and-timespan @condition-query="passingCondition($event)" :showTime="false"></code-and-timespan>
    <div class="row">
    	 <div class="col-md-2">
            <h4>服药人</h4>
        </div>
        <div class="col-md-3">
            <h4>修改前</h4>
        </div>
        <div class="col-md-3">
            <h4>修改后</h4>
        </div>
        <div class="col-md-2">
            <h4>修改时间</h4>
        </div>
        <div class="col-md-2">
            <h4>修改人</h4>
        </div>
    </div>
    <div style="max-height: 500px;overflow-y: scroll;">
        <div class="row" v-for="record in records" style="width:98%" >
        	<div class="col-md-2">
             {{record.changee}}   
            </div>
            <div class="col-md-3">
                <prescription :data="record.from"></prescription>
            </div>
            <div class="col-md-3">
                <prescription :data="record.to"></prescription>
            </div>
            <div class="col-md-2">
                <h4>{{record.date}}</h4>
            </div>
            <div class="col-md-2">
                <h4>{{record.changer}}</h4>
            </div>
        </div>
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
			records:[]
		}
	},
    methods:{
        passingCondition(condition){
            this.$http.get('prescription/history/' + condition.code + "/" + condition.timespan)
            .then((res) =>{
                console.info(res.body)
                this.records = res.body
            })
        }
    },
    components:{
        Prescription,
        'code-and-timespan': CodeAndTimespan
    }
}
</script>