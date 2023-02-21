function getSalesReportUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales-report";
}
function getBrandUrl(){
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getSalesReport() {
	let brandField = document.querySelector("#inputBrand");
    let brand = brandField.value;
	let categoryField = document.querySelector("#inputCategory");
    let category = categoryField.value;
    let startDate = $("#sales-report-form input[name=startDate]").val();
    let endDate = $("#sales-report-form input[name=endDate]").val();
	let url = getSalesReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate + '&brand=' + brand + '&category=' + category;
	
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
	let $tbody = $('#sales-report-table').find('tbody');
	$tbody.empty();
    let tmp = 1;
	data.sort(function(a, b) { 
		return b.revenue - a.revenue;
	})
	for (let i in data) {
		let e = data[i];
		let row = '<tr>'
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
	let brandField = document.querySelector("#inputBrand");
	let brand = brandField.value;
	let categoryField = document.querySelector("#inputCategory");
	let category = categoryField.value;
	let startDate = $("#sales-report-form input[name=startDate]").val();
	let endDate = $("#sales-report-form input[name=endDate]").val();
	let url = getSalesReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate + '&brand=' + brand + '&category=' + category;
	
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			data.sort(function(a, b) { 
				return b.revenue - a.revenue;
			})
			let tsv = 'Brand	Category	Quantity	Revenue\n';  
			for(row of data){
				tsv+=Object.values(row).join('	');
				tsv+='\n';
			}  
			let hiddenElement = document.createElement('a');  
			hiddenElement.href = 'data:text/tsv;charset=utf-8,' + encodeURI(tsv);  
			hiddenElement.target = '_blank';  
			hiddenElement.download = 'sales-report-' + startDate + '-to-' + endDate + '.tsv';  
			hiddenElement.click();
				},
		error: handleAjaxError
	});
}

function getBrandsList() {
	let url = getBrandUrl()+ "/get-brands";
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
	let $select = $('#inputBrand');
	$select.empty();
	let row = "<option value='' selected>All</option>";
	$select.append(row);
	data = Array.from(new Set(data));
	for (let i in data) {
		let e = data[i];
		row = "<option value='" +e+ "'>" + e + "</option>";
		$select.append(row);
	}
}

function getCategories() {
	let url = getBrandUrl()+ "/get-categories/";
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
	let $select1 = $('#inputCategory');
	$select1.empty();
	let row = "<option value='' selected>All</option>";
	$select1.append(row);
	data = Array.from(new Set(data));
	for (let i in data) {
		let e = data[i];
		let row = "<option value='" +e+ "'>" + e + "</option>";
		$select1.append(row);
	}
}

function getDefaultDate(){
	// today
	let date = new Date();
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	let today = year + "-" + month + "-" + day;     
	document.getElementById("inputEndDate").setAttribute("max", today);
	document.getElementById("inputEndDate").value = today;

	// one month before today
	let m = date.getMonth()+1;
	date.setMonth(date.getMonth());
	if (date.getMonth() == m) date.setDate(0);
	date.setHours(0, 0, 0, 0);
	day = date.getDate();
	month = date.getMonth();
	year = date.getFullYear();
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	let monthAgo = year + "-" + month + "-" + day;      
	document.getElementById("inputStartDate").setAttribute("max", today);
	document.getElementById("inputStartDate").value = monthAgo;
}

function init() {
	getDefaultDate();
    getSalesReport();
    getBrandsList();
    getCategories();
	$('#apply-filter').click(getSalesReport);
    $('#download-tsv-sales-report').click(downloadSalesReport);
}

$(document).ready(init);
