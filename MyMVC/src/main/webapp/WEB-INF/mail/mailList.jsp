<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
    
<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>

<!DOCTYPE html>
<html>

<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
	
	#secondHeader {
		display: inline-block;
	}
	
	#delTrash, #readReceive {
		border: 0;
		background-color: white;
	}
	
	ul#secondHeaderGroup, .secondHeaderList {
		list-style-type: none;
		display: inline-block;
	}
	
  #mail_searchType, #searchWord, 
  #mail_serachWord, #mail_search,
  #btnSearch {
  	 display: inline-block;
  }	

<%-- 헤더부분 --%>
* {box-sizing: border-box;}

body { 
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.header {
  overflow: hidden;
  background-color: #f1f1f1;
  padding: 20px 10px;
}

.header a {
  float: left;
  color: black;
  text-align: center;
  padding: 12px;
  text-decoration: none;
  font-size: 18px; 
  line-height: 25px;
  border-radius: 4px;
}

.header a.logo {
  font-size: 25px;
  font-weight: bold;
}

.header a:hover {
  background-color: #ddd;
  color: black;
}

.header a.active {
  background-color: dodgerblue;
  color: white;
}

.header-right {
  float: right;
}

@media screen and (max-width: 500px) {
  .header a {
    float: none;
    display: block;
    text-align: left;
  }
  
  .header-right {
    float: none;
  }
}

<%-- 사이드바 부분 --%>
body {
  margin: 0;
  font-family: "Lato", sans-serif;
}

.sidebar {
  margin: 0;
  padding: 0;
  width: 200px;
  background-color: #f1f1f1;
  position: fixed;
  height: 100%;
  overflow: auto;
}

.sidebar a {
  display: block;
  color: black;
  padding: 16px;
  text-decoration: none;
}
 
.sidebar a.active {
  background-color: #80c1ff;
  color: black;
}

.sidebar a:hover:not(.active) {
  background-color: #555;
  color: white;
}

div.content {
  margin-left: 200px;
  padding: 1px 16px;
  height: 1000px;
}

@media screen and (max-width: 700px) {
  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
  }
  .sidebar a {float: left;}
  div.content {margin-left: 0;}
}

@media screen and (max-width: 400px) {
  .sidebar a {
    text-align: center;
    float: none;
  }
}

</style>

<meta charset="UTF-8">
<title>받은메일함 목록</title>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 5 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/style.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script> 

<!-- jQueryUI 추가 -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.css" > 
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.js"></script>

</head>


<script type="text/javascript">

// function declaration 

// 검색 버튼 클릭시 동작하는 함수
	function goMailSearch() {
		
		
	
	}// end of function goMailSearch(){}-------------------------

</script>

<body>	
<%-- 헤더 부분 시작 --%>
<div class="header">
  <a href="#default" class="logo">BTSGroupWare</a>
  <div class="header-right">
    <a class="active" href="#home">Home</a>
  </div>
</div>
<%-- 헤더 부분 끝 --%>

<%-- 사이드바 부분 시작 --%>
<div class="sidebar">
  <a class="active" href="#home">홈</a>
  <a href="#writeMail">메일쓰기</a>
  <a href="#receiveMail">받은메일함</a>
  <a href="#importantMail">중요메일함</a>
  <a href="#temporaryMail">임시보관함</a>
  <a href="#reserveMail">예약메일함</a>
  <a href="#recyclebinMail">휴지통</a>  
</div>

<%-- 사이드바 부분 끝 --%>


<%-- 받은 메일함 목록 보여주기 --%>	
<div class="content">	
	<div class="row bg-title" style="border-bottom: solid .025em gray;">	
		<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
			<h4 class="page-title" style="color: black;">받은 메일함</h4>
		</div>
		
		<form name="goReceiveListSelectFrm" style="display: inline-block;" style="text-align: right;">		
			<div id="mail_searchType">
				<select class="form-control" id="searchType" name="searchType" style="">
					<option value="mail_subject" selected="selected">제목</option>
					<option value="emp_name">사원명</option>
				</select>
			</div>
			
			<div id="mail_serachWord">
				<input id="searchWord" name="searchWord" type="text" class="form-control" placeholder="내용을 입력하세요.">
			</div>
			
			<button type="button" id="btnSearch" class="btn btn-secondary" onclick="gomailSearch()">
				<i class="fa fa-search" aria-hidden="true" style="font-size:15px"></i>
			</button>
		</form>				
	</div>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="white-box" style="padding-top: 5px;">
				<div id="secondHeader">
					<ul id="secondHeaderGroup" style="padding: 0px;">
						<li class="secondHeaderList">
							<button type="button" id="delTrash" onclick="goMailDelTrash()">
							<i class="fa fa-trash-o fa-fw"></i>
								삭제
							</button>
						</li>
						<li class="secondHeaderList">
							<button type="button" id=readReceive onclick="goReadReceive()">
							<i class="fa fa-envelope-o fa-fw"></i>
								읽음
							</button>
						</li>
					</ul>
				</div>
				
				<div class="table-responsive" style="color: black;">
					<table>
						<thead>
							<tr>
								<th style="width: 40px;">
									<input type="checkbox" id="checkAll" />
								</th>
								<th style="width: 40px;">
									<span class="fa fa-star-o"></span>
								</th>
								<th style="width: 40px;">
									<span class="fa fa-paperclip"></span>
								</th>
								<th style="width: 70px;">사원명</th>
								<th style="width: 500px;">제목</th>
								<th style="width: 120px;">날짜</th>
							</tr>
						</thead>
						
						<tbody>
							<tr>
								<td style="width: 40px;">
									<input type="checkbox" id="checkAll" />
								</td>
								<td style="width: 40px;">
									<span class="fa fa-star-o"></span>
								</td>
								<td style="width: 40px;">
									<span class="fa fa-paperclip"></span>
								</td>							
								<td>김민정</td>
								<td>메일 제목 확인 JSP</td>
								<td>2022.04.25</td>
							</tr>						
							<tr>
								<td style="width: 40px;">
									<input type="checkbox" id="checkAll" />
								</td>
								<td style="width: 40px;">
									<span class="fa fa-star-o"></span>
								</td>
								<td style="width: 40px;">
									<span class="fa fa-paperclip"></span>
								</td>
								<td>김민정</td>
								<td>메일 제목 확인 JSP</td>
								<td>2022.04.25</td>
							</tr>	
							<tr>
								<td style="width: 40px;">
									<input type="checkbox" id="checkAll" />
								</td>
								<td style="width: 40px;">
									<span class="fa fa-star-o"></span>
								</td>
								<td style="width: 40px;">
									<span class="fa fa-paperclip"></span>
								</td>
								<td>김민정</td>
								<td>메일 제목 확인 JSP</td>
								<td>2022.04.25</td>
							</tr>								
						</tbody>
					</table>
				</div>	
				<%-- 페이징 란 --%>
				<div>
					<ul class="pagination" style="width: 70%; margin: 20px auto">
					  <li class="page-item"><a class="page-link" href="#">Previous</a></li>
					  <li class="page-item"><a class="page-link" href="#">1</a></li>
					  <li class="page-item"><a class="page-link" href="#">2</a></li>
					  <li class="page-item"><a class="page-link" href="#">3</a></li>
					  <li class="page-item"><a class="page-link" href="#">Next</a></li>
					</ul>
				</div>							
			</div>
		</div>	
	</div>
</div>
</body>
</html>