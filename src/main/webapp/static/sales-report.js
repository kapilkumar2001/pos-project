function getSalesReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales-report";
}
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getSalesReport() {
	var brandField = document.querySelector("#inputBrand");
    var brand = brandField.value;
	var categoryField = document.querySelector("#inputCategory");
    var category = categoryField.value;
    var startDate = $("#sales-report-form input[name=startDate]").val();
    var endDate = $("#sales-report-form input[name=endDate]").val();
	var url = getSalesReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate + '&brand=' + brand + '&category=' + category;
	
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displaySalesReport(data);
		},
		error: handleAjaxError
	});
}

function displaySalesReport(data) {
	var $tbody = $('#sales-report-table').find('tbody');
	$tbody.empty();
    var tmp = 1;
	data.sort(function(a, b) { 
		return b.revenue - a.revenue;
	})
	for (var i in data) {
		var e = data[i];
		var row = '<tr>'
			+ '<td>' + tmp + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.quantity + '</td>'
            + '<td>' + e.revenue + '</td>'
			+ '</tr>';
		$tbody.append(row);
        tmp=tmp+1;
	}
}

function downloadSalesReport(){
	var brandField = document.querySelector("#inputBrand");
	var brand = brandField.value;
	var categoryField = document.querySelector("#inputCategory");
	var category = categoryField.value;
	var startDate = $("#sales-report-form input[name=startDate]").val();
	var endDate = $("#sales-report-form input[name=endDate]").val();
	var url = getSalesReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate + '&brand=' + brand + '&category=' + category;
	
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			data.sort(function(a, b) { 
				return b.revenue - a.revenue;
			})
			var tsv = 'Brand	Category	Quantity	Revenue\n';  
			for(row of data){
				tsv+=Object.values(row).join('	');
				tsv+='\n';
			}  
			var hiddenElement = document.createElement('a');  
			hiddenElement.href = 'data:text/tsv;charset=utf-8,' + encodeURI(tsv);  
			hiddenElement.target = '_blank';  
			hiddenElement.download = 'sales-report-' + startDate + '-to-' + endDate + '.tsv';  
			hiddenElement.click();
				},
		error: handleAjaxError
	});
}

function getBrandsList() {
	var url = getBrandUrl()+ "/get-brands";
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayBrandList(data);
		},
		error: handleAjaxError
	});
}

function displayBrandList(data) {
	var $select = $('#inputBrand');
	$select.empty();
	var row = "<option value='' selected>All</option>";
	$select.append(row);
	data = Array.from(new Set(data));
	for (var i in data) {
		var e = data[i];
		row = "<option value='" +e+ "'>" + e + "</option>";
		$select.append(row);
	}
}

function getCategories() {
	var url = getBrandUrl()+ "/get-categories/";
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayCategoryList(data);
		},
		error: handleAjaxError
	});
}

function displayCategoryList(data) {
	var $select1 = $('#inputCategory');
	$select1.empty();
	var row = "<option value='' selected>All</option>";
	$select1.append(row);
	data = Array.from(new Set(data));
	for (var i in data) {
		var e = data[i];
		var row = "<option value='" +e+ "'>" + e + "</option>";
		$select1.append(row);
	}
}

function getDefaultDate(){
	// today
	var date = new Date();
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	var today = year + "-" + month + "-" + day;     
	// document.getElementById("inputEndDate").required = true;  
	document.getElementById("inputEndDate").setAttribute("max", today);
	document.getElementById("inputEndDate").value = today;

	// one month before today
	var m = date.getMonth()+1;
	date.setMonth(date.getMonth());
	if (date.getMonth() == m) date.setDate(0);
	date.setHours(0, 0, 0, 0);
	var day = date.getDate();
	var month = date.getMonth();
	var year = date.getFullYear();
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	var monthAgo = year + "-" + month + "-" + day;   
	// document.getElementById("inputStartDate").required = true;    
	document.getElementById("inputStartDate").setAttribute("max", today);
	document.getElementById("inputStartDate").value = monthAgo;
}

// function enableFilterandDownload(){
// 	$('#apply-filter').click(getSalesReport);
//     $('#download-tsv-sales-report').click(downloadSalesReport);
// }

function init() {
	getDefaultDate();
    getSalesReport();
    getBrandsList();
    getCategories();
	// document.getElementById("sales-report-form").addEventListener("submit", function(e){
	// 	console.log("value= " + document.getElementById("inputStartDate").value);
	// 	if(document.getElementById("inputStartDate").value=="" || document.getElementById("inputEndDate").value==""){
	// 		console.log("this");
	// 	    return false;
	// 	}
	// 	else{
	// 		e.preventDefault();
	// 		enableFilterandDownload();
	// 	}
	// });
	$('#apply-filter').click(getSalesReport);
    $('#download-tsv-sales-report').click(downloadSalesReport);
}

$(document).ready(init);
