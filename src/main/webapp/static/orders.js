function getOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}
function getInvoiceUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/invoice";
}
function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}


var editOrderModelOrderId;
var productMrp;
var availableQuantity;

function createOrder() {
	var $form = $("#order-list-form");
	var json = convertToArrayOfObjectToCreate($form);
	if(json==false){
		return;
	}
	var url = getOrderUrl();
	if(json.length==2){
		showError("Order can't be created without item");
		return;
	}
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getOrderList();
			var $thead = $('#order-item-table').find('thead');
			$thead.empty();
			var $tbody = $('#order-item-table').find('tbody');
			$tbody.empty();
			$('#create-order-modal').modal('hide');
			tmpCreateOrderId = 0;
			showSuccess("Order created succesfully!");
		},
		error: handleAjaxError
	});
	return false;
}

function updateOrder(orderId) {
	var orderId = editOrderModelOrderId;
	var url = getOrderUrl() + "/" + orderId;
	var $form = $("#edit-order-list-form");
	var json = convertToArrayOfObjectToUpdate($form);
	if(json==false){
		return;
	}
	if(json.length==2){
		showError("Order can't be updated without any item");
		return;
	}
	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getOrderList();
			var $thead = $('#order-item-table').find('thead');
			$thead.empty();
			var $tbody = $('#edit-order-item-table').find('tbody');
			$tbody.empty();
			$('#edit-order-modal').modal('hide');
			showSuccess("Order updated succesfully!");
		},
		error: handleAjaxError
	});
	return false;
}

function cancelOrder(orderId) {
	var orderId = editOrderModelOrderId;
	var url = getOrderUrl() + "/cancel/" + orderId;
	$.ajax({
		url: url,
		type: 'PUT',
		success: function (response) {
			getOrderList();
			var $thead = $('#order-item-table').find('thead');
			$thead.empty();
			var $tbody = $('#edit-order-item-table').find('tbody');
			$tbody.empty();
			$('#edit-order-modal').modal('hide');
			showSuccess("Order cancelled succesfully!");
		},
		error: handleAjaxError
	});
	return false;
}

function getOrderList() {
	var url = getOrderUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayOrderList(data);
		},
		error: handleAjaxError
	});
}

function getOrderItems(id) {
	var url = getOrderUrl() + '/' + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			viewOrderItems(data);
		},
		error: handleAjaxError
	});
}

function getProductsList() {
	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayBarcodesList(data);
		},
		error: handleAjaxError
	});
}

function getProduct(barcode){
	var url = getProductUrl() + '/barcode/' + barcode;
	console.log(url);
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			productMrp = data.mrp;
			document.getElementById("mrp-field-create-order").innerHTML = ('<p style="margin-left:5px; color:green">MRP: ' + data.mrp + '</p>');
			document.getElementById("mrp-field-edit-order").innerHTML = ('<p style="margin-left:5px; color:green">MRP: ' + data.mrp + '</p>');
		},
		error: function() {
			handleAjaxError
		}
	});
}

function getInventory(barcode){
	var url = getInventoryUrl() + '/' + barcode;
	console.log(url);
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			availableQuantity = data.quantity;
			document.getElementById("available-quantity-field-create-order").innerHTML = ('<p style="margin-left:10px; color:green">Available Quantity: ' + data.quantity + '</p>');
			document.getElementById("available-quantity-field-edit-order").innerHTML = ('<p style="margin-left:10px; color:green">Available Quantity: ' + data.quantity + '</p>');
		},
		error: function() {
			handleAjaxError
		}
	});
}


function displayOrderList(data) {
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	data = data.reverse();
	for (var i in data) {
		var e = data[i];
		var buttonHtml = '';
		var date = new Date((e.createdAt).replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"));
		var options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: true };
		var createdAt = new Intl.DateTimeFormat('en-US', options).format(date);
		date = new Date((e.updatedAt).replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"));
		var updatedAt = new Intl.DateTimeFormat('en-US', options).format(date);
		var status;
		if (e.status == 'invoiced') {
			status = '<span class="badge badge-pill badge-success">Invoiced</span>';
			buttonHtml += '<button onclick="viewOrder(' + e.id + ')" style=\'border: none;margin-right:16px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="View Order"><i class=\'fa fa-eye\' style=\'font-size:18px;color:blue;\'></i></button>'
			buttonHtml += '<button onclick="getInvoice(' + e.id + ')" style=\'border: none; margin-left:16px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Download Invoice"><i class=\'fa fa-download\' style=\'font-size:18px;color:black;\'></i></button>'
		}
		else if(e.status== 'cancelled'){
			status = '<span class="badge badge-pill badge-danger">Cancelled</span>';
		}
		else {
			status = '<span class="badge badge-pill badge-warning">Created</span>';
			buttonHtml += '<button onclick="editOrder(' + e.id + ')" style=\'border: none;margin-right:16px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
			buttonHtml += '<button onclick="generateInvoice(' + e.id + ')" style=\'border: none; margin-left:16px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Generate Invoice"><i class=\'fa fa-file-text\' style=\'font-size:18px;color:black;\'></i></button>'
		}
		var row = '<tr>'
			+ '<td>' + e.id + '</td>'
			+ '<td>' + createdAt + '</td>'
			+ '<td>' + updatedAt + '</td>'
			+ '<td>' + status + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
	$('[data-toggle="tooltip"]').tooltip()
}

// Create Order - Add items to the order list 
function addItemInList() {
	let barcode = document.getElementById("add-order-item-form").elements[0].value;
	let quanity = document.getElementById("add-order-item-form").elements[1].value;
	let sellingPrice = document.getElementById("add-order-item-form").elements[2].value;
	console.log(barcode); 
	console.log(quanity);
	console.log(sellingPrice);
	if(barcode=='' || quanity=='' || sellingPrice==''){
		showError("Please fill all the fields!");
		return;
	}
	if(quanity>availableQuantity){
		showError("Not enough quantity available");
		return;
	}
	if(sellingPrice > productMrp){
		showError("Selling Price should be less than or equal to MRP");
		return;
	}
	displayOrderItemList(barcode, quanity, sellingPrice);

	$("#add-order-item-form input[name=sellingPrice]").val("");
	$("#add-order-item-form input[name=quantity]").val("");
	document.getElementById("inputBarcode").selectedIndex = 0;
	document.getElementById("mrp-field-create-order").innerHTML = '';
	document.getElementById("available-quantity-field-create-order").innerHTML = '';
}

var tmpCreateOrderId = 0;
function displayOrderItemList(barcode, quanity, sellingPrice) {
	if(tmpCreateOrderId==0){
		var $thead = $('#order-item-table').find('thead');
		var row = '<tr> <th scope="col">Barcode</th><th scope="col">Quantity</th> <th scope="col">Selling Price</th> <th scope="col">Actions</th></tr>';
	    $thead.prepend(row);
	}
	var $tbody = $('#order-item-table').find('tbody');
	var buttonHtml = '<button onclick="deleteItem(' + tmpCreateOrderId + ')" style=\'border: none;margin-right:8px; background-color:transparent\'><i class=\'fa fa-trash-o\' style=\'font-size:18px;color:red;\'></i></button>'
	var row = '<tr id="row' + tmpCreateOrderId + '">'
		+ '<td> <div class="form-group"><input type="text" class="form-control form-control-sm" name="barcode' + tmpCreateOrderId + '" id="barcode' + tmpCreateOrderId + '" placeholder="Enter Barcode" value="'+ barcode + '" readonly="true"></div> </td>'
		+ '<td> <div class="form-group"><input type="number" class="form-control form-control-sm" name="quantity' + tmpCreateOrderId + '" id="quantity' + tmpCreateOrderId + '" placeholder="Enter Quantity" value="'+ quanity + '" required></div> </td>'
		+ '<td> <div class="form-group"><input type="number" class="form-control form-control-sm" name="sellingPrice' + tmpCreateOrderId + '" id="sellingPrice' + tmpCreateOrderId + '" placeholder="Enter Price" value="'+ sellingPrice + '" required></div> </td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
	$tbody.prepend(row);
	tmpCreateOrderId = tmpCreateOrderId + 1;
}

function deleteItem(tmpId) {
	rowtmp = "row" + tmpId;
	document.getElementById(`${rowtmp}`).remove();
	return false;
}

function displayBarcodesList(data) {
	let $select = $('#inputBarcode');
	$select.empty();
	var row = "<option value='' disabled selected style='display: none'>Select Barcode</option>";
	$select.append(row);
	data = Array.from(new Set(data));
	for (var i in data) {
		var e = data[i];
		row = "<option value='" + e.barcode + "'>" + e.barcode + "</option>";
		$select.append(row);
	}

	$select = $('#inputBarcodeEditOrder');
	$select.empty();
	var row = "<option value='' disabled selected style='display: none'>Select Barcode</option>";
	$select.append(row);
	data = Array.from(new Set(data));
	for (var i in data) {
		var e = data[i];
		row = "<option value='" + e.barcode + "'>" + e.barcode + "</option>";
		$select.append(row);
	}
}


// View order 
function viewOrder(id) {
	$('#view-order-modal').modal('toggle');
	document.getElementById("view-order-modal-title").innerHTML = ("View Order: " + id);
	getOrderItems(id);
}

function viewOrderItems(data) {
	var $tbody = $('#view-order-table').find('tbody');
	$tbody.empty();
	let totalAmount = 0;
	for (var i in data['orders']) {
		var e = data['orders'][i];
		var row = '<tr>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.productName + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>' + e.sellingPrice + '</td>'
			+ '</tr>';
		$tbody.append(row);
		totalAmount+=(e.sellingPrice*e.quantity);
	}
	document.getElementById("view-order-total-amount").innerHTML = ("Total Amount: " + parseFloat(totalAmount).toFixed(2));
}


// Edit Order
var orderId;
function editOrder(id) {
	$('#edit-order-modal').modal('toggle');
	getProductsList();
	document.getElementById("mrp-field-edit-order").innerHTML = '';
	document.getElementById("available-quantity-field-edit-order").innerHTML = '';

	document.getElementById("edit-order-modal-title").innerHTML = ("Edit Order: " + id);
	var url = getOrderUrl() + '/' + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			editOrderModelOrderId = data['id'];
			editOrderItems(data);
		},
		error: handleAjaxError
	});
}

var tmpEditOrderId = 0;
function editOrderItems(data) {
	getProductsList();
	var $tbody = $('#edit-order-item-table').find('tbody');
	$tbody.empty();
	for (var i in data['orders']) {
		var e = data['orders'][i];
		var orderId = data['id'];
		var $tbody = $('#edit-order-item-table').find('tbody');
		var buttonHtml = '<button onclick="deleteItem(' + tmpEditOrderId + ')" style=\'border: none;margin-right:8px; background-color:transparent\'><i class=\'fa fa-trash-o\' style=\'font-size:18px;color:red;\'></i></button>'
		var row = '<tr id="row' + tmpEditOrderId + '">'
			+ '<td> <div class="form-group"><input type="text" class="form-control form-control-sm" name="editbarcode' + tmpEditOrderId + '" id="editbarcode' + tmpEditOrderId + '" value="' + e.barcode + '" readonly="true"></div> </td>'
			+ '<td> <div class="form-group"><input type="number" class="form-control form-control-sm" name="quantity' + tmpEditOrderId + '" id="quantity' + tmpEditOrderId + '" value="' + e.quantity + '" required></div> </td>'
			+ '<td> <div class="form-group"><input type="number" class="form-control form-control-sm" name="sellingPrice' + tmpEditOrderId + '" id="sellingPrice' + tmpEditOrderId + '" value="' + e.sellingPrice + '" required></div> </td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '<td> <div class="form-group"><input type="hidden" class="form-control form-control-sm" name="orderItemId' + tmpEditOrderId + '" id="orderItemId' + tmpEditOrderId + '" value="' + e.orderItemId + '"></input>'
			+ '<td> <div class="form-group"><input type="hidden" class="form-control form-control-sm" name="orderId" id="orderId" value="' + orderId + '"></input>'
			+ '</tr>';
		$tbody.append(row);
		tmpEditOrderId = tmpEditOrderId + 1;
	}
}

function openCreateOrderModel() {
	$('#create-order-modal').modal('toggle');
	document.getElementById("mrp-field-create-order").innerHTML = '';
	document.getElementById("available-quantity-field-create-order").innerHTML = '';
	getProductsList();
}

function convertToArrayOfObjectToCreate(data) {
	var serialized = data.serializeArray();
	let arr = []
	for (let i = 0; i < serialized.length; i += 3) {
		let obj = {};
		console.log(serialized[i].value);
		console.log(serialized[i + 1].value);
		console.log(serialized[i + 2].value);
		if(serialized[i].value=="" && serialized[i+1].value=="" && serialized[i+2].value==""){
			continue;
		}
		if(serialized[i].value=="" || serialized[i+1].value=="" || serialized[i+2].value==""){
			showError("Please fill all the fields!");
			return false;
		}
		obj['barcode'] = serialized[i].value;
		obj['quantity'] = serialized[i + 1].value;
		obj['sellingPrice'] = serialized[i + 2].value;
		arr.push(obj);
	}
	return JSON.stringify(arr);
}

function convertToArrayOfObjectToUpdate(data) {
	var serialized = data.serializeArray();
	let arr = []
	for (let i = 0; i < serialized.length; i += 5) {
		let obj = {};
		console.log(serialized[i].value);
		console.log(serialized[i + 1].value);
		console.log(serialized[i + 2].value);
		console.log(serialized[i + 3].value);
		if(serialized[i].value=="" && serialized[i+1].value=="" && serialized[i+2].value==""){
			continue;
		}
		if(serialized[i].value=="" || serialized[i+1].value=="" || serialized[i+2].value==""){
			showError("Please fill all the fields!");
			return false;
		}
		obj['barcode'] = serialized[i].value;
		obj['quantity'] = serialized[i + 1].value;
		obj['sellingPrice'] = serialized[i + 2].value;
		obj['orderItemId'] = serialized[i + 3].value;
		arr.push(obj);
	}
	return JSON.stringify(arr);
}


function addIteminEditForm() {
	let barcode = document.getElementById("edit-add-order-item-form").elements[0].value;
	let quanity = document.getElementById("edit-add-order-item-form").elements[1].value;
	let sellingPrice = document.getElementById("edit-add-order-item-form").elements[2].value;
	console.log(barcode); 
	console.log(quanity);
	console.log(sellingPrice);
	if(barcode=='' || quanity=='' || sellingPrice==''){
		showError("Please fill all the fields!");
		return;
	}
	if(quanity>availableQuantity){
		showError("Not enough quantity available");
		return;
	}
	if(sellingPrice > productMrp){
		showError("Selling Price should be less than or equal to MRP");
		return;
	}

	var $tbody = $('#edit-order-item-table').find('tbody');
	var buttonHtml = '<button onclick="deleteItem(' + tmpEditOrderId + ')" style=\'border: none;margin-right:8px; background-color:transparent\'><i class=\'fa fa-trash-o\' style=\'font-size:18px;color:red;\'></i></button>'
	var row = '<tr id="row' + tmpEditOrderId + '">'
	    + '<td> <div class="form-group"><input type="text" class="form-control form-control-sm" name="barcode' + tmpEditOrderId + '" id="barcode' + tmpEditOrderId + '" value="' + barcode + '" readonly="true"></div> </td>'
		+ '<td> <div class="form-group"><input type="number" class="form-control form-control-sm" name="quantity' + tmpEditOrderId + '" id="quantity' + tmpEditOrderId + '" value="' + quanity + '"></div> </td>'
		+ '<td> <div class="form-group"><input type="number" class="form-control form-control-sm" name="sellingPrice' + tmpEditOrderId + '" id="sellingPrice' + tmpEditOrderId + '" value="' + sellingPrice + '"></div> </td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '<td> <div class="form-group"><input type="hidden" class="form-control form-control-sm" name="orderItemId' + tmpEditOrderId + '" id="orderItemId' + tmpEditOrderId + '" value="0"></input>'
		+ '<td> <div class="form-group"><input type="hidden" class="form-control form-control-sm" name="orderId" id="orderId" value="' + orderId + '"></input>'
		+ '</tr>';
	$tbody.prepend(row);
	tmpEditOrderId = tmpEditOrderId + 1;

	$("#edit-add-order-item-form input[name=sellingPrice]").val("");
	$("#edit-add-order-item-form input[name=quantity]").val("");
	document.getElementById("inputBarcodeEditOrder").selectedIndex = 0;
	document.getElementById("mrp-field-edit-order").innerHTML = '';
	document.getElementById("available-quantity-field-edit-order").innerHTML = '';
}

function cancelCreate() {
	tmpCreateOrderId=0;
	var $thead = $('#order-item-table').find('thead');
	$thead.empty();
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	$("#add-order-item-form input[name=sellingPrice]").val("");
	$("#add-order-item-form input[name=quantity]").val("");
	document.getElementById("inputBarcode").selectedIndex = 0;
	document.getElementById("mrp-field").innerHTML = '';
	document.getElementById("available-quantity-field").innerHTML = '';
	$('#create-order-modal').modal('hide');
}




// Invoice functions
function getInvoice(id) {
	var url = getInvoiceUrl() + '/' + id;
	window.open(url, '_blank');
	getOrderList();
}
function generateInvoice(id) {
	var url = getInvoiceUrl() + '/' + id;
	$.ajax({
		url: url,
		type: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getInvoice(id);
		},
		error: handleAjaxError
	});
	return false;
}


function init() {
	getOrderList();
	$('#create-order-button').click(openCreateOrderModel);
	$('#inputBarcode').on('change', function (){
		let barcode = document.getElementById("add-order-item-form").elements[0].value;
		getProduct(barcode);
		getInventory(barcode);
	});
	$('#inputBarcodeEditOrder').on('change', function (){
		let barcode = document.getElementById("edit-add-order-item-form").elements[0].value;
		getProduct(barcode);
		getInventory(barcode);
	});
	$('#add-item').click(addItemInList);
	$('#create-order').click(createOrder);
	$('#cancel-order').click(cancelOrder);
	$('#update-order').click(updateOrder);
	$('#edit-add-item').click(addIteminEditForm);
	$('#cancel-create').click(cancelCreate);
}
$(document).ready(init);


