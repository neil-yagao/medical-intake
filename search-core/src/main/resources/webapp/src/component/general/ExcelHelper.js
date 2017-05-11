import XLSX from "xlsx"
var FileSaver = require('file-saver');

function Workbook() {
    if (!(this instanceof Workbook)) return new Workbook();
    this.SheetNames = [];
    this.Sheets = {};
}

function s2ab(s) {
    var buf = new ArrayBuffer(s.length);
    var view = new Uint8Array(buf);
    for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}

function install(Vue, options) {
    Vue.exportToExcel = function(sheets, filename) {
        var wb = new Workbook();
        var wopts = {
            bookType: 'xlsx',
            bookSST: true,
            type: 'binary'
        };
        for (var sheetName in sheets) {
            wb.SheetNames.push(sheetName);
            var content = sheets[sheetName];
            if (content.length < 2) {
                continue;
            }
            var ws = XLSX.utils.aoa_to_sheet(content);
            wb.Sheets[sheetName] = ws;
        }
        console.info(wb)

        var wbout = XLSX.write(wb, wopts);

        FileSaver.saveAs(new Blob([s2ab(wbout)], {
            type: "application/octet-stream"
        }), filename);
    }
}

export default install;