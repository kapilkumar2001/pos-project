function getInventoryUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function updateInventory() {
	let barcode = $("#inventory-edit-form input[name=barcode]").val();
	let url = getInventoryUrl() + "/" + barcode;
	let form = $("#inventory-edit-form");
	let json = toJson(form);
	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getInventoryList();
			$('#edit-inventory-modal').modal('hide');
			showSuccess("Quantity updated succesfully!");
		},
		error: handleAjaxError
	});
}

function getInventoryList() {
	let url = getInventoryUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayInventoryList(data);
		},
		error: handleAjaxError
	});
}

//UI display methods
function displayInventoryList(data) {
	let userRole = $('.user-role').find('span').text();
	
	let thead = $('#inventory-table').find('thead');
	thead.empty();
	let header;
	if(userRole=="supervisor"){
		header = '<tr> <th scope="col">S.No.</th> <th scope="col">Barcode</th> <th scope="col">Product Name</th> <th scope="col">Quantity</th> <th scope="col" class="text-center">Action</th> </tr>';
	} else{
		header = '<tr> <th scope="col">S.No.</th> <th scope="col">Barcode</th> <th scope="col">Product Name</th> <th scope="col">Quantity</th> </tr>';
	}
	thead.append(header);

	data = data.reverse();
	let tbody = $('#inventory-table').find('tbody');
	tbody.empty();
	let serialNumber = 1;
	for (let i in data) {
		let e = data[i];
		let buttonHtml = '';
		if(userRole=="supervisor"){
		    buttonHtml += '<button onclick="displayEditInventory(\'' + e.barcode + '\')\" class="border-0 bg-transparent" data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'color:blue;\'></i></button>'
		}
		let row = '<tr>'
			+ '<td>' + serialNumber + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.productName + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td class="text-center">' + buttonHtml + '</td>'
			+ '</tr>';
		tbody.append(row);
		serialNumber+=1;
	}
	$('[data-toggle="tooltip"]').tooltip()
}

function displayEditInventory(barcode) {
	let url = getInventoryUrl() + "/" + barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayInventory(data);
		},
		error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
let fileData = [];
let errorData = [];
let processCount = 0;

function processData() {
	processCount = 0;
	fileData = [];
	errorData = [];
	$('#download-errors').remove();

	let file = $('#inventoryFile')[0].files[0];
	if($('#inventoryFile')[0].files.length==0){
		showError("Please Choose File");
		return;
	}

	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	if(fileData[0].barcode==undefined || fileData[0].quantity==undefined){
		showError("Invalid file");
		return;
	}
	if(fileData.length>5000){
	    showError("Data limit exceeded. Max data limit - 5000 rows");
		return;
	}
	if($('#upload-modal-data-row').length==0){
		let modalbody = $('#upload-inventory-modal').find('.modal-body');
		let row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		modalbody.append(row);
	}
	uploadRows();
}

function uploadRows() {
	updateUploadDialog();
	if(processCount==fileData.length && errorData.length==0){
		$('#upload-inventory-modal').modal('hide');
		showSuccess("Inventory data uploaded succesfully!");
		getInventoryList();
		return;
	}
	else if(processCount == fileData.length) {
		let modalfooter = $('#upload-inventory-modal').find('.modal-footer');
		let htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download text-white mr-1'></i>Download Errors</button>";
		modalfooter.prepend(htmlButton);
		getInventoryList();
		return;
	}

	let row = fileData[processCount];
	processCount++;

	let json = JSON.stringify(row);
	let url = getInventoryUrl();
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			uploadRows();
		},
		error: function (response) {
			row.error=response.responseJSON['message'];
	   		errorData.push(row);
	   		uploadRows();
		}
	});
}

function downloadErrors() {
	writeFileData(errorData);
}

function resetUploadDialog() {
	let file = $('#inventoryFile');
	file.val('');
	$('#inventoryFileName').html("Choose File");
	
	processCount = 0;
	fileData = [];
	errorData = [];

	$('#upload-modal-data-row').remove();
	$('#download-errors').remove();
}

function updateUploadDialog() {
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName() {
	let file = $('#inventoryFile');
	let fileName = file.val().split("\\")[2];
	$('#inventoryFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data) {
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
    $("#inventory-edit-form input[name=quantity]").val(data.quantity);
	document.getElementById("edit-inventory-modal-title").innerHTML = ("Edit Inventory <span class=\"badge badge-pill badge-secondary p-2 ml-2\">" + data.barcode + "</span>");
	$('#edit-inventory-modal').modal('toggle');
}

function init() {
	getInventoryList();
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#inventoryFile').on('change', updateFileName);
	$('[data-toggle="tooltip"]').tooltip()
}

$(document).ready(init);
