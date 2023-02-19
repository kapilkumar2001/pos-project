function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function updateInventory() {
	var barcode = $("#inventory-edit-form input[name=barcode]").val();
	var url = getInventoryUrl() + "/" + barcode;
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
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
	return false;
}

function getInventoryList() {
	var url = getInventoryUrl();
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
	data = data.reverse();
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var userRole = $('.user-role').find('span').text();
	let serialNumber = 1;
	for (var i in data) {
		var e = data[i];
		var buttonHtml = '';
		if(userRole=="supervisor"){
		    buttonHtml += '<button onclick="displayEditInventory(\'' + e.barcode + '\')\" style=\'border: none;margin-right:8px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
		}
		var row = '<tr>'
			+ '<td>' + serialNumber + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.productName + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
		serialNumber+=1;
	}
	$('[data-toggle="tooltip"]').tooltip()
}

function displayEditInventory(barcode) {
	var url = getInventoryUrl() + "/" + barcode;
	console.log(url)
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

var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
	processCount = 0;
	fileData = [];
	errorData = [];
	$('#download-errors').remove();

	var file = $('#inventoryFile')[0].files[0];
	if($('#inventoryFile')[0].files.length==0){
		showError("Please Choose File");
		return;
	}

	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	if(fileData.length>5000){
	showError("Data limit exceeded. Max data limit - 5000 rows");
		return;
	}
	if($('#upload-modal-data-row').length==0){
		var $modalbody = $('#upload-inventory-modal').find('.modal-body');
		var row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		$modalbody.append(row);
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
		var $modalfooter = $('#upload-inventory-modal').find('.modal-footer');
		var htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download' style='font-size:16px;color:white;padding-right: 4px;'></i>Download Errors</button>";
		$modalfooter.prepend(htmlButton);
		getInventoryList();
		return;
	}

	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getInventoryUrl();
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
	var $file = $('#inventoryFile');
	$file.val('');
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
	var $file = $('#inventoryFile');
	var fileName = $file.val().split("\\")[2];
	$('#inventoryFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data) {
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
    $("#inventory-edit-form input[name=quantity]").val(data.quantity);
	document.getElementById("edit-inventory-modal-title").innerHTML = ("Edit Inventory : " + data.barcode);
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
