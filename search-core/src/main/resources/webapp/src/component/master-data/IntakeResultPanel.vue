<template>
<div>
    <div style="max-height: 500px;overflow-y: scroll">
        <div class="list-group" style="margin-top:10px">
            <a class="list-group-item" role="button" v-for="candidate in candidates" style="background:transparent">
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
                    <table class="table table-hover">
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
</div>

</template>
<script type="text/javascript">
import Vue from 'vue';
import moment from 'moment';
export default {
    name: 'intake-record-query-result',
    data() {
        return {
            candidates: [],
            candidate: {}
        }
    },
    props: ['condition', 'flag'],
    watch: {
        condition: function() {
            var timespan = this.condition.timespan;
            this.$http.get('intake/' + this.condition.code + "/" + timespan + "/" + this.condition.time).then((res) => {
                this.candidates = []
                _.forEach(res.body, (r) => {
                    this.candidates.push({
                        'code': r.code,
                        'date': moment(r.timestamp).format('YYYY-MM-DD'),
                        'time': moment(r.timestamp).format("HH:mm"),
                        'timestamp': r.timestamp,
                        'selected': false,
                        'medicals': r.medicals
                    })
                })
            })
        },
        flag: function() {
            if (this.flag) {
            	if(this.candidates.length == 0){
            		return 
            	}
                var groupedCandidate = _.groupBy(this.candidates, 'code')
                var wb = {}
                var heading = ["编号", "日期", "服药时间", "药物名称", "数量"]
                for (var code in groupedCandidate) {
                    var records = groupedCandidate[code];
                    var sheet = [heading]

                    for (var index in records) {
                        var eachRecord = records[index]
                        if (!eachRecord.medicals) {
                            continue;
                        }
                        for (var m in eachRecord.medicals) {
                            var intakeRecord = eachRecord.medicals[m]
                            sheet.push([code, eachRecord.date, eachRecord.time, intakeRecord.medical, intakeRecord.amount])
                        }
                    }

                    wb[code] = sheet;
                }
                var now = moment().format("YYYY-MM-DD")
                Vue.exportToExcel(wb,"服药记录" + now + ".xlsx")
            }
        }
    }
}
</script>
