function getInventoryReportUrl() {
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory/report/";
}

function getInventory() {
	let url = getInventoryReportUrl();

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			displayInventory(data);
		},
		error: handleAjaxError
	});
}

function displayInventory(data) {
	let thead = $("#inventory-report-table").find("thead");
	thead.empty();
	let header = "<tr> <th scope='col'>S.No.</th> <th scope='col'>Brand</th> <th scope='col'>Category</th> <th scope='col'>Quantity</th> </tr>";
	thead.append(header);

	let tbody = $("#inventory-report-table").find("tbody");
	tbody.empty();
    let tmp = 1;
	
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

	for (let i in data) {
		let e = data[i];
		let row = "<tr>"
			+ "<td>" + tmp + "</td>"
			+ "<td>" + e.brand + "</td>"
			+ "<td>" + e.category + "</td>"
			+ "<td>" + e.quantity + "</td>"
			+ "</tr>";
		tbody.append(row);
        tmp=tmp+1;
	}
}

function downloadInventoryReport() {
	let url = getInventoryReportUrl();

	$.ajax({
		url: url,
		type: "GET",
		success: function (data) {
			
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

			let tsv = "Brand	Category	Quantity\n"; 

			for(row of data){
				tsv+=Object.values(row).join("	");
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
			hiddenElement.download = "inventory-report-" + currentDate + ".tsv";  
			hiddenElement.click();
			hiddenElement.remove();
		},
		error: handleAjaxError
	});
}

function init() {
	getInventory();
    $("#download-tsv-inventory-report").click(downloadInventoryReport);
}

$(document).ready(init);
