function getSignUpUrl(){
	let baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/signup";
}

function signUp(){
	if(validateEmailandPassword()===false){
    return;
  }

	let form = $("#signup-form");
	let data = form.serialize();
	let url = getSignUpUrl();

	$.ajax({
	    url: url,
	    type: "POST",
 	    data: data,
 	    headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },	  	   
	    success: function(response) {
            window.location.replace("http://localhost:9000/pos/site/login");
	    },
	    error: handleAjaxError
	});
}

function validateEmailandPassword(){
  let mail = $("#signup-form input[name=email]").val();
  let password = $("#signup-form input[name=password]").val();

  if(mail==="" || password===""){
    showError("Please fill all the fields!");
    return false;
  }

  let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

  if(!mail.match(mailformat)) {
    showError("Invalid email address!");
    return false;
  } 
  else if(password.length<6) {
    showError("Password should contain more than 6 characters")
    return false;
  }
  
  return true;
}

function showOrHidePassword() {
	$(this).toggleClass("fa-eye fa-eye-slash");
	var input = $($(this).attr("toggle"));

	if (input.attr("type") == "password") {
  	input.attr("type", "text");
	} else {
	  input.attr("type", "password");
	}
}

function init(){
	$("#signup-button").click(signUp);
  $(".toggle-password").click(showOrHidePassword);
}

$(document).ready(init);

