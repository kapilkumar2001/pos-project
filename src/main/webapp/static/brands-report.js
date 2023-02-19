function getBrandsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getBrands(){
	var url = getBrandsUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrands(data);  
	   },
	   error: handleAjaxError
	});
}


function displayBrands(data){
	var $tbody = $('#brand-report-table').find('tbody');
	$tbody.empty();
	let serialNo = 1;
	data = data.reverse();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + serialNo + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
		serialNo+=1;
	}
}

function getBrandsReport(){
	var url = getBrandsUrl();
	$.ajax({
	  url: url,
	  type: 'GET',
	  success: function(data) { 
		data = data.reverse();
        let headers = 'Brand	Category\n'; 
        let tsv = '';
        tsv += headers
		for(row of data){
			tsv+=(row.brand + '	' + row.category);
			tsv+='\n';
		  }  
        var hiddenElement = document.createElement('a');  
        hiddenElement.href = 'data:text/tsv;charset=utf-8,' + encodeURI(tsv);  
        hiddenElement.target = '_blank';  
        hiddenElement.download = 'brands-report.tsv';  
        hiddenElement.click();  
	  },
	  error: handleAjaxError
	});
}

function init(){
    getBrands()
    $('#download-tsv-brands-report').click(getBrandsReport);
}

$(document).ready(init);