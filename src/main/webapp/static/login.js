function getLoginUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/login";
}

function login(){
	var $form = $("#login-form");
	var data = $form.serialize();
	var url = getLoginUrl();
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
	return false;
}


function init(){
	$('#login-button').click(login);
}

$(document).ready(init);