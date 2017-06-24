<template>
	<div>
		<video :src="src" controls autoplay id="record"></video>	
	</div>
</template>
<script>
export default {
	data (){
		return {
			src:''
		}
	},
	mounted:function(){
		var time = this.$route.params.time;
		console.info('time:' + time)
		this.$http.get('video-record/' + time).then((res) =>{
			this.src = 'video/' + res.body.video;
			var vid = document.getElementById("record"); 
			vid.play();
		})
	}

}
</script>