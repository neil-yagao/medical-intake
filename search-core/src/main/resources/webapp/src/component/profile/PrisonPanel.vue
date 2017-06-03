<template>
	<div style="margin-top:10px">
		<div class="row"  style="max-height: 350px;overflow-y: scroll;">
			<div class="col-md-3" v-for="inmate in matchingInmates">
			    <a :href="inmate.toURL" class="thumbnail thumbnail-transparent" role="button" style="text-align:center">
			    	<img :src="inmate.img" />
			    	<div class="caption">
			    		<span><b>{{inmate.code}}</b></span>
			    	</div>
			    </a>
			  </div>
		</div>
	</div>
</template>
<script charset="utf-8">

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
        this.$http.get("inmates/all").then((res) => {
            var self = this;
            _.each(res.body, function(identity) {
                self.rows.push({
                    'code': identity.code,
                    'toURL': "#/working/detail/" + identity.code,
                    'img': identity.headPic
                });
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
            return _.sortBy(resultList, 'code');
        }
    }
}
</script>
<style>
.thumbnail-transparent {
	 background: rgba(255, 255, 252, 0.6)!important;
}

</style>