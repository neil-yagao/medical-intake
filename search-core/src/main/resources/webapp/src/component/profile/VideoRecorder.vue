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
			<video id="recording" width="400" height="300" autoplay></video>
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
	methods: {
		captureUserMedia: function(mediaConstraints, successCallback, errorCallback) {
            navigator.mediaDevices.getUserMedia(mediaConstraints).then(successCallback).catch(errorCallback);
        },
        start: function(){
        	this.captureUserMedia(this.mediaConstraints, this.onMediaSuccess, this.onMediaError)
        	this.recording = true;
        	this.savable = false;
        	console.info("starting")
        },
        stop: function() {
            this.mediaRecorder.stop();
            this.recording = false;
            this.savable = true;
            this.saveRecord()
        },
        saveRecord(){
        	var blob = new Blob(this.blobs, {
                type: 'video/webm'
            });
            var url = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
			document.body.appendChild(a);
            a.download = 'recording.webm';
            a.click();
            setTimeout(function() {
			    document.body.removeChild(a);
			    window.URL.revokeObjectURL(url);
			  }, 100);
            this.blobs = [];
        },
        onMediaSuccess:function (stream) {
            var video = document.getElementById('recording');
            video.src = URL.createObjectURL(stream);
            setTimeout(_ => {
            	video.play();
            }, 100)
            this.blobs = []
            if(!window.localStorage.getItem('identity')){
            	this.stop();
            }
            this.mediaRecorder = new MediaRecorder(stream,{mimeType: 'video/webm'})
            this.mediaRecorder.ondataavailable = (event) =>{
            	if (event.data && event.data.size > 0) {
			    	this.blobs.push(event.data);
			    }
            }
            this.mediaRecorder.videoWidth = 400;
            this.mediaRecorder.videoHeight = 300;
            this.mediaRecorder.start(1000);

        },
        onMediaError:function(e) {
            console.error('media error', e);
        }
	},
	mounted:function(){
		if(!this.recording){
			this.start()
		}
		var interval = setInterval(()=>{
			var lastUsed = window.localStorage.getItem('last-usage');
			console.info(lastUsed);
			if(lastUsed){
				var now = Math.floor(Date.now() / 1000);
				var duration = now - lastUsed;
				if(this.recording){
					if(duration >= 600){
						//automatic download
						this.stop();
						window.localStorage.setItem('last-usage','')
						clearInterval(interval)
					}
				}else{
					this.start()
				}
			}
		}, 1000)
	}
}

</script>