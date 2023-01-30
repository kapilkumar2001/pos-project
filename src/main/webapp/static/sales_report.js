function getSalesReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales-report";
}
function getBrandCategoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}

function getSalesReport() {

    // var brand = $("#sales-report-form input[name=brand]").val();
    // var category = $("#sales-report-form input[name=category]").val();
	var brandField = document.querySelector("#inputBrand");
    var brand = brandField.value;
	var categoryField = document.querySelector("#inputCategory");
    var category = categoryField.value;
    var startDate = $("#sales-report-form input[name=startDate]").val();
    var endDate = $("#sales-report-form input[name=endDate]").val();




	var url = getSalesReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate + '&brand=' + brand + '&category=' + category;

    console.log("url = " + url + ", b:" + brand + ", c:" + category);
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
            console.log(data);
			displaySalesReport(data);
		},
		error: handleAjaxError
	});
}

function displaySalesReport(data) {
	var $tbody = $('#sales-report-table').find('tbody');
	$tbody.empty();
    var tmp = 1;
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

function getBrandsList() {
	var url = getBrandCategoryUrl()+ "/get-brands";
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
	var url = getBrandCategoryUrl()+ "/get-categories/";
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


function init() {
	$('#apply-filter').click(getSalesReport);
}

$(document).ready(init);
$(document).ready(getSalesReport);
$(document).ready(getBrandsList);
$(document).ready(getCategories);