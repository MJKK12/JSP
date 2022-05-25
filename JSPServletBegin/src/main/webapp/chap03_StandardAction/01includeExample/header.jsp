<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
	// 	   /JSPServletBegin
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<!-- Required meta tags -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

	<title>부트스트랩 페이지 링크 예제</title>
	
	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="<%= ctxPath %>/bootstrap-4.6.0-dist/bootstrap-4.6.0-dist/css/bootstrap.min.css" type="text/css">

	<!-- Font Awesome 5 Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

	<!-- 직접 만든 CSS -->
	<link rel="stylesheet" href="<%= ctxPath %>/chap03_StandardAction/01includeExample/css/mycss.css">
</head>
<body>
	<div class="container pt-2">
	
	<!-- 상단 네비게이션 시작 -->
	<nav class="navbar navbar-expand-lg bg-dark navbar-dark">
		<!-- 표준 탐색 모음은 .navbar 클래스를 사용하여 생성되고 반응형 축소 클래스(.navbar-expand-xl|lg|md|sm)가 뒤따릅니다. -->   
        <!-- Large(lg) ==> 992px 이상  1200px미만. navbar-expand-lg 은 width 가 991px 되어지는 순간 nav 이 축소되어진다. -->	  
	  	
	  	<!-- Brand/logo --> <!-- Font Awesome 5 Icons -->
	  	<a class="navbar-brand" href="#"><i class="fab fa-windows fa-2x"></i></a> <!-- 아이콘은 windows 로 하겠다. -->

		<!-- 디바이스 크기가 휴대폰 크기로 됐을 경우에는 아코디언 같은 Navigation Bar 만들기 -->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
          <span class="navbar-toggler-icon"></span>
        </button>
      
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto"> <!-- .mr-auto 는 css 의  margin-right: auto; 임. 즉, 가로축 미사용 공간 너비를 우축 바깥 여백에 할당한다는 것임. -->
            <li class="nav-item">
              <a class="nav-link menufont-size" href="index.jsp">Home</a>
            </li>
            <li class="nav-item">
           	  <a class="nav-link menufont-size" href="board.jsp">게시판</a>
           	</li>
           	<li class="nav-item">
              <a class="nav-link menufont-size" href="login.jsp">로그인</a>
           	</li>
           	<li class="nav-item">
           	  <a class="nav-link menufont-size" href="register.jsp">회원가입</a>
           	</li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle menufont-size" href="#" id="navbarDropdown" data-toggle="dropdown">
                Dropdown
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="#">Action</a>
                <a class="dropdown-item" href="#">Another action</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#">Something else here</a>
              </div>
            </li>
          </ul>
          <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
          </form>
        </div>		
	</nav>
	<!-- 상단 네비게이션 끝 -->