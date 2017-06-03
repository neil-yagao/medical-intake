<template>
    <div>
        <nav class="navbar navbar-default navbar-fixed-top" v-if=" identity== 'police' || identity == 'medical'">
            <ul class="nav navbar-nav navbar-left">
                <li class="dropdown" :class="this.$route.path.indexOf('prescription') >= 0?'active':''">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">处方管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#/working/upload/prescription">批量上传处方</a></li>
                        <li><a href="#/working/prescription/by-number">按编号查询及修改</a></li>
                        <li><a href="#/working/prescription/records">处方修改记录</a></li>
                    </ul>
                </li>
                <li :class="this.$route.path.indexOf('by-time') >= 0?'active':''"><a href="#/working/by-time">发药辅助</a></li>
                <li class="dropdown" :class="this.$route.path.indexOf('data-edit') >= 0?'active':''" v-if=" identity== 'police'">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">服药记录<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#/working/data-edit/intake">记录查询</a></li>
                        <li><a href="#/working/data-edit/inmate">未服药记录查询</a></li>
                    </ul>
                </li>
                <li class="dropdown" :class="this.$route.path.indexOf('medical-inventory') >= 0?'active':''">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">药物管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#/working/medical-inventory/overall">存量管理</a></li>
                        <li><a href="#/working/medical-inventory/inbound">出库记录</a></li>
                        <li><a href="#/working/medical-inventory/outbound">入库记录</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li v-if=" identity== 'police'"><a href="http://localhost:8090/" target="_blank" title="注册" class="btn-lg">注册
	        <span class="glyphicon glyphicon-user" aria-hidden="true" style="color:#121cea"></span>
	        </a></li>
                <li title="登出" @click="logout()" role="button">
                    <a class="btn-lg">登出
                        <span class="glyphicon glyphicon-off" style="color:black" aria-hidden="true" ></span>
                    </a>
                </li>
            </ul>
        </nav>
        <div style="margin-top:70px" id="working-area" class="container-fluid">
            <img src="img/background.png" id="background" />
            <router-view @confirm-intake="console.info('in working-area')"></router-view>
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
    		window.localStorage.setItem('code', '')
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
            window.localStorage.setItem('code', infor.code)
            if (infor.identity =='prison' || infor.identity == 'medical') {
            	window.location.href = "#/working/detail/" + infor.code;
            } else if (infor.identity == 'police') {
                window.location.href = "#/working/prescription/by-number"
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
