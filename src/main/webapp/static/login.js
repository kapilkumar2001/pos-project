function signupButtonShow(){
    document.getElementById('navbarNavOut').innerHTML = '';
    let row = '<ul class="navbar-nav ml-auto"><li class="nav-item"><a class="btn btn-primary" th:href="@{/site/signup}">Signup</a></li>';
    document.getElementById('navbarNavOut').innerHTML += row;
    console.log(document.getElementById('navbarNavOut').innerHTML);
}

function init(){
	signupButtonShow();
}

$(document).ready(init);