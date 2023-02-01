function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getBrandCategoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}

//BUTTON ACTIONS
function addProduct(event) {
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();

	console.log(json);

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			showSuccess("Product added succesfully!");
			getProductList();

			$("#product-form input[name=name]").val("");
			$("#product-form input[name=mrp]").val("");
			$("#product-form input[name=barcode]").val("");
			document.getElementById("inputBrand").selectedIndex = -1;
			document.getElementById("inputBrand").innerHTML = "<option value='' disabled selected style='display: none'>Please Choose Brand</option>";
			document.getElementById("inputCategory").selectedIndex = -1;
			document.getElementById("inputCategory").innerHTML = "<option value='' disabled selected style='display: none'>Select Brand First</option>";
			$('#add-product-modal').modal('hide');

		},
		error: handleAjaxError
	});

	return false;
}

function updateProduct(event) {
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			showSuccess("Product updated succesfully!");
			getProductList();
			$('#edit-product-modal').modal('hide');
		},
		error: handleAjaxError
	});

	return false;
}


function getProductList() {
	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayProductList(data);
		},
		error: handleAjaxError
	});
}

function deleteProduct(id) {
	var url = getProductUrl() + "/" + id;

	$.ajax({
		url: url,
		type: 'DELETE',
		success: function (data) {
			getProductList();
		},
		error: handleAjaxError
	});
}

function getBrandsList() {
	var url = getBrandCategoryUrl() + "/get-brands";
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayBrandList(data);
		},
		error: handleAjaxError
	});
}

function getCategoriesList() {
	var curSelectedBrand = $('#inputBrand').val();
	console.log("->", curSelectedBrand);
	var url = getBrandCategoryUrl() + "/get-categories/" + curSelectedBrand;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayCategoryList(data);
		},
		error: handleAjaxError
	});
}


//UI DISPLAY METHODS

function displayProductList(data) {
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	var role = $('.user-role').find('span').text();
	for (var i in data) {
		var e = data[i];
		var buttonHtml = '';
		console.log("role: " + role);
		if (role == "supervisor") {
			buttonHtml += '<button onclick="displayEditProduct(' + e.id + ')" style=\'border: none;margin-right:8px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
		}
		var row = '<tr>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>' + e.mrp + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
}

function displayBrandList(data) {
	var $select = $('#inputBrand');
	$select.empty();
	var row = "<option value='' disabled selected style='display: none'>Please Choose Brand</option>";
	$select.append(row);
	data = Array.from(new Set(data));
	for (var i in data) {
		var e = data[i];
		row = "<option value='" + e + "'>" + e + "</option>";
		$select.append(row);
	}
}

function displayCategoryList(data) {
	var $select1 = $('#inputCategory');
	$select1.empty();
	var row = "<option value='' disabled selected style='display: none'>Please Choose</option>";
	$select1.append(row);
	for (var i in data) {
		var e = data[i];
		var row = "<option value='" + e + "'>" + e + "</option>";
		$select1.append(row);
	}
}

function displayEditProduct(id) {
	var url = getProductUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayProduct(data);
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

	var file = $('#productFile')[0].files[0];

	if($('#productFile')[0].files.length==0){
		showError("Please Choose File");
		return;
	}

	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;

	if(fileData.length>5000){
		showError("Data limit exceeded. Max data limit: 5000 rows");
		return;
	}

	if($('#upload-modal-data-row').length==0){
		var $modalbody = $('#upload-product-modal').find('.modal-body');
		var row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		$modalbody.append(row);
	}

	uploadRows();
}

function uploadRows() {
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length && errorData.length==0){
		$('#upload-product-modal').modal('hide');
		getProductList();
		showSuccess("Products uploaded succesfully!");
		return;
	}
	else if(processCount==fileData.length){
		var $modalfooter = $('#upload-product-modal').find('.modal-footer');
		var htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download' style='font-size:16px;color:white;padding-right: 4px;'></i>Download Errors</button>";
		$modalfooter.prepend(htmlButton);
	
		getProductList();
		return;
	}


	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getProductUrl();

	console.log(json);

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
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
}

function updateFileName() {
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}




function displayProduct(data) {
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function OpenAddProductModal() {
	$('#add-product-modal').modal('toggle');
}

//INITIALIZATION CODE
function init() {
	getProductList();
	getBrandsList();
	$('#add-product-button').click(OpenAddProductModal);
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#productFile').on('change', updateFileName);
	$('#inputBrand').on('change', getCategoriesList);
}

$(document).ready(init);

