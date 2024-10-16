function getSalesReportUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales-report";
}

function getBrandUrl(){
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getSalesReport() {
	let brand = $("#sales-report-form select[name=brand]").val();
	let category = $("#sales-report-form select[name=category]").val();
	let startDate = $("#sales-report-form input[name=start-date]").val();
	let endDate = $("#sales-report-form input[name=end-date]").val();

	if(startDate>endDate) {
		showError("Invalid Start Date or End Date");
		return;
	}

	let url = getSalesReportUrl() + "/?startdate=" + startDate + "&enddate=" + endDate + "&brand=" + brand + "&category=" + category;

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displaySalesReport(data);
		},
		error: handleAjaxError
	});
}

function displaySalesReport(data) {
	let thead = $("#sales-report-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col'>S.No.</th> <th scope='col'>Brand</th> <th scope='col'>Category</th> <th scope='col'>Quantity</th> <th scope='col'>Revenue</th> </tr>";
	thead.append(header);

	let tbody = $("#sales-report-table").find("tbody");
	tbody.empty();
    let tmp = 1;
	data.sort(function(a, b) { 
		return b.revenue - a.revenue;
	})

	for (let i in data) {
		let e = data[i];
		let row = "<tr>"
			+ "<td>" + tmp + "</td>"
			+ "<td>" + e.brand + "</td>"
			+ "<td>" + e.category + "</td>"
			+ "<td>" + e.quantity + "</td>"
            + "<td>" + e.revenue + "</td>"
			+ "</tr>";
		tbody.append(row);
        tmp=tmp+1;
	}
}

function downloadSalesReport(){
	let brand = $("#sales-report-form select[name=brand]").val();
	let category = $("#sales-report-form select[name=category]").val();
	let startDate = $("#sales-report-form input[name=start-date]").val();
	let endDate = $("#sales-report-form input[name=end-date]").val();

	if(startDate>endDate) {
		showError("Invalid Start Date or End Date");
		return;
	}

	let url = getSalesReportUrl() + "/?startdate=" + startDate + "&enddate=" + endDate + "&brand=" + brand + "&category=" + category;
	
	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			data.sort(function(a, b) { 
				return b.revenue - a.revenue;
			})
			let tsv = "Brand	Category	Quantity	Revenue\n"; 

			for(row of data){
				tsv+=Object.values(row).join("	");
				tsv+="\n";
			}  

			let hiddenElement = document.createElement("a");  
			hiddenElement.href = "data:text/tsv;charset=utf-8," + encodeURI(tsv);  
			hiddenElement.target = "_blank";  
			hiddenElement.download = "sales-report-" + startDate + "-to-" + endDate + ".tsv";  
			hiddenElement.click();
			hiddenElement.remove();
		},
		error: handleAjaxError
	});
}

function getBrandsList() {
	let url = getBrandUrl()+ "/get-brands";

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displayBrandList(data);
		},
		error: handleAjaxError
	});
}

function displayBrandList(data) {
	let select = $("#input-brand");
	select.empty();
	let row = "<option value='' selected>All</option>";
	select.append(row);
	data = Array.from(new Set(data));
	data.sort();

	for (let i in data) {
		let e = data[i];
		row = "<option value='" +e+ "'>" + e + "</option>";
		select.append(row);
	}
}

function getCategoriesList() {
	let url = getBrandUrl()+ "/get-categories/";

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displayCategoriesList(data);
		},
		error: handleAjaxError
	});
}

function displayCategoriesList(data) {
	let select1 = $("#input-category");
	select1.empty();
	let row = "<option value='' selected>All</option>";
	select1.append(row);
	data = Array.from(new Set(data));
	data.sort();

	for (let i in data) {
		let e = data[i];
		let row = "<option value='" +e+ "'>" + e + "</option>";
		select1.append(row);
	}
}

function setDefaultDate(){
	// today
	let date = new Date();
	let day = date.getDate();
	let month = date.getMonth() + 1;
	let year = date.getFullYear();

	if (month < 10) 
		month = "0" + month;

	if (day < 10) 
		day = "0" + day;

	let today = year + "-" + month + "-" + day;     
	$("#sales-report-form input[name=end-date]").attr("max", today);
	$("#sales-report-form input[name=end-date]").val(today);

	// one month before today
	let m = date.getMonth()+1;
	date.setMonth(date.getMonth());

	if (date.getMonth() === m) 
		date.setDate(0);

	date.setHours(0, 0, 0, 0);
	day = date.getDate();
	month = date.getMonth();
	year = date.getFullYear();

	if (month < 10) 
		month = "0" + month;

	if (day < 10) 
		day = "0" + day;
		
	let monthAgo = year + "-" + month + "-" + day;      
	$("#sales-report-form input[name=start-date]").attr("max", today);
	$("#sales-report-form input[name=start-date]").val(monthAgo);
}

function init() {
	setDefaultDate();
	getBrandsList();
	getCategoriesList();
	getSalesReport();
	$("#apply-filter").click(getSalesReport);
    $("#download-tsv-sales-report").click(downloadSalesReport);
}

$(document).ready(init);
