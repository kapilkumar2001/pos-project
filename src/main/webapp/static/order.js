function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

//BUTTON ACTIONS

function createOrder(){
	
	var $form = $("#order-list-form");
	var json = convertToArrayOfObjectToCreate($form);

	console.log(json);
	var url = getOrderUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();  
			var $tbody = $('#order-item-table').find('tbody');
			$tbody.empty();
			$('#create-order-modal').modal('hide');
	   },
	   error: handleAjaxError
	});


	return false;
}



function updateOrder(id){
	$('#edit-order-modal').modal('toggle');
	//Get the ID

	var id = $('#edit-order-list-form input[name=orderId]').val();	

	console.log(id);
	var url = getOrderUrl() + "/" + id;

	var $form = $("#edit-order-list-form");
	var json = convertToArrayOfObjectToUpdate($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList(); 
			var $tbody = $('#edit-order-item-table').find('tbody');
			$tbody.empty();
			$('#edit-order-modal').modal('hide');
	   },
	   error: handleAjaxError
	});
	return false;
}

function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: handleAjaxError
	});
}

function getOrderItems(id){
	var url = getOrderUrl()+ '/' + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		viewOrderItems(data);  
	   },
	   error: handleAjaxError
	});
}



// display orders list
function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="viewOrder(' + e.id + ')">View</button>'
		buttonHtml += '<button onclick="editOrder(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.time + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

// add items to the order list
var tmpc =0;
function displayOrderItemList(){
	
	var $tbody = $('#order-item-table').find('tbody');
	var buttonHtml = '<button onclick="deleteItem(' + tmpc + ')">Delete</button>'
	var row = '<tr id="row' + tmpc + '">'
	+ '<td> <div class="form-group"><input type="text" class="form-control" name="barcode' + tmpc + '" id="barcode' + tmpc + '" placeholder="enter barcode"></div> </td>'
	+ '<td> <div class="form-group"><input type="number" class="form-control" name="quantity' + tmpc + '" id="quantity' + tmpc + '" placeholder="enter quantity"></div> </td>'
	+ '<td> <div class="form-group"><input type="number" class="form-control" name="sellingPrice' + tmpc + '" id="sellingPrice' + tmpc + '" placeholder="enter price"></div> </td>'
	+ '<td>' + buttonHtml + '</td>'
	+ '</tr>';
	$tbody.prepend(row);
	tmpc=tmpc+1;
}

function deleteItem(tmp){
	rowtmp = "row"+tmp;
	document.getElementById(`${rowtmp}`).remove();
	return false;
}


// edit order 


// view order 
function viewOrder(id){
	$('#view-order-modal').modal('toggle');
	getOrderItems(id);
}

function viewOrderItems(data){
	var $tbody = $('#view-order-table').find('tbody');
	$tbody.empty();
	for(var i in data['orders']){
		var e = data['orders'][i];
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.productName + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

// edit order

var orderId;

function editOrder(id){
	$('#edit-order-modal').modal('toggle');

	var url = getOrderUrl()+ '/' + id;
	
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		    orderId = data['id'];
	   		editOrderItems(data);  
	   },
	   error: handleAjaxError
	});
}

var tmpe = 0;
function editOrderItems(data){
	var $tbody = $('#edit-order-item-table').find('tbody');
	$tbody.empty();

	for(var i in data['orders']){
		var e = data['orders'][i];
		var orderId = data['id']; 
		var $tbody = $('#edit-order-item-table').find('tbody');
		var buttonHtml = '<button onclick="deleteItem(' + tmpe + ')">Delete</button>'
		var row = '<tr id="row' + tmpe + '">'
		+ '<td> <div class="form-group"><input type="text" class="form-control" name="barcode' + tmpe + '" id="barcode' + tmpe + '" value="' + e.barcode +  '" ></div> </td>'
		+ '<td> <div class="form-group"><input type="number" class="form-control" name="quantity' + tmpe + '" id="quantity' + tmpe + '" value="' + e.quantity +  '"></div> </td>'
		+ '<td> <div class="form-group"><input type="number" class="form-control" name="sellingPrice' + tmpe + '" id="sellingPrice' + tmpe + '" value="' + e.sellingPrice +  '"></div> </td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '<td> <div class="form-group"><input type="hidden" class="form-control" name="orderItemId' + tmpe + '" id="orderItemId' + tmpe + '" value="' + e.orderItemId + '"></input>'
		+ '<td> <div class="form-group"><input type="hidden" class="form-control" name="orderId" id="orderId" value="' + orderId + '"></input>'
		+ '</tr>';
		
		$tbody.append(row);
		tmpe=tmpe+1;
	}

}

function openCreateOrderModel(){
	$('#create-order-modal').modal('toggle');
}

function addItemInList(){
	displayOrderItemList();
}


function convertToArrayOfObjectToCreate(data){
	var serialized = data.serializeArray();
	let arr = []
	for(let i=0;i<serialized.length;i+=3){
			let obj = {};
			console.log(serialized[i].value);
			console.log(serialized[i+1].value);
			console.log(serialized[i+2].value);
			obj['barcode'] = serialized[i].value;
			obj['quantity'] = serialized[i+1].value;
			obj['sellingPrice'] = serialized[i+2].value;
			arr.push(obj);
	}
	return JSON.stringify(arr);
}


function convertToArrayOfObjectToUpdate(data){
	var serialized = data.serializeArray();
	let arr = []
	for(let i=0;i<serialized.length;i+=5){
			let obj = {};
			console.log(serialized[i].value);
			console.log(serialized[i+1].value);
			console.log(serialized[i+2].value);
			console.log(serialized[i+3].value);
			obj['barcode'] = serialized[i].value;
			obj['quantity'] = serialized[i+1].value;
			obj['sellingPrice'] = serialized[i+2].value;
			obj['orderItemId'] = serialized[i+3].value;
			arr.push(obj);
	}
	return JSON.stringify(arr);
}

function cancelOrder(){
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();	
	$('#create-order-modal').modal('hide');
}

function cancelUpdate(){
	var $tbody = $('#edit-order-item-table').find('tbody');
	$tbody.empty();
	$('#edit-order-modal').modal('hide');
}

function addIteminEditForm(){
	var $tbody = $('#edit-order-item-table').find('tbody');
	
	var buttonHtml = '<button onclick="deleteItem(' + tmpe + ')">Delete</button>'
	var row = '<tr id="row' + tmpe + '">'
	+ '<td> <div class="form-group"><input type="text" class="form-control" name="barcode' + tmpe + '" id="barcode' + tmpe + '" value="" ></div> </td>'
	+ '<td> <div class="form-group"><input type="number" class="form-control" name="quantity' + tmpe + '" id="quantity' + tmpe + '" value=""></div> </td>'
	+ '<td> <div class="form-group"><input type="number" class="form-control" name="sellingPrice' + tmpe + '" id="sellingPrice' + tmpe + '" value=""></div> </td>'
	+ '<td>' + buttonHtml + '</td>'
	+ '<td> <div class="form-group"><input type="hidden" class="form-control" name="orderItemId' + tmpe + '" id="orderItemId' + tmpe + '" value="0"></input>'
	+ '<td> <div class="form-group"><input type="hidden" class="form-control" name="orderId" id="orderId" value="' + orderId + '"></input>'
	+ '</tr>';
	
	$tbody.prepend(row);
	tmpe=tmpe+1;
	
}


//INITIALIZATION CODE
function init(){
	$('#create-order-button').click(openCreateOrderModel);
    $('#add-item').click(addItemInList);
	$('#create-order').click(createOrder);
	$('#cancel-order').click(cancelOrder);
	$('#update-order').click(updateOrder);
	$('#cancel-update').click(cancelUpdate);
	$('#edit-add-item').click(addIteminEditForm);
}

$(document).ready(init);
$(document).ready(getOrderList);

