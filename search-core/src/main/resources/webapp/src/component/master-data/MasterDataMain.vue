<template>
	<div>
		<div class="row">
			<div class="col-md-3"> 
				<ul class="list-group">
					<li class="list-group-item" role="button" :class="this.$route.path.indexOf('data-edit/medical') >= 0?'list-group-item-success':''"> <a href="#/working/data-edit/medical" >药物管理</a></li>
					<li class="list-group-item" role="button" :class="this.$route.path.indexOf('data-edit/inmate') >= 0?'list-group-item-success':''"> <a href="#/working/data-edit/inmate" >服刑人员未服药记录</a></li>
					<li class="list-group-item" role="button" :class="this.$route.path.indexOf('data-edit/intake') >= 0?'list-group-item-success':''"><a href="#/working/data-edit/intake">服刑人员服药记录</a></li>
					<hr>
					<li class="list-group-item list-group-item-warning" role="button" @click="downloadMedicalInfo()">服刑人员服药情况下载</li>
				</ul>
			</div>
			<div class="col-md-9">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>
<script>
var FileSaver = require('file-saver');
import XLSX from "xlsx"
function Workbook() {
	if(!(this instanceof Workbook)) return new Workbook();
	this.SheetNames = [];
	this.Sheets = {};
}
export default {
    name: 'master-data',
    data() {
        return {}
    },
    methods: {
        downloadMedicalInfo() {
        	this.$http.get('inmate/medical/all')
        	.then(function(res){
        		var dataSource = [
        			["编码","时间","药物","数量"]
        		];
                _.each(res.body, function(medicalInfo) {
                    dataSource.push([medicalInfo.code, medicalInfo.time, medicalInfo.medical, medicalInfo.amount])
                });
        		var wb = new Workbook();
	        	wb.SheetNames.push("inmate");
	        	console.info(dataSource);
	        	var ws = XLSX.utils.aoa_to_sheet(dataSource);
	        	wb.Sheets["inmate"] = ws;
	        	var wopts = { bookType:'xlsx', bookSST:true, type:'binary' };

				var wbout = XLSX.write(wb,wopts);

				function s2ab(s) {
				  var buf = new ArrayBuffer(s.length);
				  var view = new Uint8Array(buf);
				  for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
				  return buf;
				}

				FileSaver.saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}), "服药情况.xlsx");
        	})
        	
        }
    }
}
</script>
<style>
.drop_zone {
    border: 2px dashed #bbb;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
    padding: 25px;
    text-align: center;
    font: 20pt bold 'Vollkorn';
    color: #bbb;
}
</style>
