function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getBrandCategoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}

//BUTTON ACTIONS
function addProduct(event) {
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getProductList();
		},
		error: handleAjaxError
	});

	return false;
}

function updateProduct(event) {
	$('#edit-product-modal').modal('toggle');
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
			getProductList();
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
	var url = getBrandCategoryUrl()+ "/get-brands";
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
	var url = getBrandCategoryUrl()+ "/get-categories/" + curSelectedBrand;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayCategoryList(data);
		},
		error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if (processCount == fileData.length) {
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getProductUrl();

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

//UI DISPLAY METHODS

function displayProductList(data) {
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for (var i in data) {
		var e = data[i];
		var buttonHtml = '<button onclick="deleteProduct(' + e.id + ')">delete</button>'
		buttonHtml += ' <button onclick="displayEditProduct(' + e.id + ')">edit</button>'
		var row = '<tr>'
			+ '<td>' + e.id + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.mrp + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
}

function displayBrandList(data) {
	var $select = $('#inputBrand');
	$select.empty();
	var row = "<option value='' disabled selected style='display: none'>Please Choose</option>";
	$select.append(row);
	for (var i in data) {
		var e = data[i];
		row = "<option value='" +e+ "'>" + e + "</option>";
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
		var row = "<option value='" +e+ "'>" + e + "</option>";
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

function resetUploadDialog() {
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
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

//INITIALIZATION CODE
function init() {
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#productFile').on('change', updateFileName);
	$('#inputBrand').on('change', getCategoriesList);
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandsList);

