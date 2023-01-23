function getInventoryReportUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory-report/";
}

function getInventory() {
	var url = getInventoryReportUrl();
    console.log(url);
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
            console.log(data);
			displayInventory(data);
		},
		error: handleAjaxError
	});
}

function displayInventory(data) {
	var $tbody = $('#inventory-report-table').find('tbody');
	$tbody.empty();
    var tmp = 1;
	for (var i in data) {
		var e = data[i];
		var row = '<tr>'
			+ '<td>' + tmp + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '</tr>';
		$tbody.append(row);
        tmp=tmp+1;
	}
}

function init() {
	
}

$(document).ready(init);
$(document).ready(getInventory);