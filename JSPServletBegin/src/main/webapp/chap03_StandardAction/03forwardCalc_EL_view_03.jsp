<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계산된 결과값을 보여주는 곳</title>
</head>
<body>
	<h3>계산된 결과값(EL 을 사용한 것) -3</h3>
		${requestScope.firstno}부터 ${requestScope.secondno}까지의 합은?<br/>
		결과값 : <span style="color:green; font-weight:bold;">${requestScope.hab}</span>

	<br/><br/>

	<h3>계산된 결과값(EL 을 사용한 것) -4</h3>
		${firstno}부터 ${secondno}까지의 합은?<br/>
		결과값 : <span style="color:pink; font-weight:bold;">${hab}</span>
</body>
</html>