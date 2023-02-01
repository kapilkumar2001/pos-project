function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

// BUTTON ACTIONS
function addInventory(event) {
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getInventoryList();
		},
		error: handleAjaxError
	});

	return false;
}

function updateInventory(event) {
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var barcode = $("#inventory-edit-form input[name=barcode]").val();
    console.log(barcode);
	var url = getInventoryUrl() + "/" + barcode;
    console.log(url);
	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
    console.log(json);
	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getInventoryList();
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

//UI DISPLAY METHODS

function displayInventoryList(data) {
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var role = $('.user-role').find('span').text();
	for (var i in data) {
		var e = data[i];
		var buttonHtml = '';
		if(role=="supervisor"){
		    buttonHtml += '<button onclick="displayEditInventory(\'' + e.barcode + '\')\" style=\'border: none;margin-right:8px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
		}
		
		var row = '<tr>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.productName + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
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
		handleError("please choose file");
		return;
	}


	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;

	if(fileData.length>5000){
		handleError("data limit exceeded. Max data limit - 5000 rows");
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
	//Update progress
	updateUploadDialog();
	if(processCount==fileData.length && errorData.length==0){
		$('#upload-inventory-modal').modal('hide');

		showSuccess("inventory data uploaded succesfully!");
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

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getInventoryUrl();

	console.log(json);

	//Make ajax call
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
			row.error = response.responseText
			errorData.push(row);
			uploadRows();
		}
	});
}

function downloadErrors() {
	writeFileData(errorData);
}

function resetUploadDialog() {
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
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

	if(errorData.length==1){
		var $modalfooter = $('#upload-inventory-modal').find('.modal-footer');
		var htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download' style='font-size:16px;color:white;padding-right: 4px;'></i>Download Errors</button>";
		$modalfooter.prepend(htmlButton);
	}

}

function updateFileName() {
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data) {
    // $("#inventory-edit-form input[name=id]").val(data.id);
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
    $("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$('#edit-inventory-modal').modal('toggle');
}

//INITIALIZATION CODE
function init() {
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#inventoryFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getInventoryList);