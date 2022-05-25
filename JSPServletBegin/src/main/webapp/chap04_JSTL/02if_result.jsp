<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    

<%-- JSTL(JSP Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>if 를 사용하여 전송된 값을 비교한 결과물 출력하기</title>
</head>
<body>

	<c:if test="${param.first == param.second}">	<!-- test 가 조건절이다. -->
		${param.first} 와 ${param.second} 는 <span style="color: blue;">같습니다.</span>
	</c:if> 	

	<c:if test="${param.first != param.second}">	<!-- test 가 조건절이다. -->
		${param.first} 와 ${param.second} 는 <span style="color: red;">같지 않습니다.</span>
	</c:if> 	

	<hr style="border: solid 1px red;">
	
	<!-- eq:같다 -->
	<c:if test="${param.first eq param.second}">	<!-- test 가 조건절이다. -->
		${param.first} 와 ${param.second} 는 <span style="color: blue;">같습니다.</span>
	</c:if> 	

	<!-- ne:not equal -->
	<c:if test="${param.first ne param.second}">	<!-- test 가 조건절이다. -->
		${param.first} 와 ${param.second} 는 <span style="color: red;">같지 않습니다.</span>
	</c:if> 	
	
	<hr style="border: solid 1px blue;">
	
	<c:if test="${empty param.third}">	<!-- test 가 조건절이다. -->
		세번째 입력란은 <span style="color: pink; background-color: navy;">입력하지 않으셨습니다.</span>
	</c:if> 	
	
	<c:if test="${not empty param.third}">	<!-- test 가 조건절이다. -->
		세번째 입력란은 <span style="color: green;">입력하셨습니다.</span>
	</c:if> 	
		
	<c:if test="${!empty param.third}">	<!-- test 가 조건절이다. -->
		세번째 입력란은 <span style="color: purple;">입력하셨습니다.</span>		
	</c:if> 	
	
		
</body>
</html>