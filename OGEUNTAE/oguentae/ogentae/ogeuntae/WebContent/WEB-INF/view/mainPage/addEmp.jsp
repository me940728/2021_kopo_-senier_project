<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String aemail= (String)session.getAttribute("aemail");
    
    if(aemail == null){
    	response.sendRedirect("/user/sessioCheck.do");	
    }
    
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>add EMP</title>
<link
      rel="stylesheet"
      href="https://fonts.googleapis.com/css?family=Roboto:400,700"
    />
    <!-- https://fonts.google.com/specimen/Roboto -->
    <link rel="stylesheet" href="/resources/css/fontawesome.min.css" />
    <!-- https://fontawesome.com/ -->
    <link rel="stylesheet" href="/resources/jquery-ui-datepicker/jquery-ui.min.css" type="text/css" />
    <!-- http://api.jqueryui.com/datepicker/ -->
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css" />
    <!-- https://getbootstrap.com/ -->
    <link rel="stylesheet" href="/resources/css/templatemo-style.css">
    <!--
	Product Admin CSS Template
	https://templatemo.com/tm-524-product-admin
	-->
</head>
       <div>
		<!-- 오근태 탑 탑!!-->
		<%@ include file="/WEB-INF/view/mainPage/oTop.jsp"%>
		<!-- 탑 영역 끝-->
	   </div>
<body id="reportsPage">

	   

    <div class="container tm-mt-big tm-mb-big">
      <div class="row">
        <div class="col-xl-9 col-lg-10 col-md-12 col-sm-12 mx-auto">
          <div class="tm-bg-primary-dark tm-block tm-block-h-auto">
            <div class="row">
              <div class="col-12">
                <h2 class="tm-block-title d-inline-block">직원 정보</h2>
              </div>
            </div>
            <div class="row tm-edit-product-row">
              <div class="col-xl-6 col-lg-6 col-md-12">
                  <div class="form-group mb-3">
                  
            <form action="/user/addEmpProc.do" class="tm-edit-product-form" method="post" enctype="multipart/form-data">        
                    <label
                      for="empno"
                      >사원번호
                    </label>
                    <input
                      id="empno"
                      name="empno"
                      type="text"
                      class="form-control validate"
                      required
                    />
             <br>
                   <label
                      for="ename"
                      >이름
                    </label>
                     <input
                      id="ename"
                      name="ename"
                      type="text"
                      class="form-control validate"
                      required
                    />
              <br>
                     <label
                      for="addrs"
                      > 주소
                    </label>
                     <input
                      id="addrs"
                      name="addrs"
                      type="text"
                      class="form-control validate"
                      required
                    />
                  </div>
              
                  <div class="form-group mb-3">
                    <label
                      for="category"
                      >성별</label
                    >
                    <select
                      class="custom-select tm-select-accounts"
                      id="sex"
                      name = "sex"
                    >
                      <option selected>성별 선택</option>
                      <option value="1">남자</option>
                      <option value="2">여자</option>
                    </select>
                  </div>
                  <div class="row">
                      <div class="form-group mb-3 col-xs-12 col-sm-6">
                          <label
                            for="expire_date"
                            >생년월일
                          </label>
                          <input
                            id="hiredate"
                            name="bday"
                            type="text"
                            class="form-control validate"
                            data-large-mode="true"
                          />
                        </div>

                  </div>
                  
              </div>
              
              <div class="col-xl-6 col-lg-6 col-md-12 mx-auto mb-4">
                <div class="tm-product-img-dummy mx-auto">
                
				        <!-- 상품명 등록(추후 제목으로 표시된다) -->
				            <label for="img"></label>
				            <input type="file" id="img" name="fileUpload" required/>
				            <div class="select_img"><img src=""/></div>
        
                </div>
                
                <!-- 이거 눌러서 학습 시키는 기능 엮기 -->
                <div class="custom-file mt-3 mb-3">
                  <input id="fileInput" type="file" style="display:none;" />
                  <input
                    type="button"
                    class="btn btn-primary btn-block mx-auto"
                    value="직원 얼굴 학습 시키기"
                    name="emp"
                    onclick="empnoSend()"
                  />
                </div>
 <!--"window.open('imgStorage.do','이미지 학습','width=700,height=700,location=no,status=no,scrollbars=yes');"   -->
                
              <div class="row tm-edit-product-row">
              <div class="col-xl-6 col-lg-6 col-md-12">
                  <div class="form-group mb-3">
                  
                 <label
                      for="eemail" 
                      > 이메일
                    </label>
                     <input
                      id="eemail"
                      name="eemail"
                      type="text"
                      class="form-control validate"
                      required style="width: 350px"
                    />
                    </div>
                </div>
             </div>   
              </div>
              <div class="col-12">
                <button type="submit" class="btn btn-primary btn-block text-uppercase">직원 등록 완료</button>
              </div>
            </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <footer class="tm-footer row tm-mt-small">
        <div class="col-12 font-weight-light">
          <p class="text-center text-white mb-0 px-4 small">
            Copyright &copy; <b>2018</b> All rights reserved. 
            
            Design: <a rel="nofollow noopener" href="https://templatemo.com" class="tm-footer-link">Template Mo</a>
        </p>
        </div>
    </footer> 

    <script src="/resources/js/jquery-3.3.1.min.js"></script>
    <!-- https://jquery.com/download/ -->
    <script src="/resources/jquery-ui-datepicker/jquery-ui.min.js"></script>
    <!-- https://jqueryui.com/download/ -->
    <script src="/resources/js/bootstrap.min.js"></script>
    <!-- https://getbootstrap.com/ -->
    <script>
      $ = jquery;
      $(function() {
        $("#expire_date").datepicker();
      });
    </script>
    
    <!-- 직원 얼굴 학습 누르면 실행 되는 메서드-->
    <script>
    function empnoSend() {
      var empno = document.getElementById("empno").value;
  	  var win = window.open('imgStorage.do?empno='+empno,'이미지 학습','width=700,height=700,location=no,status=no,scrollbars=yes');
  	 
  	  <!-- 태그 내에 있는 값중에 id가 empno 곳의 값을 가쟈와서 변수에 담는 코드-->
	};
    </script>
    <!--사진 저장 자바스크립트-->
            <script>
            $("#img").change(function() {
                if(this.files && this.files[0]) {
                var reader = new FileReader;
                reader.onload = function(data) {
                    $(".select_img img").attr("src",data.target.result).width(100);
                }
                reader.readAsDataURL(this.files[0]);
            }
            });
        </script>
</html>