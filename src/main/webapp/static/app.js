function toJson(form){
    let serialized = form.serializeArray();
    let s = '';
    let data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    let json = JSON.stringify(data);
    return json;
}

function handleAjaxError(response){
	response = JSON.parse(response.responseText);
  Swal.fire({
    icon: 'error',
    text: response.message,
  })
}

function showError(error){
  Swal.fire({
    icon: 'error',
    text: error,
  })
}

function showSuccess(message){
  Swal.fire({
    icon: 'success',
    text: message,
  })
}

function readFileData(file, callback){
	let config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}

function writeFileData(arr){
	let config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	let data = Papa.unparse(arr, config);
  let blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
  let fileUrl =  null;

  if (navigator.msSaveBlob) {
      fileUrl = navigator.msSaveBlob(blob, 'errors.tsv');
  } else {
      fileUrl = window.URL.createObjectURL(blob);
  }
  let tempLink = document.createElement('a');
  tempLink.href = fileUrl;
  tempLink.setAttribute('download', 'errors.tsv');
  tempLink.click(); 
}
