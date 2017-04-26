<template>
    <div class="text-center">
	  	<h3>上传需要更新的文件</h3>
        <div class="row">
            <div class="col-md-9">
                <input id="fileupload" name="myfile" type="file" @change="onFileChange"/>
            </div>
            <div class="col-md-2">
                <button class="btn btn-default"><span class="glyphicon glyphicon-open-file"></span></button>
            </div>
        </div>
        <div class="row" style="margin-top:10px">
            <div class="drop_zone" @drop="onFileChange" @dragover="dragging" style="min-height:200px;text-align:center">或拖拽文件至此处</div>
        </div>
    </div>
</template>
<script>
import X from "xlsx"
export default {
    name: 'upload-file',
    data() {
        return {}
    },
    methods: {
        onFileChange(e) {
            e.stopPropagation();
            e.preventDefault();
            var files = e.target.files || e.dataTransfer.files;
            if (!files.length)
                return;
            var reader = new FileReader()
            var self = this;
            reader.onload = function(e) {
                var content = e.target.result;
                var wb = X.read(content, {type: 'binary'});
                var data = self.process_wb(wb);
                self.handleUpdate(data)
            }
            reader.readAsBinaryString(files[0])
        },
        dragging(evt) {
            evt.stopPropagation();
            evt.preventDefault();
            evt.dataTransfer.dropEffect = 'copy';
        },
        toJson(workbook) {
			var result = {};
			workbook.SheetNames.forEach(function(sheetName) {
				var roa = X.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
				if(roa.length > 0){
					result[sheetName] = roa;
				}
			});
			return result;
		},
		process_wb(wb) {
			var output = this.toJson(wb)
			return output		
		},
		handleUpdate(data){
			console.info(data.medicals)
			if(data.medicals){
				var mappedData = _.map(data.medicals, function(med){
					return {
						"name":med["名称"],
						"num": med["数量"]
					}
				})
				this.$http.post('medicals', JSON.stringify(mappedData))
				.then((response) => {
					console.info(response.body)
				})
			}
			if(data.inmates){
				var mappedData = _.map(data.inmates, function(info){
					return {
						"code":info["编号"],
						"time": info["时间"],
						"medical":info["药物"],
						"amount":info["数量"]
					}
				})
				this.$http.post('http://localhost:8080/inmate/medical', JSON.stringify(mappedData))
				.then((response) => {
					console.info(response.body)
				})
			}
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
    padding-top: 90px;
    text-align: center;
    font: 40pt bold 'Vollkorn';
    color: #bbb;
}
</style>