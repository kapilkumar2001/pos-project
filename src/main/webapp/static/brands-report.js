function getBrandsUrl(){
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getBrands(){
	let url = getBrandsUrl();
	$.ajax({
	   url: url,
	   type: "GET",
	   success: function(data) {
	   		displayBrands(data);  
	   },
	   error: handleAjaxError
	});
}

function displayBrands(data){
	let thead = $("#brand-report-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col'>S.No.</th> <th scope='col'>Brand</th> <th scope='col'>Category</th> </tr>";
	thead.append(header);

	let tbody = $("#brand-report-table").find("tbody");
	tbody.empty();
	let serialNo = 1;
	data = data.reverse();

	for(let i in data){
		let e = data[i];
		let row = "<tr>"
		+ "<td>" + serialNo + "</td>"
		+ "<td>" + e.brand + "</td>"
		+ "<td>"  + e.category + "</td>"
		+ "</tr>";
        tbody.append(row);
		serialNo+=1;
	}
}

function getBrandsReport(){
	let url = getBrandsUrl();

	$.ajax({
	  url: url,
	  type: "GET",
	  success: function(data) { 
		data = data.reverse();
        let headers = "Brand	Category\n"; 
        let tsv = "";
        tsv += headers
		
		for(row of data){
			tsv+=(row.brand + "	" + row.category);
			tsv+="\n";
		}  

		const date = new Date();
		let day = date.getDate();
		let month = date.getMonth() + 1;
		let year = date.getFullYear();
		let currentDate = `${day}-${month}-${year}`;

        let hiddenElement = document.createElement("a");  
        hiddenElement.href = "data:text/tsv;charset=utf-8," + encodeURI(tsv);  
        hiddenElement.target = "_blank";  
        hiddenElement.download = "brands-report-" + currentDate + ".tsv";  
        hiddenElement.click();  
		hiddenElement.remove();
	  },
	  error: handleAjaxError
	});
}

function init(){
    getBrands()
    $("#download-tsv-brands-report").click(getBrandsReport);
}

$(document).ready(init);