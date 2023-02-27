function getBrandsUrl(){
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function addBrand(){
	if(($("#brand-form input[name=brand]").val()==="") || ($("#brand-form input[name=category]").val()==="")){
			showError("Please fill all the fields");
			return;
	}

	let form = $("#brand-form");
	let json = toJson(form);
	let url = getBrandsUrl();

	$.ajax({
	    url: url,
	    type: "POST",
 	    data: json,
 	    headers: {
         	"Content-Type": "application/json"
        },	   
	    success: function(response) {
			showSuccess("Brand added succesfully!");
	   	 	getBrandsList();  
			$("#brand-form input[name=brand]").val("");
			$("#brand-form input[name=category]").val("");
			$("#add-brand-modal").modal("hide");
	    },
	    error: handleAjaxError
	});
}

function updateBrand(){
	if(($("#brand-edit-form input[name=brand]").val()==="") || ($("#brand-edit-form input[name=category]").val()==="")){
		showError("Please fill all the fields");
		return;
    }

	let id = $("#brand-edit-form input[name=id]").val();	
	let url = getBrandsUrl() + "/" + id;
	let form = $("#brand-edit-form");
	let json = toJson(form);

	$.ajax({
	  url: url,
	  type: "PUT",
	  data: json,
	  headers: {
      "Content-Type": "application/json"
      },	   
	  success: function(response) {
		showSuccess("Brand updated succesfully!");
	   	getBrandsList();  
			$("#edit-brand-modal").modal("hide");
	  },
	  error: handleAjaxError
	});
}

function getBrandsList(){
	let url = getBrandsUrl();

	$.ajax({
	  url: url,
	  type: "GET",
	  success: function(data) {
	  	displayBrandsList(data);  
	  },
	  error: handleAjaxError
	});
}

function displayBrandsList(data){
	let userRole = $(".user-role").find("span").text();
	
	let thead = $("#brands-table").find("thead");
	thead.empty();
	let header;
	if(userRole==="supervisor"){
		header = "<tr> <th scope='col'>S.No.</th> <th scope='col'>Brand</th> <th scope='col'>Category</th> <th scope='col' class='text-center'>Action</th> </tr>";
	} else{
		header = "<tr> <th scope='col'>S.No.</th> <th scope='col'>Brand</th> <th scope='col'>Category</th> </tr>";
	}
	thead.append(header);

	let tbody = $("#brands-table").find("tbody");
	tbody.empty();
	data = data.reverse();
	let serialNumber = 1;

	for(let i in data){
		let e = data[i];
		let buttonHtml = "";
		if(userRole==="supervisor"){
			buttonHtml += "<button onclick='displayEditBrand(" + e.id + ")' class='border-0 bg-transparent' data-toggle='tooltip' data-placement='bottom' title='Edit'><i class='far fa-edit text-dark'></i></button>"
		} 
		let row = "<tr>"
		+ "<td>" + serialNumber + "</td>"
		+ "<td>" + e.brand + "</td>"
		+ "<td>"  + e.category + "</td>"
		+ "<td class='text-center'>" + buttonHtml + "</td>"
		+ "</tr>";
		tbody.append(row);
		serialNumber+=1;
	}
	$("[data-toggle='tooltip']").tooltip()
}

function displayEditBrand(id){
	let url = getBrandsUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: "GET",
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
	$("#edit-brand-modal").modal("toggle");
}

function openAddBrandModal(){
	$("#add-brand-modal").modal("toggle");
}


// FILE UPLOAD METHODS
let fileData = [];
let errorData = [];
let processCount = 0;

function processData(){
	processCount = 0;
	fileData = [];
	errorData = [];
	$("#download-errors").remove();
	let file = $("#brands-file")[0].files[0];

    if($("#brands-file")[0].files.length===0){
		showError("Please Choose File");
		return;
	}

	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;

	if(fileData[0].brand===undefined || fileData[0].category===undefined){
		showError("Invalid file");
		return;
	}

	if(fileData.length>5000){
		showError("Data limit exceeded. Max data limit - 5000 rows");
		return;
	}

	if($("#upload-modal-data-row").length==0){
		let modalbody = $("#upload-brands-modal").find(".modal-body");
		let row = "<p id=\"upload-modal-data-row\"> Rows: <span id=\"rowCount\">0</span>, Processed: <span id=\"processCount\">0</span>, Errors: <span id=\"errorCount\">0</span></p>";
		modalbody.append(row);
	}

	uploadRows();
}

function uploadRows(){
	updateUploadDialog();

	if(processCount===fileData.length && errorData.length===0){
		$("#upload-brands-modal").modal("hide");
		showSuccess("Brands uploaded succesfully!");
		getBrandsList();
		return;
	}
	else if(processCount===fileData.length){
		let modalfooter = $("#upload-brands-modal").find(".modal-footer");
		let htmlButton = "<button type='button' class='btn btn-danger btn-sm mr-auto' id='download-errors' onclick='downloadErrors()'><i class='fa fa-download text-white mr-1'></i>Download Errors</button>";
		modalfooter.prepend(htmlButton);
		getBrandsList();
		return;
	}

	let row = fileData[processCount];
	processCount++;

	let json = JSON.stringify(row);
	let url = getBrandsUrl();
	
	$.ajax({
	   url: url,
	   type: "POST",
	   data: json,
	   headers: {
       	"Content-Type": "application/json"
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseJSON["message"];
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

function resetUploadDialog(){
	let file = $("#brands-file");
	file.val("");
	$("#brands-file-name").html("Choose File");

	processCount = 0;
	fileData = [];
	errorData = [];

	$("#upload-modal-data-row").remove();
	$("#download-errors").remove();
}

function updateUploadDialog(){
	$("#rowCount").html("" + fileData.length);
	$("#processCount").html("" + processCount);
	$("#errorCount").html("" + errorData.length);
}

function updateFileName(){
	let file = $("#brands-file");
	let fileName = file.val().split("\\")[2];
	$("#brands-file-name").html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$("#upload-brands-modal").modal("toggle");
}

function init(){
	getBrandsList()
	$("#add-brand-button").click(openAddBrandModal);
	$("#add-brand").click(addBrand);
	$("#update-brand").click(updateBrand);
	$("#upload-data").click(displayUploadData);
	$("#process-data").click(processData);
	$("#brands-file").on("change", updateFileName);
}

$(document).ready(init);

