<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%-- JSTL(JSP Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>choose 를 사용하여 전송되어져 온 주민번호를 가지고 성별을 파악한 결과물 출력하기</title>
</head>
<body>
	<c:set var="jubun" 		value="${param.jubun}" />				<!-- execute 파일에서 name 이 jubun. 입력받은 주번 -->
	<c:set var="len"   		value="${fn:length(jubun)}" />			<!-- jubun 의 문자열 길이를 len에 찍는다. -->
	<c:set var="genderno" 	value="${fn:substring(jubun,6,7)}" /> 	<!-- "9210152123456" 에서 "2" 를 가져온다. -->

	주민번호 : ${jubun}<br/>
	주민번호 문자열 길이 : ${len}<br/>
	성별을 나타내는 숫자 : ${genderno}<br/>

	<c:if test="${len eq 0}" >
		<span style="color: red;">주민번호를 입력하지 않으셨습니다.!!</span>
	</c:if>
	
	<c:if test="${len ne 0 and len ne 13}">	<!-- 숫자가 들어오긴 했지만(ne 0) 주민번호 길이가 13이 아니다. -->
		<span style="color: red;">주민번호의 길이가 맞지 않습니다.!!</span>
	</c:if>
	
	<!-- if 의 역할 -->
	<c:if test="${len eq 13}">
		<!-- else if 의 역할 -->
		<c:choose>
			<c:when test="${genderno eq '1'}">	<!-- 이미 바깥에 "" 이기 때문에 {} 안에 '' -->
				1900 년대생 남자입니다.<br/>
			</c:when>

			<c:when test="${genderno eq '2'}">	<!-- 이미 바깥에 "" 이기 때문에 {} 안에 '' -->
				1900 년대생 여자입니다.<br/>
			</c:when>

			<c:when test="${genderno eq '3'}">	<!-- 이미 바깥에 "" 이기 때문에 {} 안에 '' -->
				2000 년대생 남자입니다.<br/>
			</c:when>
		
			<c:when test="${genderno eq '4'}">	<!-- 이미 바깥에 "" 이기 때문에 {} 안에 '' -->
				2000 년대생 여자입니다.<br/>
			</c:when>
			
			<%--else 의 역할--%>
			<c:otherwise>
				성별을 알 수 없습니다.<br/>
			</c:otherwise>				
		</c:choose>		
	</c:if>

</body>
</html>