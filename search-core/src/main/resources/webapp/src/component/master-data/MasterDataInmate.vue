<template>
<div>
	<input type="text" class="form-control" v-model="code" placeholder="人员编号">
	<table class="table table-hover">
		<thead>
			<tr>
				<td><input type="checkbox" v-model="checkAll" @change="clickedCheckAll()"></td><td>编号</td><td>日期</td><td>时间</td>
			</tr>
		</thead>
		<tbody>
			<tr v-for="matching in matchingList">
				<td><input type="checkbox" v-model="matching.checked" @change="checkRecord(matching)"></td>
				<td>{{matching.code}}</td>
				<td>{{matching.date}}</td>
				<td>{{matching.need}}</td>
			</tr> 
		</tbody>
	</table>
	<div class="row">
		<div class="col-md-2 col-md-offset-10">
			<button class="btn btn-success" @click="saveCheckedRecord()">保存</button>
		</div>
	</div>
	</div>
</template>
<script type="text/javascript">
import moment from 'moment'
export default {
    name: "inmate-records",
    data() {
        return {
            code: "",
            records: [],
            checkAll: false,
            tempCheck: []
        }
    },
    methods: {
        clickedCheckAll: function() {
            if (this.checkAll) {
                this.records = _.filter(this.records, (r) => {
                    if (!r.checked) {
                        this.tempCheck.push(this.records.indexOf(r))
                    }
                    r.checked = this.checkAll;
                    return true;
                })
            }else{
            	this.records = _.filter(this.records, (r) => {
                    if(this.tempCheck.indexOf(this.records.indexOf(r)) >= 0){
                    	r.checked = this.checkAll;
                    }
                    return true;
                })
            }

        },
        checkRecord: function(m) {
            console.info(m)
        },
        saveCheckedRecord:function(){
        	this.$http.post('intake/miss', JSON.stringify(this.records))
        }
    },
    mounted: function() {
        this.$http.get('intake/miss').then((res) => {
            this.records = _.sortBy(res.body, 'checked');
        })
    },
    computed: {
        matchingList: function() {
            return _.filter(this.records, (r) => {
                return r.code.indexOf(this.code) >= 0
            }).slice(0, 10)
        }
    }
}
</script>