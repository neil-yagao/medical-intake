<template>
	<div>
		<div class="row">
			<div class="btn-group" role="group" aria-label="...">
				<button type="button" class="btn btn-default" @click="start()" :disabled="recording">
					<span class="glyphicon glyphicon-play" aria-hidden="true"></span>
				</button>
				<button type="button" class="btn btn-default" @click="stop()" :disabled="!recording">
					<span class="glyphicon glyphicon-stop" aria-hidden="true"></span>
				</button>
			</div>
		</div>
		<div class="row" style="margin-top:20px">
			<video id="recording" width="400" height="300" :src="video"></video>
		</div>
	</div>
</template>
<script>
import MediaStreamRecorder from 'msr' 
import _ from "lodash"
export default {
	name:'video-recorder',
	data() {
		return {
			mediaRecorder: '',
			mediaConstraints : {
			        audio: false,
			        video: true
		    	},
	    	stream:"",
	    	recording:false,
	    	savable:false,
	    	blobs:[]
		}
	},
	computed: {
		video: function(){
			return this.stream == ""? "": window.URL.createObjectURL(this.stream)
		}
	},
	methods: {
		captureUserMedia: function(mediaConstraints, successCallback, errorCallback) {
            navigator.mediaDevices.getUserMedia(mediaConstraints).then(successCallback).catch(errorCallback);
        },
        start: function(){
        	this.captureUserMedia(this.mediaConstraints, this.onMediaSuccess, this.onMediaError)
        	this.recording = true;
        	this.savable = false;
        },
        stop: function(){
        	this.mediaRecorder.stop();
        	this.recording = false;
        	this.savable = true;	
        	this.mediaRecorder.save()
        },
        onMediaSuccess:function (stream) {
            var video = document.getElementById('recording');
            this.stream = stream
            setTimeout(_ => {
            	video.play();
            }, 100)
            if(!window.localStorage.getItem('identity')){
            	this.stop();
            }
            this.mediaRecorder = new MediaStreamRecorder(stream);
            this.mediaRecorder.stream = stream;

            this.mediaRecorder.videoWidth = 400;
            this.mediaRecorder.videoHeight = 300;

            this.mediaRecorder.start(5*1000);

        },
        onMediaError:function(e) {
            console.error('media error', e);
        }
	},
	mounted:function(){
		if(!this.recording){
			this.start()
		}
	}
}

</script>