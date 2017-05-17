<template>
	<div>
		<div class="row">
			<div class="col-md-3"> 
				<ul class="list-group">
					<li class="list-group-item" role="button" :class="this.$route.path.indexOf('medical-inventory/overall') >= 0?'list-group-item-success':''"> <a href="#/working/medical-inventory/overall" >药物存量查询</a></li>
					<li class="list-group-item" role="button" :class="this.$route.path.indexOf('medical-inventory/inbound') >= 0?'list-group-item-success':''"><a href="#/working/medical-inventory/inbound">药物入库记录</a></li>
					<li class="list-group-item" role="button" :class="this.$route.path.indexOf('medical-inventory/outbound') >= 0?'list-group-item-success':''"><a href="#/working/medical-inventory/outbound">药物出库记录</a></li>
				</ul>
			</div>
			<div class="col-md-9">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>
<scirpt>
</scirpt>