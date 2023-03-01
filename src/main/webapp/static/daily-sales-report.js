function getPosDaySaleReportUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/posdaysales-report/";
}

function displayPosDaySale(data) {
	let thead = $("#posdaysales-report-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col'>Date</th> <th scope='col'>Total Invoiced Orders</th> <th scope='col'>Total Invoiced Items</th> <th scope='col'>Total Revenue</th> </tr>";
	thead.append(header);

	let tbody = $("#posdaysales-report-table").find("tbody");
	tbody.empty();

	for (let i in data) {
		let e = data[i];
		let date = e.date;
		let row = "<tr>"
			+ "<td>" + date + "</td>"
			+ "<td>" + e.invoicedOrdersCount + "</td>"
			+ "<td>" + e.invoicedItemsCount + "</td>"
			+ "<td>" + e.totalRevenue + "</td>"
			+ "</tr>";
		tbody.append(row);
	}
}

function getPosDaySaleByFilter(){
	let startDate = $("#posdaysales-report-form input[name=start-date]").val();
	let endDate = $("#posdaysales-report-form input[name=end-date]").val();

	let url = getPosDaySaleReportUrl() + "?startdate=" + startDate + "&enddate=" + endDate;

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displayPosDaySale(data);
		},
		error: handleAjaxError
	});
}

function downloadPosDaySalesReport() {
	let startDate = $("#posdaysales-report-form input[name=start-date]").val();
	let endDate = $("#posdaysales-report-form input[name=end-date]").val();

	let url = getPosDaySaleReportUrl() + "?startdate=" + startDate + "&enddate=" + endDate;

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			let tsv = "Date	Invoiced Orders Count	Invoiced Items Count	Total Revenue\n";  
			for(row of data){
				tsv += (row.date + "	" + row.invoicedOrdersCount + "	" + row.invoicedItemsCount + "	" + row.totalRevenue);
				tsv+="\n";
			}  
			let hiddenElement = document.createElement("a");  
			hiddenElement.href = "data:text/tsv;charset=utf-8," + encodeURI(tsv);  
			hiddenElement.target = "_blank";  
			hiddenElement.download = "daily-sales-report-"+ startDate + "-to-" + endDate +".tsv";  
			hiddenElement.click();
			hiddenElement.remove();
		},
		error: handleAjaxError
	});
}

function getDefaultDate(){

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
	$("#posdaysales-report-form input[name=end-date]").attr("max", today);
	$("#posdaysales-report-form input[name=end-date]").val(today);

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
	$("#posdaysales-report-form input[name=start-date]").attr("max", today);
	$("#posdaysales-report-form input[name=start-date]").val(monthAgo);
}

function init() {
	getDefaultDate();
  	getPosDaySaleByFilter();
	$("#apply-filter").click(getPosDaySaleByFilter);
 	$("#download-tsv-posdaysales-report").click(downloadPosDaySalesReport);
}

$(document).ready(init);
