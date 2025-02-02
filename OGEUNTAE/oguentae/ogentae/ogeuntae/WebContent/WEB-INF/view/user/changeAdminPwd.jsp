<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    
    String sessionEmail = (String) session.getAttribute("sessionEmail");
    
    %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>CHANGE_ADMIN_PWD_PAGE</title>
    <link
      rel="stylesheet"
      href="https://fonts.googleapis.com/css?family=Roboto:400,700"
    />
    <!-- https://fonts.google.com/specimen/Open+Sans -->
    <link rel="stylesheet" href="/resources/css/fontawesome.min.css" />
    <!-- https://fontawesome.com/ -->
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
    <!-- https://getbootstrap.com/ -->
    <link rel="stylesheet" href="/resources/css/templatemo-style.css">
    <!--
	Product Admin CSS Template
	https://templatemo.com/tm-524-product-admin
	-->
</head>
<body>

 <nav class="navbar navbar-expand-xl">
  <div class="container h-100">
                <a class="navbar-brand" href="/user/login.do"> <!-- / 슬레쉬 배주면 경로 뒤에 더해서 넘어가서 에러 뜸 -->
                    <h1 class="tm-site-title mb-0">O-GeunTae</h1>
                </a>
                <button class="navbar-toggler ml-auto mr-0" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
              <i class="fas fa-bars tm-nav-icon"></i>
                </button>
   </div>
 </nav>

    <div class="container tm-mt-big tm-mb-big">
      <div class="row">
        <div class="col-12 mx-auto tm-login-col">
          <div class="tm-bg-primary-dark tm-block tm-block-h-auto">
            <div class="row">
              <div class="col-12 text-center">
                <h2 class="tm-block-title mb-4">"<%=sessionEmail %>" 님 의 정보 변경</h2>
              </div>
            </div>
            <div class="row mt-2">
              <div class="col-12">
              <!-- 로그인을 위한 데이터를 넘기기 위한 form 액션 -->
                <form name="f" action="/user/changeAdminPwdProc.do" method="post" class="tm-login-form" onsubmit="return doChangeUserCheck(this);">
                  <div class="form-group">
                  
                    <label for="aEmail">바꿀 비밀번호</label> 
                    <input
                      name="password"
                      type="password"
                      class="form-control validate"
                      id="password"
                      value=""
                      required
                    />
                  </div>
                   <div class="form-group">
                  
                    <label for="aEmail">비밀번호 확인</label> 
                    <input
                      name="passwordCheck"
                      type="password"
                      class="form-control validate"
                      id="passwordCheck"
                      value=""
                      required
                    />
                  </div>
                  
                  
                  
                  <div class="form-group mt-4">
                    <button
                      type="submit"
                      class="btn btn-primary btn-block text-uppercase"
                    >
                      확인
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <footer class="tm-footer row tm-mt-small">
      <div class="col-12 font-weight-light">
<!--         <p class="text-center text-white mb-0 px-4 small">
          Copyright &copy; <b>2018</b> All rights reserved. 
          
          Design: <a rel="nofollow noopener" href="https://templatemo.com" class="tm-footer-link">Template Mo</a>
        </p> -->
      </div>
    </footer>
<script type="text/javascript">
function doChangeUserCheck(f) {

	if (password.value != passwordCheck.value) {
		alert("비밀번호가 서로 다릅니다.");
		f.password.focus();
		return false;
	}

}

</script>
    <script src="/resources/js/jquery-3.3.1.min.js"></script>
    <!-- https://jquery.com/download/ -->
    <script src="/resources/js/bootstrap.min.js"></script>
    <!-- https://getbootstrap.com/ -->
</body>
</html>