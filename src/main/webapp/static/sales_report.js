function getSalesReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/sales-report";
}

function getSalesReport() {

    var brand = $("#sales-report-form input[name=brand]").val();
    var category = $("#sales-report-form input[name=category]").val();
    var startDate = $("#sales-report-form input[name=startDate]").val();
    var endDate = $("#sales-report-form input[name=endDate]").val();


	var url = getSalesReportUrl() + '/?startdate=' + startDate + '&enddate=' + endDate + '&brand=' + brand + '&category=' + category;

    console.log("url = " + url);
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

function init() {
	$('#apply-filter').click(getSalesReport);
}

$(document).ready(init);
$(document).ready(getSalesReport);