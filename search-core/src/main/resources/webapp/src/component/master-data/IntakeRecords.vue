<template>
<div>
    <div class="row">
        <div class="col-md-4">
            <h4>人员编号</h4>
        </div>
        <div class="col-md-4" v-show="this.code">
            <h4>服药日期</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <input type="text" v-model="code" class="form-control">
        </div>
        <div class="col-md-2" >
            <date-picker :date="startDate" :option="option" ></date-picker>
        </div>
        <div class="col-md-2" >
            <date-picker :date="endDate" :option="option" :limit="endLimit"></date-picker>
        </div>
        <div class="col-md-1" v-show="this.startDate.time || this.code">
            <button class="btn btn-default" @click="passingCondition()"><span class="glyphicon glyphicon-search"></span></button>
        </div>
        <div class="col-md-1">
        	<button class="btn btn-default" @click="startDownload()"><span class="glyphicon glyphicon-download-alt"></span></button>
        </div>
    </div>
    <result-panel :condition="queryCondition" :flag="downloadFlag"></result-panel>
</div>
</template>
<script>
import myDatepicker from 'vue-datepicker'
import ResultPanel from './IntakeResultPanel.vue'
import _ from "lodash";
import moment from "moment"
export default {
    name: 'intake-records',
    data() {
        return {
            code: '',
            startDate: {
            	time:moment().format('YYYY-MM-DD')
            },
            endDate:{
            	time:''
            },
            option: {
                type: 'day',
                week: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
                month: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                format: 'YYYY-MM-DD',
                inputStyle: {
                    'display': 'inline-block',
                    height: '34px',
                    'width': '100%',
                    padding: '6px 12px',
                    'font-size': '14px',
                    'line-height': '1.42857143',
                    'border': '1px solid #ccc',
                    'border-radius': '4px',
                    'box-shadow': '0 1px 3px 0 rgba(0, 0, 0, 0.2)',
                    'border-radius': '2px',
                    'color': '#5F5F5F'
                },
                buttons: {
                    ok: '确定',
                    cancel: '取消'
                },
                overlayOpacity: 0.5, // 0.5 as default
                dismissible: true // as true as default
            },
            queryCondition:{
            	'timespan':':'
            },
            downloadFlag:false
        }
    },
    methods: {
        passingCondition: function() {
            /*if (hour <= 9) {
                return "早餐前 && 早餐后"
            } else if (hour >= 10 && hour <= 14) {
                return "午餐前 && 午餐后"
            } else if (hour >= 16 && hour <= 19) {
                return "晚餐前 && 晚餐后"
            }
            return "临睡前"*/
            this.queryCondition = {
            	'code': this.code?this.code:'all',
            	'timespan':this.startDate.time + ":" + this.endDate.time
            }
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
        qualifiedTime: function() {
            return window.qualifiedTime;
        },
        endLimit:function(){
        	return [{
        		type: 'fromto',
        		from: this.startDate.time
        	}]
        }
    },
    components: {
        'result-panel': ResultPanel,
        'date-picker': myDatepicker
    }
}
</script>