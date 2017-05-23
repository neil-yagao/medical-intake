<template>
<div>
    <div class="row">
        <div class="col-md-4">
            <h4>人员编号</h4>
        </div>
        <div class="col-md-4">
            <h4>服药日期</h4>
        </div>
        <div class="col-md-1">
        	<button class="btn btn-default" @click="startDownload()" :disabled="downloadFlag"><span class="glyphicon glyphicon-download-alt"></span></button>
        </div>
    </div>
    <code-and-timespan @condition-query="passingCondition($event)"></code-and-timespan>
    <result-panel :condition="queryCondition" :flag="downloadFlag"></result-panel>
</div>
</template>
<script>
import CodeAndTimespan from '../general/CodeAndTimespan.vue';
import ResultPanel from './IntakeResultPanel.vue';
import _ from "lodash";
import Vue from 'vue';
export default {
    name: 'intake-records',
    data() {
        return {
            queryCondition:{
            	'timespan':':'
            },
            downloadFlag:false
        }
    },
    methods: {
        passingCondition: function(event) {
            this.queryCondition = event;
        },
        selectDropDown(value) {
            this.time = value
        },
        startDownload(){
        	this.downloadFlag = true;
        	setTimeout(()=>{
        		this.downloadFlag = false;
        	}, 5000)
        }
    },
    computed: {
        endLimit:function(){
        	return [{
        		type: 'fromto',
        		from: this.startDate.time
        	}]
        }
    },
    components: {
        'result-panel': ResultPanel,
        'code-and-timespan': CodeAndTimespan
    }
}
</script>