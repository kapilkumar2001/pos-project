function getBrandCategoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}


function addBrandCategory(event){
	var $form = $("#brandcategory-form");
	var json = toJson($form);
	var url = getBrandCategoryUrl();

	$.ajax({
	    url: url,
	    type: 'POST',
 	    data: json,
 	    headers: {
         	'Content-Type': 'application/json'
        },	   
	    success: function(response) {
			showSuccess("Brand added succesfully!");
	   	 	getBrandCategoryList();  
			$("#brandcategory-form input[name=brand]").val("");
			$("#brandcategory-form input[name=category]").val("");
			$("#add-brandcategory-modal").modal('hide');
	    },
	    
	    error: handleAjaxError
	});

	return false;
}

function updateBrandCategory(event){
	var id = $("#brandcategory-edit-form input[name=id]").val();	
	var url = getBrandCategoryUrl() + "/" + id;

	var $form = $("#brandcategory-edit-form");
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
	   		getBrandCategoryList();  
			$('#edit-brandcategory-modal').modal('hide');
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandCategoryList(){
	var url = getBrandCategoryUrl();
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandCategoryList(data);  
	   },
	   error: handleAjaxError
	});
}

function deleteBrandCategory(id){
	var url = getBrandCategoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandCategoryList();  
	   },
	   error: handleAjaxError
	});
}


//UI DISPLAY METHODS

function displayBrandCategoryList(data){
	var $tbody = $('#brandcategory-table').find('tbody');
	$tbody.empty();
	var role = $('.user-role').find('span').text();
	data = data.reverse();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '';
		console.log("role: " + role);
		if(role=="supervisor"){
			buttonHtml += '<button onclick="displayEditBrandCategory(' + e.id + ')" style=\'border: none;margin-right:8px; background-color:transparent\' data-toggle="tooltip" data-placement="bottom" title="Edit"><i class=\'far fa-edit\' style=\'font-size:18px;color:blue;\'></i></button>'
		} 
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	$('[data-toggle="tooltip"]').tooltip()
}

function displayEditBrandCategory(id){
	var url = getBrandCategoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandCategory(data);   
	   },
	   error: handleAjaxError
	});	
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

	var file = $('#brandcategoryFile')[0].files[0];
    if($('#brandcategoryFile')[0].files.length==0){
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
		var $modalbody = $('#upload-brandcategory-modal').find('.modal-body');
		var row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		$modalbody.append(row);
	}

	uploadRows();
}

function uploadRows(){
	updateUploadDialog();

	if(processCount==fileData.length && errorData.length==0){
		$('#upload-brandcategory-modal').modal('hide');

		showSuccess("Brands uploaded succesfully!");
		getBrandCategoryList();
		return;
	}
	else if(processCount==fileData.length){
		var $modalfooter = $('#upload-brandcategory-modal').find('.modal-footer');
		var htmlButton = "<button type=\'button\' class=\'btn btn-danger btn-sm mr-auto\' id=\'download-errors\' onclick=\"downloadErrors()\"><i class='fa fa-download' style='font-size:16px;color:white;padding-right: 4px;'></i>Download Errors</button>";
		$modalfooter.prepend(htmlButton);

		getBrandCategoryList();
		return;
	}

	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getBrandCategoryUrl();

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
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

function resetUploadDialog(){
	var $file = $('#brandcategoryFile');
	$file.val('');
	$('#brandcategoryFileName').html("Choose File");
	
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
	var $file = $('#brandcategoryFile');
	var fileName = $file.val();
	$('#brandcategoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brandcategory-modal').modal('toggle');
}

function displayBrandCategory(data){
	$("#brandcategory-edit-form input[name=brand]").val(data.brand);	
	$("#brandcategory-edit-form input[name=category]").val(data.category);	
	$("#brandcategory-edit-form input[name=id]").val(data.id);	
	$('#edit-brandcategory-modal').modal('toggle');
}

function OpenAddBrandCategoryModal(){
	$("#add-brandcategory-modal").modal('toggle');
}


function init(){
	getBrandCategoryList()
	$('#add-brandcategory-button').click(OpenAddBrandCategoryModal);
	$('#add-brandcategory').click(addBrandCategory);
	$('#update-brandcategory').click(updateBrandCategory);
	$('#refresh-data').click(getBrandCategoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#brandcategoryFile').on('change', updateFileName);
	$(function () {
		$('[data-toggle="tooltip"]').tooltip()
	})
}

$(document).ready(init);

