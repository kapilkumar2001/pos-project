function getBrandsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}

function addBrand(){
	if(($("#brand-form input[name=brand]").val()=="") || ($("#brand-form input[name=category]").val()=="")){
			showError("Please fill all the fields");
			return;
	}

	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandsUrl();
	$.ajax({
	    url: url,
	    type: 'POST',
 	    data: json,
 	    headers: {
         	'Content-Type': 'application/json'
        },	   
	    success: function(response) {
			showSuccess("Brand added succesfully!");
	   	 	getBrandsList();  
			$("#brand-form input[name=brand]").val("");
			$("#brand-form input[name=category]").val("");
			$("#add-brand-modal").modal('hide');
	    },
	    error: handleAjaxError
	});
	return false;
}

function updateBrand(){
	if(($("#brand-edit-form input[name=brand]").val()=="") || ($("#brand-edit-form input[name=category]").val()=="")){
		showError("Please fill all the fields");
		return;
    }

	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandsUrl() + "/" + id;
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	$.ajax({
	  url: url,
	  type: 'PUT',
	  data: json,
	  headers: {
      'Content-Type': 'application/json'
    },	   
	  success: function(response) {
		showSuccess("Brand updated succesfully!");
	   	getBrandsList();  
			$('#edit-brand-modal').modal('hide');
	  },
	  error: handleAjaxError
	});
	return false;
}

function getBrandsList(){
	var url = getBrandsUrl();
	console.log(url);
	$.ajax({
	  url: url,
	  type: 'GET',
	  success: function(data) {
	  	displayBrandsList(data);  
	  },
	  error: handleAjaxError
	});
}


// UI display methods

function displayBrandsList(data){
	var $tbody = $('#brands-table').find('tbody');
	$tbody.empty();
	var userRole = $('.user-role').find('span').text();
	data = data.reverse();
	let serialNumber = 1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '';
		if(userRole=="supervisor"){
			buttonHtml += '<button onclick="displayEditBrand(' + e.id + ')" style=\'border: none;margin-right:8px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
		} 
		var row = '<tr>'
		+ '<td>' + serialNumber + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
		serialNumber+=1;
	}
	$('[data-toggle="tooltip"]').tooltip()
}

function displayEditBrand(id){
	var url = getBrandsUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);   
	   },
	   error: handleAjaxError
	});	
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}

function openAddBrandModal(){
	$("#add-brand-modal").modal('toggle');
}


// FILE UPLOAD METHODS

var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
	processCount = 0;
	fileData = [];
	errorData = [];
	$('#download-errors').remove();
	var file = $('#brandsFile')[0].files[0];
    if($('#brandsFile')[0].files.length==0){
		showError("Please Choose File");
		return;
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
		showError("Data limit exceeded. Max data limit - 5000 rows");
		return;
	}
	if($('#upload-modal-data-row').length==0){
		var $modalbody = $('#upload-brands-modal').find('.modal-body');
		var row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		$modalbody.append(row);
	}
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
	if(processCount==fileData.length && errorData.length==0){
		$('#upload-brands-modal').modal('hide');
		showSuccess("Brands uploaded succesfully!");
		getBrandsList();
		return;
	}
	else if(processCount==fileData.length){
		var $modalfooter = $('#upload-brands-modal').find('.modal-footer');
		var htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download' style='font-size:16px;color:white;padding-right: 4px;'></i>Download Errors</button>";
		$modalfooter.prepend(htmlButton);
		getBrandsList();
		return;
	}

	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getBrandsUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseJSON['message'];
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

function resetUploadDialog(){
	var $file = $('#brandsFile');
	$file.val('');
	$('#brandsFileName').html("Choose File");

	processCount = 0;
	fileData = [];
	errorData = [];

	$('#upload-modal-data-row').remove();
	$('#download-errors').remove();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandsFile');
	var fileName = $file.val().split("\\")[2];
	$('#brandsFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brands-modal').modal('toggle');
}


function init(){
	getBrandsList()
	$('#add-brand-button').click(openAddBrandModal);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#brandsFile').on('change', updateFileName);
}

$(document).ready(init);

