<template>
<div>
	<div class="input-group">
        <div :class=" medicalsShow?'':'has-error'" class="form-group "><label class="form-control">{{medicalsShow}} </label></div>
        <span class="input-group-btn">
            <button class="btn btn-default" type="button" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-plus text-green"></span></button>
        </span>
    </div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">请选择药物</h4>
				</div>
				<div class="modal-body">
					<input type="text" class="form-control" v-model="medical" placeholder="药物名称">
					<ul class="list-group margint">
						<li class="list-group-item" :class="list-group-item-info" v-for="matching in matchingList" @click="selectMedical(matching)">{{matching.name}}</li>
					</ul>
					<h4>已选药物：</h4><span class="label label-info small-margin-left" v-for="s in selected" @click="removeSelecteMedical(s)">{{s.name}}</span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal" @click="finishSelection()">确定</button>
				</div>
			</div>
		</div>
	</div>
</div>
</template>
<script type="text/javascript">
import _ from "lodash"
export default {
	name:'medical-modal',
	data(){
		return {
			medical:"",
			allList:[],
			selected:[]
		}

	},
	methods:{
		selectMedical: function(medical){
			if(_.indexOf(this.selected,medical) < 0){
				this.selected.push(medical)
			}
		},
		removeSelecteMedical: function(medical){
			this.selected = _.filter(this.selected, function(m){
				return m.name != medical.name
			})
		},
		finishSelection: function(){
			this.$emit('selected', this.medicals)
		}
	},
	computed:{
		matchingList:function(){
			var matchingList = _.filter(this.allList, (me)=>{
				if(!this.medical) return true;
				return me.name.indexOf(this.medical) >=0
			})
			return _.orderBy(matchingList,'num','asc').slice(0,5);
		},
		medicals: function(){
			var m = ""
			for(var i in this.selected){
				m = m + "," + this.selected[i].name
			}
			return _.trimStart(m, ',');
		},
		medicalsShow:function(){
			if(this.medicals.length > 20){
				return this.medicals.slice(0,20) + '...'
			}
			return this.medicals;
		}
	},
	mounted:function(){
		this.$http.get("medicals").then((response) =>{
			this.allList = response.body; 
		})

	}
}
</script>
<style>
.small-margin-left {
	margin-left: 2px
}
</style>