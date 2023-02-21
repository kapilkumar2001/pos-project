function getPosDaySaleReportUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/posdaysales-report/";
}

function displayPosDaySale(data) {
	let $tbody = $('#posdaysales-report-table').find('tbody');
	$tbody.empty();
	for (let i in data) {
		let e = data[i];
		let date = e.date;
		let dateShow = date.join("/");
		let row = '<tr>'
			+ '<td>' + dateShow + '</td>'
			+ '<td>' + e.invoicedOrdersCount + '</td>'
			+ '<td>' + e.invoicedItemsCount + '</td>'
			+ '<td>' + e.totalRevenue + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
}

function getPosDaySaleByFilter(){
	let startDate = $("#posdaysales-report-form input[name=startDate]").val();
	let endDate = $("#posdaysales-report-form input[name=endDate]").val();

	let url = getPosDaySaleReportUrl() + '?startdate=' + startDate + '&enddate=' + endDate;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayPosDaySale(data);
		},
		error: handleAjaxError
	});
}

function downloadPosDaySalesReport() {
	let startDate = $("#posdaysales-report-form input[name=startDate]").val();
	let endDate = $("#posdaysales-report-form input[name=endDate]").val();

	let url = getPosDaySaleReportUrl() + '?startdate=' + startDate + '&enddate=' + endDate;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			let tsv = 'Date	Invoiced Orders Count	Invoiced Items Count	Total Revenue\n';  
			for(row of data){
				tsv += (row.date + '	' + row.invoicedOrdersCount + '	' + row.invoicedItemsCount + '	' + row.totalRevenue);
				tsv+='\n';
			}  
			let hiddenElement = document.createElement('a');  
			hiddenElement.href = 'data:text/tsv;charset=utf-8,' + encodeURI(tsv);  
			hiddenElement.target = '_blank';  
			hiddenElement.download = 'pos-day-sales-report-'+ startDate + '-to-' + endDate +'.tsv';  
			hiddenElement.click();
		},
		error: handleAjaxError
	});
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
	document.getElementById("inputPosEndDate").setAttribute("max", today);
	document.getElementById("inputPosEndDate").value = today;

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
	document.getElementById("inputPosStartDate").setAttribute("max", today);
	document.getElementById("inputPosStartDate").value = monthAgo;
}

function init() {
	getDefaultDate();
    getPosDaySaleByFilter();
	$('#apply-filter').click(getPosDaySaleByFilter);
    $('#download-tsv-posdaysales-report').click(downloadPosDaySalesReport);
}

$(document).ready(init);
