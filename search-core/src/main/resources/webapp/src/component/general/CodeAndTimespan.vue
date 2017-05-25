<template>
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
        <div class="col-md-3" v-if="showTime">
            <div class="input-group">
                <input type="text" class="form-control" v-model="time" placeholder="服药时间">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">服药时间
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li v-for="value in qualifiedTime" @click="time = value"><a>{{value}}</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-md-1" v-show="this.startDate.time || this.code">
            <button class="btn btn-default" @click="passingCondition()"><span class="glyphicon glyphicon-search"></span></button>
        </div>
        
    </div>
</template>
<script>
import myDatepicker from 'vue-datepicker';
import moment from "moment"
import Vue from 'vue'
export default {
    name: 'code-and-timespan',
    data() {
        return {
            code: '',
            startDate: {
                time: moment().format('YYYY-MM-DD')
            },
            endDate: {
                time: ''
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
            qualifiedTime: Vue.qualifiedTime(),
            time: ''
        }
    },
    methods: {
        passingCondition() {

            var start, end = ""
            if (this.startDate.time) {
                start = moment(this.startDate.time, 'YYYY-MM-DD')
            } else {
                return
            }
            if (this.endDate.time) {
                end = moment(this.endDate.time, 'YYYY-MM-DD')
            }
            var timespan = start + ":" + end
            this.$emit('condition-query', {
                'code': this.code ? this.code : 'all',
                'timespan': timespan,
                'time':this.time? this.time: "all"
            });
        }
    },
    props:{
    	'showTime':{
    		default:true
    	}
    },
    components: {
        'date-picker': myDatepicker
    }
}
</script>