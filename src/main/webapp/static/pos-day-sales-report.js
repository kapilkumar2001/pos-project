function getPosDaySaleReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/posdaysales-report/";
}

function displayPosDaySale(data) {
	var $tbody = $('#posdaysales-report-table').find('tbody');
	$tbody.empty();
	for (var i in data) {
		var e = data[i];
		var date = e.date;
		var dateShow = date.join("/");
		var row = '<tr>'
			+ '<td>' + dateShow + '</td>'
			+ '<td>' + e.invoicedOrdersCount + '</td>'
			+ '<td>' + e.invoicedItemsCount + '</td>'
			+ '<td>' + e.totalRevenue + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
}

function getPosDaySaleByFilter(){
	var startDate = $("#posdaysales-report-form input[name=startDate]").val();
	var endDate = $("#posdaysales-report-form input[name=endDate]").val();

	var url = getPosDaySaleReportUrl() + '?startdate=' + startDate + '&enddate=' + endDate;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log(data);
			displayPosDaySale(data);
		},
		error: handleAjaxError
	});
}

function downloadPosDaySalesReport() {
	var startDate = $("#posdaysales-report-form input[name=startDate]").val();
	var endDate = $("#posdaysales-report-form input[name=endDate]").val();

	var url = getPosDaySaleReportUrl() + '?startdate=' + startDate + '&enddate=' + endDate;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log(data);
			var tsv = 'Date	Invoiced Orders Count	Invoiced Items Count	Total Revenue\n';  
			for(row of data){
				tsv += (row.date + '	' + row.invoicedOrdersCount + '	' + row.invoicedItemsCount + '	' + row.totalRevenue);
				// tsv+=Object.values(row).join('	');
				tsv+='\n';
			}  
			var hiddenElement = document.createElement('a');  
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
	var date = new Date();
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();

	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;

	var today = year + "-" + month + "-" + day;  
	// document.getElementById("inputPosEndDate").required = true;     
	document.getElementById("inputPosEndDate").setAttribute("max", today);
	document.getElementById("inputPosEndDate").value = today;

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
	// document.getElementById("inputPosStartDate").required = true;       
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
