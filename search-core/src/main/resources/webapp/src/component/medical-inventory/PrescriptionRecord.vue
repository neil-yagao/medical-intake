<template>
<div>
    <div class="row">
        <div class="col-md-4">
            <h4>修改人</h4>
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
    <code-and-timespan @condition-query="passingCondition($event)"></code-and-timespan>
    <div class="row">
    	<div class="col-md-2">
            <h4>修改人</h4>
        </div>
        <div class="col-md-4">
            <h4>修改前</h4>
        </div>
        <div class="col-md-4">
            <h4>修改后</h4>
        </div>
        <div class="col-md-2">
            <h4>修改时间</h4>
        </div>
    </div>
    <div class="row" v-for="record in records">
    	<div class="col-md-2">
            <h4>{{record.changer}}</h4>
        </div>
        <div class="col-md-4">
            <prescription :data="record.from">
        </div>
        <div class="col-md-4">
            <prescription :data="record.to">
        </div>
        <div class="col-md-2">
            <h4>{{record.date}}</h4>
        </div>
    </div>
</div>
</template>
<script>
export default {
	name:'prescription-records',
	data(){
		return {
			records:[]
		}
	},
	mounted:function(){
		this.$http.get('')
	}
}
</script>