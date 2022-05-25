<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%-- JSTL(JSP Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 변수의 선언방법 --%>    
<c:set var="result" value="${param.firstNum * param.secondNum}" />	<!-- 결과값을 result 에 넣고 싶다. -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>두개의 수를 입력받아서 곱셈한 결과 출력하기</title>
</head>
<body>

	${param.firstNum}와 ${param.secondNum}의 곱은 ${result} 입니다.
	
</body>
</html>