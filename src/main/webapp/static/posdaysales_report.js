function getPosDaySaleReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/posdaysales-report/";
}

function getPosDaySale() {
	var url = getPosDaySaleReportUrl();
    console.log(url);
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

function init() {
	
}

$(document).ready(init);
$(document).ready(getPosDaySale);