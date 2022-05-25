<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
    
<%-- JSTL(JSP Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<%-- 
	fmt 태그는 Formatting 태그로 포맷에 관련된 태그입니다.
    참조사이트  https://sinna94.tistory.com/11
--%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문자열로 된 숫자를 누적한 결과값(자동형변환, 형변환) 나타내기 및 정수로 된 데이터를 세자리마다 콤마를 찍어서 나타내어주기</title>
</head>
<body>
	<h2>문자열로 된 숫자를 누적한 결과값(자동형변환) 나타내기(JSTL 사용한 것) </h2>

	<c:set var="sum" value="0"/>
	<c:if test="${not empty requestScope.pointArr1}">
	<%-- not empty 는 null도 안되고 {}; 도 안된다. --%>
		<ul>
			<c:forEach var="point" items="${ requestScope.pointArr1 }">
				<li>${point}</li> <%-- 변수를 출력한다. --%>
				<c:set var="sum" value="${sum + point}" /> <%-- point 를 sum 에 누적 --%>
			</c:forEach>
		</ul>
		pointArr1 누적의 합계 : ${sum}
	</c:if>
	
	<br/>
	<hr style="border: solid 1px red;">
	<br/>
	
	<h2>문자열로 된 숫자를 정수로 형변환하여 누적한 결과값 나타내기(JSTL 사용한 것) </h2>
	<c:set var="sum" value="0"/>
	<c:if test="${not empty requestScope.pointArr1}">
	<%-- not empty 는 null도 안되고 {}; 도 안된다. --%>
		<ul style="list-style-type: circle;">
			<c:forEach var="point" items="${ requestScope.pointArr1 }">
				<li>
					<fmt:parseNumber var="pointInt" value="${point}" integerOnly="true"/>	
					<%--
						fmt:parseNumber 는 문자열을 숫자형식으로 형변환하는 것이다.
						integerOnly="true" 는 소수점은 절삭하고 정수만 취해오는 것이다.
						정수만 취해온 값을 변수 pointInt 에 넣어준다.
					--%>
					${pointInt}
				</li> 
				 <c:set var="sum" value="${sum + pointInt}" />  <%-- pointInt 를 sum 에 누적 --%>
			</c:forEach>
		</ul>
		pointArr1 을 정수만 취해온 누적의 합계 : ${sum} 
	</c:if>
	
	<br/>
	<hr style="border: solid 1px blue;">
	<br/>
	
	<h2>forEach 를 사용하면서 index 번호를 나타내기</h2>

	<c:set var="sum" value="0"/>
	<c:if test="${not empty requestScope.pointArr1}">
	<%-- not empty 는 null도 안되고 {}; 도 안된다. --%>
		<ul>
			<c:forEach var="point" items="${ requestScope.pointArr1 }" varStatus="status">
				<li>${point}&nbsp;(인덱스번호 &nbsp; ${status.index})</li> <%-- 변수를 출력한다. --%>
				<%-- ${status.index} 는 0부터 시작한다. --%>	
			</c:forEach>
		</ul>
	</c:if>	
	
	<br/>
	<hr style="border: solid 1px red;">
	<br/>
	
	<h2>forEach 를 사용하면서 순서 번호를 나타내기</h2>

	<c:set var="sum" value="0"/>
	<c:if test="${not empty requestScope.pointArr1}">
	<%-- not empty 는 null도 안되고 {}; 도 안된다. --%>
		<ul>
			<c:forEach var="point" items="${ requestScope.pointArr1 }" varStatus="status">
				<li>${point}&nbsp;(순서번호 &nbsp; ${status.count})</li> <%-- 변수를 출력한다. --%>
				<%-- ${status.count} 는 1부터 시작한다. --%>	
			</c:forEach>
		</ul>
	</c:if>	
	
	
	<br/>
	<hr style="border: solid 1px blue;">
	<br/>
	
	<h2>정수로 된 데이터를 세자리 마다 콤마를 찍어서 나타내기(JSTL을 사용한 것)</h2>

	<c:set var="sum" value="0"/>
	<c:if test="${not empty requestScope.priceArr}">
		<ul>
			<c:forEach var="price" items="${ requestScope.priceArr }">
				<li><fmt:formatNumber value="${price}" pattern="#,###" /> 원</li>	<%-- value : 실제 값은 무엇인지? (여기서 배열 안에 들어있는 값들) --%>
				<c:set var="sum" value="${sum + price}" />
			</c:forEach>
		</ul>
		priceArr 누적의 합계 : <fmt:formatNumber value="${sum}" pattern="#,###" /> 원
	</c:if>
	

</body>
</html>