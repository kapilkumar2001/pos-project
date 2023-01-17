function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

//BUTTON ACTIONS

function createOrder(){
	
   
	var $form = $("#order-list-form");
	var json = convertToArrayOfObject($form);

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

			// $("#order-item-table").remove();		
			$('#create-order-modal').modal('hide');
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
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.time + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

// add items to the order list
var tmp =0;
function displayOrderItemList(){
	
	var $tbody = $('#order-item-table').find('tbody');
	var row = '<tr>'
	+ '<td> <div class="form-group"><input type="text" class="form-control" name="barcode' + tmp + '" id="barcode' + tmp + '" placeholder="enter barcode"></div> </td>'
	+ '<td> <div class="form-group"><input type="number" class="form-control" name="quantity' + tmp + '" id="quantity' + tmp + '" placeholder="enter quantity"></div> </td>'
	+ '<td> <div class="form-group"><input type="number" class="form-control" name="sellingPrice' + tmp + '" id="sellingPrice' + tmp + '" placeholder="enter price"></div> </td>'
	+ '</tr>';
	// console.log(row);
	$tbody.prepend(row);
	tmp=tmp+1;


	// var table = document.getElementById("order-item-table");
	// var row = table.insertRow(0);
	// row.innerHTML = '<tr>'
	// + '<td> <div class="form-group"><input type="text" class="form-control" name="barcode' + tmp + '" id="barcode' + tmp + '" placeholder="enter barcode"></div> </td>'
	// + '<td> <div class="form-group"><input type="number" class="form-control" name="quantity' + tmp + '" id="quantity' + tmp + '" placeholder="enter quantity"></div> </td>'
	// + '<td> <div class="form-group"><input type="number" class="form-control" name="sellingPrice' + tmp + '" id="sellingPrice' + tmp + '" placeholder="enter price"></div> </td>'
	// + '</tr>';
	// tmp = tmp+1;
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
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


function openCreateOrderModel(){
	$('#create-order-modal').modal('toggle');
}

function addItemInList(){
	displayOrderItemList();
}

function createOrderWithList(){
	createOrder();
}

function convertToArrayOfObject(data){
	var serialized = data.serializeArray();
	let arr = []
	//Convert to array of object
	for(let i=0;i<serialized.length;i+=3){
		let obj = {};
		obj['barcode'] = serialized[i].value;
		obj['quantity'] = serialized[i+1].value;
		obj['sellingPrice'] = serialized[i+2].value;
		arr.push(obj);
	}
	return JSON.stringify(arr);
}

function cancelOrder(){
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	// $("#order-item-table").remove();		
	$('#create-order-modal').modal('hide');
}


//INITIALIZATION CODE
function init(){
	$('#create-order-button').click(openCreateOrderModel);
    $('#add-item').click(addItemInList);
	$('#create-order').click(createOrderWithList);
	$('#cancel-order').click(cancelOrder);
}

$(document).ready(init);
$(document).ready(getOrderList);

