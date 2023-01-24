function getBrandCategoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandcategory";
}

//BUTTON ACTIONS
function addBrandCategory(event){
	//Set the values to update
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
	   		getBrandCategoryList();  
			$("#brandcategory-form input[name=brand]").val("");
			$("#brandcategory-form input[name=category]").val("");
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrandCategory(event){
	$('#edit-brandcategory-modal').modal('toggle');
	//Get the ID
	var id = $("#brandcategory-edit-form input[name=id]").val();	
	var url = getBrandCategoryUrl() + "/" + id;

	//Set the values to update
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
	   		getBrandCategoryList();   
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

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandcategoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getBrandCategoryUrl();

	//Make ajax call
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

//UI DISPLAY METHODS

function displayBrandCategoryList(data){
	var $tbody = $('#brandcategory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="displayEditBrandCategory(' + e.id + ')" style=\'border: none;margin-right:8px; background-color:transparent\'><i class=\'far fa-edit\' style=\'font-size:18px;color:black;\'></i></button>'
		buttonHtml += '<button onclick="deleteBrandCategory(' + e.id + ')" style=\'border: none; margin-left:8px; background-color:transparent\'><i class=\'fas fa-trash\' style=\'font-size:18px;color:black;\'></i></button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
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

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandcategoryFile');
	$file.val('');
	$('#brandcategoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
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


//INITIALIZATION CODE
function init(){
	$('#add-brandcategory').click(addBrandCategory);
	$('#update-brandcategory').click(updateBrandCategory);
	$('#refresh-data').click(getBrandCategoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandcategoryFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getBrandCategoryList);

