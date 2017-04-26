<template>
	<div style="margin-top:10px">
		<div class="row"  style="max-height: 350px;overflow-y: scroll;">
			<div class="col-md-3" v-for="inmate in matchingInmates">
			    <a :href="inmate.toURL" class="thumbnail thumbnail-transparent" role="button" style="text-align:center">
			    	<img :src="inmate.img" />
			    	<div class="caption" v-if="inmate.code != 'add'">
			    		<span><b>{{inmate.code}}</b></span>
			    	</div>
			    </a>
			  </div>
		</div>
	</div>
</template>
<script>

import _ from 'lodash'
export default {
	name: 'inamte-panel',
	data(){
		return {
			rows:[

			]
		}
	},
	method:{
		
	},
    mounted: function() {
        this.$http.get("inmate/medical/all").then((res) => {
            var self = this;

            _.each(res.body, function(medicalInfo) {
                self.rows.push({
                    'code': medicalInfo.code,
                    'toURL': "#/working/detail/" + medicalInfo.code,
                    'img': 'img/head/' + medicalInfo.code + ".png",
                    'style': '',
                    'time':medicalInfo.time
                })
            });
        })
    },
    props:['inmates'],
    computed:{
        matchingInmates: function() {
        	var resultList = this.rows
            if (this.inmates.code) {
                resultList =  _.filter(this.rows, (val) => {
                    return val.code.indexOf(this.inmates.code) > 0
                })
            }
            if (this.inmates.time) {
            	console.info(this.rows)
                resultList = _.filter(this.rows, (val) => {
                    return val.time == this.inmates.time
                })
            }
            console.info(resultList)
            return _.uniqBy(resultList, 'code')
        }
    }
}
</script>
<style>
.thumbnail-transparent {
	 background: none;
}

.panel-transparent .thumbnail{
	background: rgba(122, 130, 136, 0.2)!important;
}
</style>