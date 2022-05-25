<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오늘의 메뉴</title>

<link rel="stylesheet" type="text/css" href="css/style.css">

</head>
<body>

	<h3>오늘의 메뉴</h3>
	<ol>
		<li>햄버거 5,000원</li>
		<li>짜장면 4,000원</li>
		<li>짬뽕  5,500원</li>
		<li>탕수육 7,000원</li>
	</ol>

	<div id="today">
		현재시각 : <%@include file="03_1_todayTime.jsp" %>
	</div>
	
</body>
</html>