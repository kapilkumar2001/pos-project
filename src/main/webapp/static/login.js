function loginErrorMessage(){
    console.log("message: ");
    var message = $('.error-message').find('span').text();
    console.log(message);
    if(message!=""){
        showError(message);
    }
}

function init(){
	$("login").click(loginErrorMessage);
}

$(document).ready(init);