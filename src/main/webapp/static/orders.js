function getOrderUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getInvoiceUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/invoice";
}

function getProductUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getInventoryUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

let editOrderModelOrderId;

function createOrder() {
	let form = $("#order-list-form");
	let json = createConvert(form);

	if(!json) { return;}

	if(json.length===2) {
		showError("Order can't be created without item");
		return;
	}

	let url = getOrderUrl();

	$.ajax({
		url: url,
		type: "POST",
		data: json,
		headers: {
			"Content-Type": "application/json"
		},
		success: function (response) {
			getOrdersList();

			let thead = $("#order-item-table").find("thead");
			thead.empty();
			let tbody = $("#order-item-table").find("tbody");
			tbody.empty();

			$("#create-order-modal").modal("hide");
			showSuccess("Order created succesfully!");
			tmpCreateOrderId = 0;
		},
		error: handleAjaxError
	});
}

function updateOrder() {
	let orderId = editOrderModelOrderId;
	let form = $("#edit-order-list-form");
	let json = updateConvert(form);

	if(!json) { return;}

	if(json.length===2){
		showError("Order can't be updated without any item");
		return;
	}

	let url = getOrderUrl() + "/" + orderId;

	$.ajax({
		url: url,
		type: "PUT",
		data: json,
		headers: {
			"Content-Type": "application/json"
		},
		success: function (response) {
			getOrdersList();

			let thead = $("#order-item-table").find("thead");
			thead.empty();
			let tbody = $("#edit-order-item-table").find("tbody");
			tbody.empty();

			$("#edit-order-modal").modal("hide");
			showSuccess("Order updated succesfully!");
		},
		error: handleAjaxError
	});
}

function cancelOrder() {
	let orderId = editOrderModelOrderId;
	let url = getOrderUrl() + "/cancel/" + orderId;

	$.ajax({
		url: url,
		type: "PUT",
		success: function (response) {
			getOrdersList();

			let thead = $("#order-item-table").find("thead");
			thead.empty();
			let tbody = $("#edit-order-item-table").find("tbody");
			tbody.empty();

			$("#edit-order-modal").modal("hide");
			showSuccess("Order cancelled succesfully!");
		},
		error: handleAjaxError
	});
}

function getOrdersList() {
	let url = getOrderUrl();

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displayOrdersList(data);
		},
		error: handleAjaxError
	});
}

function getOrderItems(id) {
	let url = getOrderUrl() + "/" + id;

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			viewOrderItems(data);
		},
		error: handleAjaxError
	});
}

function getProductsList() {
	let url = getProductUrl();

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displayBarcodesList(data);
		},
		error: handleAjaxError
	});
}

function displayOrdersList(data) {
	let thead = $("#order-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col' class='text-center'>Order ID</th> <th scope='col' class='text-center'>Created At</th> <th scope='col' class='text-center'>Updated At</th> <th scope='col' class='text-center'>Status</th> <th scope='col' class='text-center'>Actions</th> </tr>";
	thead.append(header);

	let tbody = $("#order-table").find("tbody");
	tbody.empty();

	data = data.reverse();

	for (let i in data) {
		let e = data[i];
		let buttonHtml = "";
		let status;
		if (e.status === "invoiced") {
			status = "<span class='badge badge-success text-dark'>Invoiced</span>";
			buttonHtml += "<button onclick='viewOrder(" + e.id + ")' class='border-0 mr-4 bg-transparent' data-toggle='tooltip' data-placement='bottom' title='View'><i class='fa fa-eye text-dark'></i></button>"
			buttonHtml += "<button onclick='getInvoice(" + e.id + ")' class='border-0 bg-transparent' data-toggle='tooltip' data-placement='bottom' title='Download Invoice'><i class='fa fa-download text-dark'></i></button>"
		}
		else if(e.status === "cancelled"){
			status = "<span class='badge badge-danger text-dark'>Cancelled</span>";
		}
		else {
			status = "<span class='badge badge-warning text-dark'>Created</span>";
			buttonHtml += "<button onclick='editOrder(" + e.id + ")' class='border-0 mr-4 bg-transparent' data-toggle='tooltip' data-placement='bottom' title='Edit' id='edit-order" + e.id + "'><i class='far fa-edit text-dark'></i></button>"
			buttonHtml += "<button onclick='generateInvoice(" + e.id + ")' class='border-0 bg-transparent' data-toggle='tooltip' data-placement='bottom' title='Generate Invoice'><i class='fa fa-file-text text-dark'></i></button>"
		}
		let row = "<tr>"
			+ "<td class='text-center'>" + e.id + "</td>"
			+ "<td class='text-center'>" + e.createdAt + "</td>"
			+ "<td class='text-center'>" + e.updatedAt + "</td>"
			+ "<td class='text-center'>" + status + "</td>"
			+ "<td class='text-center'>" + buttonHtml + "</td>"
			+ "</tr>";
		tbody.append(row);
	}
	$("[data-toggle='tooltip']").tooltip()
}

// Create Order - Add items to the order list 
let tmpCreateOrderId = 0; 
let mapQuantity = new Map();
let maptmpCreateOrderId = new Map();

function addItemInList() {
	let barcode = $("#add-order-item-form select[name=barcode]").val();
	let quantity = $("#add-order-item-form input[name=quantity]").val();
	let sellingPrice = $("#add-order-item-form input[name=selling-price]").val();

	if(barcode === "" || quantity === "" || sellingPrice === ""){
		showError("Please fill all the fields!");
		return;
	}

	if(quantity <= 0){
		showError("Quantity should be greater than 0");
		return;
	}

	if(quantity%1 !== 0){
		showError("Quantity should be a integer value");
		return;
	}

	if(sellingPrice < 0){
		showError("Selling Price should be greater than or equal to 0");
		return;
	}

	if(mapQuantity.has(barcode + sellingPrice)) {
		quantity = parseInt(quantity) + parseInt(mapQuantity.get(barcode + sellingPrice));
		deleteItem(maptmpCreateOrderId.get(barcode + sellingPrice), barcode, sellingPrice);

		mapQuantity.set(barcode + sellingPrice, quantity);
		maptmpCreateOrderId.set(barcode + sellingPrice, tmpCreateOrderId);

		displayOrderItemList(barcode, quantity, sellingPrice, tmpCreateOrderId);
		tmpCreateOrderId = tmpCreateOrderId + 1;
	}
	else {
		mapQuantity.set(barcode + sellingPrice, quantity);
		maptmpCreateOrderId.set(barcode + sellingPrice, tmpCreateOrderId);

		displayOrderItemList(barcode, quantity, sellingPrice, tmpCreateOrderId);
		tmpCreateOrderId = tmpCreateOrderId + 1;
	}

	$("#add-order-item-form input[name=selling-price]").val("");
	$("#add-order-item-form input[name=quantity]").val("");
	$("#add-order-item-form select[name=barcode]").prop("selectedIndex", 0);
}

function displayOrderItemList(barcode, quantity, sellingPrice, tmpId) {
	if(tmpId===0){
		let thead = $("#order-item-table").find("thead");
		let header = "<tr> <th scope='col'>Barcode</th><th scope='col'>Quantity</th> <th scope='col'>Selling Price</th> <th scope='col' class='text-center'>Actions</th></tr>";
	    thead.prepend(header);
	}

	let tbody = $("#order-item-table").find("tbody");

	let buttonHtml = "<button onclick='createOrderEditOrSaveItem(" + tmpId + ")' class='border-0 bg-transparent mr-4' id='edit" + tmpId + "' data-toggle='tooltip' data-placement='bottom' title='Edit'><i class='fa fa-edit text-dark'></i></button>"
	buttonHtml += "<button onclick='deleteItem(" + tmpId + ",\"" + barcode + "\"," + sellingPrice + ")' class='border-0 bg-transparent' id='remove" + tmpId + "' data-toggle='tooltip' data-placement='bottom' title='Remove'><i class='fa fa-trash-o text-danger'></i></button>"
	let row = "<tr id='row" + tmpId + "'>"
		+ "<td> <div class='form-group'><input type='text' class='form-control form-control-sm' name='barcode" + tmpId + "' id='barcode" + tmpId + "' placeholder='Enter Barcode' value='"+ barcode + "' readonly='true'></div> </td>"
		+ "<td> <div class='form-group'><input type='number' class='form-control form-control-sm' name='quantity" + tmpId + "' id='quantity" + tmpId + "' placeholder='Enter Quantity' value='"+ quantity + "' readonly='true'></div> </td>"
		+ "<td> <div class='form-group'><input type='number' class='form-control form-control-sm' name='selling-price" + tmpId + "' id='selling-price" + tmpId + "' placeholder='Enter Price' value='"+ sellingPrice + "' readonly='true'></div> </td>"
		+ "<td class='text-center'>" + buttonHtml + "</td>"
		+ "</tr>";
	tbody.prepend(row);

	$("[data-toggle='tooltip']").tooltip({trigger : "hover"})
}

let createOrderPreviousQuantity;
let createOrderPreviousSellingPrice;

function createOrderEditOrSaveItem(tmpId) {
	event.preventDefault();

	if($("#order-list-form input[name=quantity" + tmpId + "]").prop("readonly") === true) {
		createOrderPreviousQuantity =  $("#order-list-form input[name=quantity" + tmpId + "]").val();
		createOrderPreviousSellingPrice = $("#order-list-form input[name=selling-price" + tmpId + "]").val();

		$("#order-list-form input[name=quantity" + tmpId + "]").prop("readonly", false);
		$("#order-list-form input[name=selling-price" + tmpId + "]").prop("readonly", false);
	}
	else {
		currentQuantity =  $("#order-list-form input[name=quantity" + tmpId + "]").val();
		currentSellingPrice = $("#order-list-form input[name=selling-price" + tmpId + "]").val();
		currentBarcode = $("#order-list-form input[name=barcode" + tmpId + "]").val();

		if(currentQuantity==="" || currentSellingPrice==="") {
			showError("Please fill all the fields!");
		    return;
		}

		if(currentQuantity<=0){
			showError("Quantity should be greater than 0");
			return;
		}
	
		if(currentQuantity%1 !== 0){
			showError("Quantity should be a integer value");
			return;
		}

		if(currentSellingPrice<0){
			showError("Selling Price should be greater than or equal to 0");
			return;
		}

		$("#order-list-form input[name=quantity" + tmpId + "]").prop("readonly", true);
		$("#order-list-form input[name=selling-price" + tmpId + "]").prop("readonly", true);

		if(currentSellingPrice === createOrderPreviousSellingPrice) {
			mapQuantity.set(currentBarcode + currentSellingPrice, currentQuantity);
		}
		else {
			mapQuantity.delete(currentBarcode + createOrderPreviousSellingPrice);
			let id = maptmpCreateOrderId.get(currentBarcode + createOrderPreviousSellingPrice)
			maptmpCreateOrderId.delete(currentBarcode + createOrderPreviousSellingPrice);

			mapQuantity.set(currentBarcode + currentSellingPrice, currentQuantity);
			maptmpCreateOrderId.set(currentBarcode + currentSellingPrice, id);
		}
	}

	var button = $("#edit" + tmpId);
	if (button.html().includes("fa-edit")) {
		button.attr('data-original-title', 'Save');
		button.html("<i class='fa fa-save text-dark'></i>");
	} else {
		button.attr('data-original-title', 'Edit');
		button.html("<i class='fa fa-edit text-dark'></i>");
	}
}

let editOrderPreviousQuantity;
let editOrderPreviousSellingPrice;

function editOrderEditOrSaveItem(tmpId) {
	event.preventDefault();
	
	if($("#edit-order-list-form input[name=quantity" + tmpId + "]").prop("readonly") == true) {
		editOrderPreviousQuantity =  $("#edit-order-list-form input[name=quantity" + tmpId + "]").val();
		editOrderPreviousSellingPrice = $("#edit-order-list-form input[name=selling-price" + tmpId + "]").val();

		$("#edit-order-list-form input[name=quantity" + tmpId + "]").prop("readonly", false);
		$("#edit-order-list-form input[name=selling-price" + tmpId + "]").prop("readonly", false);
	}
	else {
		currentQuantity =  $("#edit-order-list-form input[name=quantity" + tmpId + "]").val();
		currentSellingPrice = $("#edit-order-list-form input[name=selling-price" + tmpId + "]").val();
		currentBarcode = $("#edit-order-list-form input[name=barcode" + tmpId + "]").val();

		if(currentQuantity==="" || currentSellingPrice==="") {
			showError("Please fill all the fields!");
		    return;
		}

		if(currentQuantity<=0){
			showError("Quantity should be greater than 0");
			return;
		}

		if(currentQuantity%1 !== 0){
			showError("Quantity should be a integer value");
			return;
		}
	
		if(currentSellingPrice<0){
			showError("Selling Price should be greater than or equal to 0");
			return;
		}

		$("#edit-order-list-form input[name=quantity" + tmpId + "]").prop("readonly", true);
		$("#edit-order-list-form input[name=selling-price" + tmpId + "]").prop("readonly", true);

		if(currentSellingPrice === editOrderPreviousSellingPrice) {
			mapQuantityEditOrder.set(currentBarcode + currentSellingPrice, currentQuantity);
		}
		else {
			mapQuantityEditOrder.delete(currentBarcode + editOrderPreviousSellingPrice);
			let id = maptmpEditOrderId.get(currentBarcode + editOrderPreviousSellingPrice)
			maptmpEditOrderId.delete(currentBarcode + editOrderPreviousSellingPrice);

			mapQuantityEditOrder.set(currentBarcode + currentSellingPrice, currentQuantity);
			maptmpEditOrderId.set(currentBarcode + currentSellingPrice, id);
		}
	}

	var button = $("#edit" + tmpId);
	if (button.html().includes("fa-edit")) {
		button.attr('data-original-title', 'Save');
		button.html("<i class='fa fa-save text-dark'></i>");
	} else {
		button.attr('data-original-title', 'Edit');
		button.html("<i class='fa fa-edit text-dark'></i>");
	}
}

function deleteItem(tmpId, barcode, sellingPrice) {
	$("#remove" + tmpId).tooltip("hide");
	$("#row" + tmpId).remove();

	if(mapQuantity.has(barcode + sellingPrice))
		mapQuantity.delete(barcode + sellingPrice);

	if(mapQuantityEditOrder.has(barcode + sellingPrice))
		mapQuantityEditOrder.delete(barcode + sellingPrice);

	if(maptmpCreateOrderId.has(barcode + sellingPrice))
		maptmpCreateOrderId.delete(barcode + sellingPrice);
	
	if(maptmpEditOrderId.has(barcode + sellingPrice))
		maptmpEditOrderId.delete(barcode + sellingPrice);
}

function displayBarcodesList(data) {
	let select = $("#add-order-item-form select[name=barcode]");
	select.empty();
	let row = "<option value='' disabled selected class='d-none'>Select Barcode</option>";
	select.append(row);
	data = Array.from(new Set(data));
	data.sort(function(a, b){return a.barcode-b.barcode});
    
	for (let i in data) {
		let e = data[i];
		row = "<option value='" + e.barcode + "'>" + e.barcode + "</option>";
		select.append(row);
	}

	select = $("#edit-add-order-item-form select[name=barcode]");
	select.empty();
	row = "<option value='' disabled selected class='d-none'>Select Barcode</option>";
	select.append(row);
	data = Array.from(new Set(data));
	data.sort(function(a, b){return a.barcode-b.barcode});

	for (let i in data) {
		let e = data[i];
		row = "<option value='" + e.barcode + "'>" + e.barcode + "</option>";
		select.append(row);
	}
}

function viewOrder(id) {
	$("#view-order-modal").modal("toggle");
	$("#view-order-modal-title").html("Order No <span class='badge badge-secondary p-2'>" + id + "</span>");
	getOrderItems(id);
}

function viewOrderItems(data) {
	let thead = $("#view-order-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col'>Barcode</th> <th scope='col'>Product Name</th> <th scope='col'>Quantity</th> <th scope='col'>Selling Price</th> </tr>";
	thead.append(header);

	let tbody = $("#view-order-table").find("tbody");
	tbody.empty();
	let totalAmount = 0;

	for (let i in data["orders"]) {
		let e = data["orders"][i];
		let row = "<tr>"
			+ "<td>" + e.barcode + "</td>"
			+ "<td>" + e.productName + "</td>"
			+ "<td>" + e.quantity + "</td>"
			+ "<td>" + e.sellingPrice + "</td>"
			+ "</tr>";
		tbody.append(row);
		totalAmount+=(e.sellingPrice*e.quantity);
	}

	totalAmount = parseFloat(totalAmount).toFixed(2);
	$("#view-order-total-amount").html("Total Amount : " + totalAmount);
}

function editOrder(id) {
	$("#edit-order-modal").modal("toggle");

	getProductsList();

	$("#edit-order-modal-title").html("Edit Order <span class='badge badge-secondary p-2'>" + id + "</span>");
	let url = getOrderUrl() + "/" + id;

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			editOrderModelOrderId = data["id"];
			editOrderItems(data);
		},
		error: handleAjaxError
	});
}

let tmpEditOrderId = 0;
let mapQuantityEditOrder = new Map();
let maptmpEditOrderId = new Map();

function editOrderItems(data) {
	let thead = $("#edit-order-item-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col'>Barcode</th> <th scope='col'>Quantity</th> <th scope='col'>Selling Price</th> <th scope='col' class='text-center'>Actions</th> </tr>";
	thead.append(header);

	let tbody = $("#edit-order-item-table").find("tbody");
	tbody.empty();

	for (let i in data["orders"]) {
		let e = data["orders"][i];
		let orderId = data["id"];
		let buttonHtml = "<button onclick='editOrderEditOrSaveItem(" + tmpEditOrderId + ")' class='border-0 bg-transparent mr-4' id='edit" + tmpEditOrderId + "' data-toggle='tooltip' data-placement='bottom' title='Edit'><i class='fa fa-edit text-dark'></i></button>"
		buttonHtml += "<button onclick='deleteItem(" + tmpEditOrderId + ",\"" + e.barcode + "\"," + e.sellingPrice + ")' class='border-0 bg-transparent' id='remove" + tmpEditOrderId + "' data-toggle='tooltip' data-placement='bottom' title='Remove'><i class='fa fa-trash-o text-danger'></i></button>";
		let row = "<tr id='row" + tmpEditOrderId + "'>"
			+ "<td> <div class='form-group'><input type='text' class='form-control form-control-sm' name='barcode" + tmpEditOrderId + "' id='barcode" + tmpEditOrderId + "' value='" + e.barcode + "' readonly='true'></div> </td>"
			+ "<td> <div class='form-group'><input type='number' class='form-control form-control-sm' name='quantity" + tmpEditOrderId + "' id='quantity" + tmpEditOrderId + "' value='" + e.quantity + "' readonly='true'></div> </td>"
			+ "<td> <div class='form-group'><input type='number' class='form-control form-control-sm' name='selling-price" + tmpEditOrderId + "' id='selling-price" + tmpEditOrderId + "' value='" + e.sellingPrice + "' readonly='true'></div> </td>"
			+ "<td class='text-center'>" + buttonHtml + "</td>"
			+ "<td class='d-none'> <div class='form-group'><input type='hidden' class='form-control form-control-sm' name='orderItemId" + tmpEditOrderId + "' id='orderItemId" + tmpEditOrderId + "' value='" + e.orderItemId + "'></div> </td>"
			+ "<td class='d-none'> <div class='form-group'><input type='hidden' class='form-control form-control-sm' name='orderId' id='orderId' value='" + orderId + "'></div> </td>"
			+ "</tr>";
		tbody.append(row);

		mapQuantityEditOrder.set((e.barcode)+(e.sellingPrice), (e.quantity));
		maptmpEditOrderId.set((e.barcode)+(e.sellingPrice), tmpEditOrderId);
		tmpEditOrderId = tmpEditOrderId + 1;
	}

	$("[data-toggle='tooltip']").tooltip({trigger : "hover"})
}

function openCreateOrderModel() {
	$("#create-order-modal").modal("toggle");
	getProductsList();
}

function createConvert(data) {
	let serialized = data.serializeArray();
	let arr = []

	for (let i = 0; i < serialized.length; i += 3) {
		let obj = {};

		if(serialized[i].value === "" && serialized[i+1].value === "" && serialized[i+2].value === ""){
			continue;
		}

		if(serialized[i].value === "" || serialized[i+1].value === "" || serialized[i+2].value === ""){
			showError("Please fill all the fields!");
			return false;
		}

		obj["barcode"] = serialized[i].value;
		obj["quantity"] = serialized[i + 1].value;
		obj["sellingPrice"] = serialized[i + 2].value;
		arr.push(obj);
	}

	return JSON.stringify(arr);
}

function updateConvert(data) {
	let serialized = data.serializeArray();
	let arr = []

	for (let i = 0; i < serialized.length; i += 5) {
		let obj = {};

		if(serialized[i].value === "" && serialized[i+1].value === "" && serialized[i+2].value === "") {
			continue;
		}

		if(serialized[i].value === "" || serialized[i+1].value === "" || serialized[i+2].value === "") {
			showError("Please fill all the fields!");
			return false;
		}

		obj["barcode"] = serialized[i].value;
		obj["quantity"] = serialized[i + 1].value;
		obj["sellingPrice"] = serialized[i + 2].value;
		obj["orderItemId"] = serialized[i + 3].value;
		arr.push(obj);
	}
	return JSON.stringify(arr);
}

// Edit Order- add item in form
function addIteminEditForm() {
	let barcode = $("#edit-add-order-item-form select[name=barcode]").val();
	let quantity = $("#edit-add-order-item-form input[name=quantity]").val();
	let sellingPrice = $("#edit-add-order-item-form input[name=selling-price]").val();

	if(barcode === "" || quantity === "" || sellingPrice === ""){
		showError("Please fill all the fields!");
		return;
	}

	if(quantity<=0){
		showError("Quantity should be greater than 0");
		return;
	}

	if(quantity%1 !== 0){
		showError("Quantity should be a integer value");
		return;
	}

	if(sellingPrice<0){
		showError("Selling Price should be greater than or equal to 0");
		return;
	}

	if(mapQuantityEditOrder.has(barcode + sellingPrice)){
        quantity = parseInt(quantity) + parseInt(mapQuantityEditOrder.get(barcode + sellingPrice)); 
		$("#edit-order-list-form input[name=quantity" + maptmpEditOrderId.get(barcode + sellingPrice) + "]").val(quantity);
		mapQuantityEditOrder.set(barcode + sellingPrice, quantity);
	}
	else{
		mapQuantityEditOrder.set(barcode + sellingPrice, quantity);
		maptmpEditOrderId.set(barcode + sellingPrice, tmpEditOrderId);

		displayEditItemForm(barcode, quantity, sellingPrice, tmpEditOrderId);
		tmpEditOrderId = tmpEditOrderId + 1;
	}

	$("#edit-add-order-item-form input[name=selling-price]").val("");
	$("#edit-add-order-item-form input[name=quantity]").val("");
	$("#edit-add-order-item-form select[name=barcode]").prop("selectedIndex", 0);
}

function displayEditItemForm(barcode, quantity, sellingPrice, tmpId){
	let tbody = $("#edit-order-item-table").find("tbody");
	let buttonHtml = "<button onclick='editOrderEditOrSaveItem(" + tmpId + ")' class='border-0 bg-transparent mr-4' id='edit" + tmpId + "' data-toggle='tooltip' data-placement='bottom' title='Edit'><i class='fa fa-edit text-dark'></i></button>"
	buttonHtml += "<button onclick='deleteItem(" + tmpId + ",\"" + barcode + "\"," + sellingPrice + ")' class='border-0 bg-transparent' id='remove" + tmpId + "' data-toggle='tooltip' data-placement='bottom' title='Remove'><i class='fa fa-trash-o text-danger'></i></button>";
	let row = "<tr id='row" + tmpId + "'>"
	    + "<td> <div class='form-group'><input type='text' class='form-control form-control-sm' name='barcode" + tmpId + "' id='barcode" + tmpId + "' value='" + barcode + "' readonly='true'></div> </td>"
		+ "<td> <div class='form-group'><input type='number' class='form-control form-control-sm' name='quantity" + tmpId + "' id='quantity" + tmpId + "' value='" + quantity + "' readonly='true'></div> </td>"
		+ "<td> <div class='form-group'><input type='number' class='form-control form-control-sm' name='selling-price" + tmpId + "' id='selling-price" + tmpId + "' value='" + sellingPrice + "' readonly='true'></div> </td>"
		+ "<td class='text-center'>" + buttonHtml + "</td>"
		+ "<td class='d-none'> <div class='form-group'><input type='hidden' class='form-control form-control-sm' name='orderItemId" + tmpId + "' id='orderItemId" + tmpId + "' value='0'></div></td>"
		+ "<td class='d-none'> <div class='form-group'><input type='hidden' class='form-control form-control-sm' name='orderId' id='orderId' value='" + editOrderModelOrderId + "'></div></td>"
		+ "</tr>";
	tbody.prepend(row);

	$("[data-toggle='tooltip']").tooltip({trigger : "hover"})
}

function cancelCreate() {
	tmpCreateOrderId=0;
	let thead = $("#order-item-table").find("thead");
	thead.empty();

	let tbody = $("#order-item-table").find("tbody");
	tbody.empty();

	$("#add-order-item-form input[name=selling-price]").val("");
	$("#add-order-item-form input[name=quantity]").val("");
	$("#add-order-item-form select[name=barcode]").prop("selectedIndex", 0);
	$("#create-order-modal").modal("hide");
}

function getInvoice(id) {
	let url = getInvoiceUrl() + "/" + id;
	window.open(url, "_blank");
	getOrdersList();
}

function generateInvoice(id) {
	let url = getInvoiceUrl() + "/" + id;

	$.ajax({
		url: url,
		type: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		success: function (response) {
			getInvoice(id);
		},
		error: handleAjaxError
	});
}

function init() {
	getOrdersList();
	$("#create-order-button").click(openCreateOrderModel);
	$("#add-item").click(addItemInList);
	$("#create-order").click(createOrder);
	$("#cancel-order").click(cancelOrder);
	$("#update-order").click(updateOrder);
	$("#edit-add-item").click(addIteminEditForm);
	$("#cancel-create").click(cancelCreate);
}

$(document).ready(init);


