<%@ page import="poly.util.CmmUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <script src="/resources/js/jquery-3.3.1.min.js"></script>
    <!-- https://jquery.com/download/ -->
    <script src="/resources/js/moment.min.js"></script>
    <!-- https://momentjs.com/ -->
    <script src="/resources/js/Chart.min.js"></script>
    <!-- http://www.chartjs.org/docs/latest/ -->
    <script src="/resources/js/bootstrap.min.js"></script>
    <!-- https://getbootstrap.com/ -->
    <script src="/resources/js/tooplate-scripts.js"></script>
<body>
    <nav class="navbar navbar-expand-xl">
            <div class="container h-100">
                <a class="navbar-brand" href="opencvIndex.do"> 
                    <h1 class="tm-site-title mb-0">O-GeunTae</h1>
                </a>
                <button class="navbar-toggler ml-auto mr-0" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
              <i class="fas fa-bars tm-nav-icon"></i>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mx-auto h-100">
                        <li class="nav-item dropdown">

                            <a class="nav-link dropdown-toggle" href="/opencvIndex.do" id="navbarDropdown" role="button" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-cog"></i>
                                <span>
                                    대쉬보드
                                </span>
                            </a>
                        </li>
                         <li class="nav-item">
                            <a class="nav-link active" href="imgCheckPage.do">
                                <i class="fas fa-tachometer-alt"></i>
                                출근체크
                                <span class="sr-only">(current)</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="empMange.do">
                                <i class="far fa-user"></i>
                                직원관리
                            </a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="sendMail.do">
                                <i class="far fa-file-alt"></i>
                                메일발송
                            </a>
                        </li>
<!--                         <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-cog"></i>
                                <span>
                                    프로그램관리 <i class="fas fa-angle-down"></i>
                                </span>
                            </a> -->
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="#">Profile</a>
                                <a class="dropdown-item" href="#">Billing</a>
                                <a class="dropdown-item" href="#">Customize</a>
                            </div>
                        </li>
                    </ul>
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link d-block" href="user/logOut.do">
                                <b>로그아웃</b>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
</body>
</html>