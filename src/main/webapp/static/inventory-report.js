function getInventoryReportUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory/report/";
}

function getInventory() {
	let url = getInventoryReportUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayInventory(data);
		},
		error: handleAjaxError
	});
}

function displayInventory(data) {
	let thead = $('#inventory-report-table').find('thead');
	thead.empty();
	let header = '<tr> <th scope="col">S.No.</th> <th scope="col">Brand</th> <th scope="col">Category</th> <th scope="col">Quantity</th> </tr>';
	thead.append(header);

	let tbody = $('#inventory-report-table').find('tbody');
	tbody.empty();
    let tmp = 1;
	data = data.reverse();
	for (let i in data) {
		let e = data[i];
		let row = '<tr>'
			+ '<td>' + tmp + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '</tr>';
		tbody.append(row);
        tmp=tmp+1;
	}
}

function getInventoryReport() {
	let url = getInventoryReportUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			data = data.reverse();
			let tsv = 'Brand	Category	Quantity\n';  
			for(row of data){
				tsv+=Object.values(row).join('	');
				tsv+='\n';
			}  
			let hiddenElement = document.createElement('a');  
			hiddenElement.href = 'data:text/tsv;charset=utf-8,' + encodeURI(tsv);  
			hiddenElement.target = '_blank';  
			hiddenElement.download = 'inventory-report.tsv';  
			hiddenElement.click();
		},
		error: handleAjaxError
	});
}

function init() {
	getInventory();
    $('#download-tsv-inventory-report').click(getInventoryReport);
}

$(document).ready(init);
