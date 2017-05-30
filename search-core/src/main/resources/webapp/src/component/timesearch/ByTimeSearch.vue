<template>
<div>
    <div class="page-header text-center">
        <h3>按照时间搜索</h3>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="input-group">
                <input type="text" class="form-control" v-model="time" placeholder="服药时间">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">服药时间
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li v-for="value in qualifiedTime" @click="selectDropDown(value)"><a>{{value}}</a></li>
                    </ul>
                </div>
            </div>
            <!-- /btn-group -->
        </div>
    </div>
    <div class="row" style="margin-top:10px ;max-height: 500px;overflow-y: scroll;">
    	<div class="col-md-3" v-for="row in mathcingMedicals">
        	<prescription :data="row" title="code"></prescription>
        </div>
    </div>
</div>

</template>
<script>
import Prescription from '../master-data/Prescription.vue'
import Vue from 'vue'
export default {
    name: 'by-time-search',
    data() {
        return {
            rows:[],
            time:'',
            qualifiedTime:Vue.qualifiedTime()
        }
    },
    methods: {
        selectDropDown: function(value){
            this.time = value
		},
    },
    mounted: function() {
        this.$http.get("inmate/medical/all").then((res) => {
            var self = this;
            _.each(res.body, function(medicalInfo) {
                self.rows.push({
                    'code': medicalInfo.code,
                    'medical': medicalInfo.medical,
                    'amount': medicalInfo.amount,
                    'time':medicalInfo.time
                });
            });

        })
        console.info(this.rows)
    },
    components: {
        Prescription
    },
    computed:{
    	mathcingMedicals :function(){
    		var resultList = _.filter(this.rows, (val) => {
                return val.time == this.time
            })
            var result = _.groupBy(resultList, 'code');
            console.info(result)
            return result;
    	}

    }
}
</script>
<style>

</style>
