function getLoginUrl(){
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/login";
}

function login(){
	if(($("#login-form input[name=email]").val()==="") || ($("#login-form input[name=password]").val()==="")){
		showError("Please fill all the fields");
		return;
    }
	let form = $("#login-form");
	let data = form.serialize();
	let url = getLoginUrl();
	$.ajax({
	    url: url,
	    type: 'POST',
 	    data: data,
 	    headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },	  	   
	    success: function(response) {
            window.location.replace("http://localhost:9000/pos/ui/brands")
	    },
	    error: handleAjaxError
	});
}

function init(){
	$('#login-button').click(login);
}

$(document).ready(init);