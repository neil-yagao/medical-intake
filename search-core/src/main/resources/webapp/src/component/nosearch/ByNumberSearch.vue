<template>
<div>
    <div class="page-header text-center">
	  	<h3>按照编号搜索</h3>
	</div>
    <div class="row">
		<div class="col-md-8 col-md-offset-2">
		    <div class="input-group">
		      <input type="text" class="form-control" v-model="id" placeholder="编号">
		      <span class="input-group-btn">
		        <a class="btn btn-default" type="button" href="#/working/add"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></a>
		      </span>
		    </div>
	  	</div>
  	</div>
  	<div class="row">
  		<prison-panel :inmates="{'code': id}"></prison-panel>
  	</div>
</div>
</template>
<script>
import InmatePanel from '../profile/PrisonPanel.vue'
export default {
    name: 'by-number-search',
    data() {
        return {
            id: '',
            shown : false,
            currentMedical:{}
        }
    },
    methods: {
    	
    },
    components: {
    	'prison-panel': InmatePanel
    }
}
</script>
<style>

</style>
