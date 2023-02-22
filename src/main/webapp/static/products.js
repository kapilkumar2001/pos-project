function getProductUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getBrandsUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function addProduct(event) {
	if(($("#product-form input[name=name]").val()=="") || ($("#product-form input[name=mrp]").val()=="") || 
	    ($("#product-form input[name=barcode]").val()=="") || (document.getElementById("inputBrand").selectedIndex == 0) || 
		(document.getElementById("inputCategory").selectedIndex == 0)){
			showError("Please fill all the fields");
			return;
	}
	let form = $("#product-form");
	let json = toJson(form);
	let url = getProductUrl();
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
			document.getElementById("inputBrand").selectedIndex = 0;
			document.getElementById("inputBrand").innerHTML = "<option value='' disabled selected style='display: none'>Please Choose Brand</option>";
			document.getElementById("inputCategory").selectedIndex = 0;
			document.getElementById("inputCategory").innerHTML = "<option value='' disabled selected style='display: none'>Select Brand First</option>";
			$('#add-product-modal').modal('hide');
		},
		error: handleAjaxError
	});
}

function updateProduct() {
	if(($("#product-edit-form input[name=name]").val()=="") || ($("#product-edit-form input[name=mrp]").val()=="")){
			showError("Please fill all the fields");
			return;
	}
	let form = $("#product-edit-form");
	let json = toJson(form);
	let id = $("#product-edit-form input[name=id]").val();
	let url = getProductUrl() + "/" + id;
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
}

function getProductList() {
	let url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayProductList(data);
		},
		error: handleAjaxError
	});
}

function getBrandsList() {
	let url = getBrandsUrl() + "/get-brands";
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
	let currSelectedBrand = $('#inputBrand').val();
	let url = getBrandsUrl() + "/get-categories/" + currSelectedBrand;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayCategoryList(data);
		},
		error: handleAjaxError
	});
}

// UI display methods
function displayProductList(data) {
	let tbody = $('#product-table').find('tbody');
	tbody.empty();
	let userRole = $('.user-role').find('span').text();
	data = data.reverse();
	let serialNumber = 1;
	for (let i in data) {
		let e = data[i];
		let buttonHtml = '';
		if (userRole == "supervisor") {
			buttonHtml += '<button onclick="displayEditProduct(' + e.id + ')" style=\'border: none;margin-right:8px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
		}
		let row = '<tr>'
			+ '<td>' + serialNumber + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>' + e.mrp + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		tbody.append(row);
		serialNumber+=1;
	}
	$('[data-toggle="tooltip"]').tooltip()
}

function displayBrandList(data) {
	let select = $('#inputBrand');
	select.empty();
	let row = "<option value='' disabled selected style='display: none'>Please Choose Brand</option>";
	select.append(row);
	data = Array.from(new Set(data));
	for (let i in data) {
		let e = data[i];
		row = "<option value='" + e + "'>" + e + "</option>";
		select.append(row);
	}
}

function displayCategoryList(data) {
	let select1 = $('#inputCategory');
	select1.empty();
	let row = "<option value='' disabled selected style='display: none'>Please Choose</option>";
	select1.append(row);
	for (let i in data) {
		let e = data[i];
		let row = "<option value='" + e + "'>" + e + "</option>";
		select1.append(row);
	}
}

function displayEditProduct(id) {
	let url = getProductUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayProduct(data);
		},
		error: handleAjaxError
	});
}

function displayProduct(data) {
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=id]").val(data.id);
	document.getElementById("edit-product-modal-title").innerHTML = ("Edit Product: " + data.barcode);
	$('#edit-product-modal').modal('toggle');
}

function openAddProductModal() {
	$('#add-product-modal').modal('toggle');
	getBrandsList();
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

	let file = $('#productFile')[0].files[0];
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
		let modalbody = $('#upload-product-modal').find('.modal-body');
		let row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		modalbody.append(row);
	}
	uploadRows();
}

function uploadRows() {
	updateUploadDialog();
	if(processCount==fileData.length && errorData.length==0){
		$('#upload-product-modal').modal('hide');
		getProductList();
		showSuccess("Products uploaded succesfully!");
		return;
	}
	else if(processCount==fileData.length){
		let modalfooter = $('#upload-product-modal').find('.modal-footer');
		let htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download' style='font-size:16px;color:white;padding-right: 4px;'></i>Download Errors</button>";
		modalfooter.prepend(htmlButton);
	
		getProductList();
		return;
	}

	let row = fileData[processCount];
	processCount++;

	if(row.barcode==undefined || row.brand==undefined || row.category==undefined || row.name==undefined || row.mrp==undefined){
		showError("Invalid file");
		return;
	}
	let json = JSON.stringify(row);
	let url = getProductUrl();
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
	let file = $('#productFile');
	file.val('');
	$('#productFileName').html("Choose File");
	
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
	let file = $('#productFile');
	let fileName = file.val().split("\\")[2];
	$('#productFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function init() {
	getProductList();
	getBrandsList();
	$('#add-product-button').click(openAddProductModal);
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#productFile').on('change', updateFileName);
	$('#inputBrand').on('change', getCategoriesList);
	$('[data-toggle="tooltip"]').tooltip()
}

$(document).ready(init);

