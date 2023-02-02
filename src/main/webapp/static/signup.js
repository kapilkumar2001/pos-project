function loginButtonShow(){
    document.getElementById('navbarNavOut').innerHTML = '';
    let row = '<ul class="navbar-nav ml-auto"><li class="nav-item"><a class="btn btn-primary" th:href="@{/site/login}">Login</a></li>';
    document.getElementById('navbarNavOut').innerHTML += row;
    console.log(document.getElementById('navbarNavOut').innerHTML);
}

function init(){
	loginButtonShow();
}

$(document).ready(init);