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
    <div class="row">
        <prison-panel :inmates="{'time': time}"></prison-panel>
    </div>
</div>

</template>
<script>
import InmatePanel from '../profile/PrisonPanel.vue'
export default {
    name: 'by-time-search',
    data() {
        return {
        	qualifiedTime:["早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","临睡前"],
            key: '',
            time:"",
            matchingPrison:[],
            totals:[]
        }
    },
    methods: {
        selectDropDown: function(value){
			this.time = value;
		},
    },
    components: {
        'prison-panel': InmatePanel
    }
}
</script>
<style>

</style>
