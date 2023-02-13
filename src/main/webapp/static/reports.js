function getBrandsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}

function getInventoryReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory-report/";
}

function getPosDaySaleReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/posdaysales-report";
}

function getSalesReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales-report";
}

function getBrandsReport(){
	var url = getBrandsUrl();
	$.ajax({
	  url: url,
	  type: 'GET',
	  success: function(data) {  
      var csv = 'Brands, Category, ID\n';  
      for(row of data){
        csv+=Object.values(row).join(', ');
        csv+='\n';
      }  
      var hiddenElement = document.createElement('a');  
      hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);  
      hiddenElement.target = '_blank';  
      hiddenElement.download = 'brands-report.csv';  
      hiddenElement.click();  
	  },
	  error: handleAjaxError
	});
}

function getInventoryReport() {
	var url = getInventoryReportUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
      console.log(data);
      var csv = 'Brands, Category, Quantity\n';  
      for(row of data){
        csv+=Object.values(row).join(', ');
        csv+='\n';
      }  
      var hiddenElement = document.createElement('a');  
      hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);  
      hiddenElement.target = '_blank';  
      hiddenElement.download = 'inventory-report.csv';  
      hiddenElement.click();
		},
		error: handleAjaxError
	});
}

function getPosDaySaleReport() {
	var startDate = $("#posdaysales-report-form input[name=startDate]").val();
	var endDate = $("#posdaysales-report-form input[name=endDate]").val();

	var url = getPosDaySaleReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log(data);
			var csv = 'Date, Invoice Orders Count, InvoicedItemsCount, Total Revenue\n';  
			for(row of data){
				csv+=Object.values(row).join(', ');
				csv+='\n';
			}  
			var hiddenElement = document.createElement('a');  
			hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);  
			hiddenElement.target = '_blank';  
			hiddenElement.download = 'pos-day-sales-report.csv';  
			hiddenElement.click();
				},
		error: handleAjaxError
	});
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
			console.log(data);
			var csv = 'Brand, Category, Quantity, Revenue\n';  
			for(row of data){
				csv+=Object.values(row).join(', ');
				csv+='\n';
			}  
			var hiddenElement = document.createElement('a');  
			hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);  
			hiddenElement.target = '_blank';  
			hiddenElement.download = 'sales-report.csv';  
			hiddenElement.click();
				},
		error: handleAjaxError
	});
}

function getBrandsList() {
	var url = getBrandsUrl()+ "/get-brands";
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
	var url = getBrandsUrl()+ "/get-categories/";
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
  getBrandsList();
  getCategories();
  $('#download-csv-brands-report').click(getBrandsReport);
  $('#download-csv-inventory-report').click(getInventoryReport);
  $('#download-csv-posdaysales-report').click(getPosDaySaleReport);
  $('#download-csv-sales-report').click(getSalesReport);
}

$(document).ready(init);
