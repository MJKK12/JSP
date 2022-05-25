<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<!DOCTYPE html>
<html>
<head>

<title>:::HOMEPAGE:::</title>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 5 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/style.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script> 

<script type="text/javascript">
	
	$(document).ready(function(){
		
		let side_html = "";
		let content_html = ""; 
		for(var i=0; i<15; i++) {
			side_html += (i+1)+".사이드 내용물<br/>";
			content_html += (i+1)+".content 내용물 입니다<br/>";
		}
		
		$("#sidecontent").html(side_html);
		$("#maincontent").html(content_html);
		
	});

</script>

</head>
<body>

	<!-- 상단 네비게이션 시작 -->
	<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top mx-4">
		<!-- Brand/logo --> <!-- Font Awesome 5 Icons -->
		<a class="navbar-brand" href="#" style="margin-right: 10%;"><img src="<%= ctxPath %>/images/logo1.png" /></a>
		
		<!-- 아코디언 같은 Navigation Bar 만들기 -->
	    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
	      <span class="navbar-toggler-icon"></span>
	    </button>
		
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
		  <ul class="navbar-nav">
		     <li class="nav-item active">
		        <a class="nav-link menufont_size" href="<%= ctxPath %>/index.jsp">Home</a>
		     </li>
		     <li class="nav-item active">
		     	<a class="nav-link menufont_size" href="<%= ctxPath %>/member/memberform.jsp">회원가입</a>
		     </li>
			 <li class="nav-item">
			    <a class="nav-link menufont_size" href="<%= ctxPath %>/member/memberList.jsp">회원목록</a>
			 </li>
			 <li class="nav-item dropdown">
		        <a class="nav-link dropdown-toggle menufont_size text-primary" href="#" id="navbarDropdown" data-toggle="dropdown"> 
		           Dropdown                                <%-- .text-primary 는 글자색으로 파랑색임 --%>  
		        </a>
		        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
		           <a class="dropdown-item text-primary" href="#">Action</a>
		           <a class="dropdown-item text-primary" href="#">Another action</a>
		           <div class="dropdown-divider"></div>
		           <a class="dropdown-item text-primary" href="#">Something else here</a>
		        </div>
		     </li>
		  </ul>
		</div>
	</nav>
	<!-- 상단 네비게이션 끝 -->

    <hr style="background-color: gold; height: 1.2px; position: relative; top:75px; margin: 0 1.7%;"> 

    <div class="container-fluid" id="container" style="position: relative; top:85px; padding: 0.1% 2.5%;">
      
	  <div class="row">
		 <div class="col-md-3" id="sideinfo">
			<div style="height: 200px; text-align: left; padding: 20px;">
				2. 로그인/Tree/View
			</div>
			<div id="sidecontent" style="text-align: left; padding: 20px;"></div>
		 </div>
		 <div class="col-md-9" id="maininfo" align="center">
			<div id="maincontent"></div>
		 </div>
	  </div>
		
	  <div id="footer" class="row my-2">
		 <p class="col-md-12" style="text-align: center; padding: 3%;">
			 &copy; Company 2022
		 </p>
	  </div>
	
	</div>
</body>
</html>