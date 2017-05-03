<template>
<div>
    <ul class="nav nav-tabs" v-if=" identity== 'police'">
        <li role="presentation" :class="this.$route.path.indexOf('by-number') >= 0?'active':''" title="编号搜索">
            <a href="#/working/by-number" class="btn-lg">
                <span class="glyphicon glyphicon-barcode" aria-hidden="true"></span>
            </a>
        </li>
        <li role="presentation" :class="this.$route.path.indexOf('by-time') >= 0?'active':''" title="时段搜索">
            <a href="#/working/by-time" class="btn-lg">
                <span class="glyphicon glyphicon-time" aria-hidden="true"></span>
            </a>
        </li>
        <li role="presentation" :class="this.$route.path.indexOf('data-edit') >= 0?'active':''" title="基本配置">
            <a href="#/working/data-edit" class="btn-lg">
                <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
            </a>
        </li>
        <li role="presentation" title="注册" style="float:right">
            <a href="http://localhost:8090/"  target="_blank" class="btn-lg">
                <span class="glyphicon glyphicon-user" aria-hidden="true" style="color:#121cea"></span>
            </a>
        </li>
        <li role="presentation" style="float:right" title="登出">
            <a class="btn-lg">
                <span class="glyphicon glyphicon-off" style="color:black" aria-hidden="true" @click="logout()"></span>
            </a>
        </li>
    </ul>
    <div style="margin-top:10px" id="working-area" class="container-fluid">
    	<img src="img/background.png" id="background"/>
        <router-view></router-view>
    </div>
    <footer class="text-center">
        <span><b> 当前时间：{{currentTime}}</b></span>
    </footer>
</div>

</template>
<script>
export default {
    name: 'working-page',
    data() {
        return {
            key: '',
            currentTime: (new Date()).toLocaleString(),
            identity:window.localStorage.getItem('identity'),
            websocket:'',
            debounceToLogin:'',
            vedioRecording:false
        }
    },
    methods: {
    	logout(){
    		window.location.href = "#/";
    		window.localStorage.setItem('identity', '');
    	}
    },
    mounted: function() {
        var self = this;
        console.info('loading working page')
        setInterval(function() {
            self.currentTime = (new Date()).toLocaleString()
        }, 1000);
        this.websocket = new WebSocket("ws://localhost:8090/identity");

        this.websocket.onopen = function() {
            console.info("connected identity")
        }

        this.websocket.onmessage = (msg) => {
            var infor = JSON.parse(msg.data)
            console.info(infor)
            this.identity = infor.identity;
            window.localStorage.setItem('identity', infor.identity);
            if (infor.identity == 'prison') {
                window.location.href = "#/working/detail/" + infor.code;
            } else if (infor.identity == 'police') {
                window.location.href = "#/working/by-number"
            }
        }
    }
}
</script>
<style>
.btn-lg {
    font-size:20px;
}
.footer {
  position: relative;
  margin-top: -150px; /* negative value of footer height */
  height: 150px;
  clear:both;
  padding-top:20px;
} 
#working-area {
  min-height: 600px;
}

#background {
	position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    opacity: 0.4;
    z-index: -1
}
.nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover {
	color: rgba(234, 6, 6, 0.8);
    cursor: default;
    background-color:rgba(96, 102, 229, 0.2);
    border-bottom-color: transparent;
    margin-right: 0px
}
.nav-tabs>li>a{
	margin-right: 0px;
    line-height: 1.42857143;
    border: 1px solid transparent;
    border-radius: 4px 4px 0 0;
}

</style>
