function getSignUpUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/signup";
}

function signUp(){
	if(validateEmailandPassword()==false){
        return;
    }

	var $form = $("#signup-form");
	var data = $form.serialize();
	var url = getSignUpUrl();
	$.ajax({
	    url: url,
	    type: 'POST',
 	    data: data,
 	    headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },	  	   
	    success: function(response) {
            window.location.replace("http://localhost:9000/pos/site/login")
            showSuccess("User registered successfully. Login now!");
	    },
	    error: handleAjaxError
	});
	return false;
}

function validateEmailandPassword(){
    let mail = $("#signup-form input[name=email]").val();
    let password = $("#signup-form input[name=password]").val();

    console.log(mail + " : " + password);
    var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(mail.match(mailformat)){
        console.log("valid");
    }
    else{
        showError("Invalid email address!");
        return false;
    }
    if(password.length<6){
        showError("Password should contain more than 6 characters")
        return false;
    }
    return true;
}

function init(){
	$('#signup-button').click(signUp);
}

$(document).ready(init);

