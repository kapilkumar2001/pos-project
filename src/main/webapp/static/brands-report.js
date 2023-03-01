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

	data.sort((a, b) => {
		if (a.brand < b.brand) {
		  	return -1;
		} else if (a.brand > b.brand) {
		  	return 1;
		} else {
			if (a.category < b.category) {
				return -1;
			} else if (a.category > b.category) {
				return 1;
			} else {
				return 0;
			}
		}
	});

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

function downloadBrandsReport(){
	let url = getBrandsUrl();

	$.ajax({
		url: url,
		type: "GET",
		success: function(data) { 
			data.sort((a, b) => {
				if (a.brand < b.brand) {
					return -1;
				} else if (a.brand > b.brand) {
					return 1;
				} else {
					if (a.category < b.category) {
						return -1;
					} else if (a.category > b.category) {
						return 1;
					} else {
						return 0;
					}
				}
			});

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
	$("#download-tsv-brands-report").click(downloadBrandsReport);
}

$(document).ready(init);