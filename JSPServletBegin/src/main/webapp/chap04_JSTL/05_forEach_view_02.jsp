<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%-- JSTL(JSP Standard Tag Library) 사용하기 --%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>친구명단 출력하기</title>
</head>
<body>

	<c:if test="${empty requestScope.arrFriendName}">
		<div>
			<span style="color: red;">친구 명단이 없습니다.</span>
		</div>
	</c:if>


	<c:if test="${not empty requestScope.arrFriendName }">
		<div>
			<ol>
				<c:forEach var="friendName" items="${requestScope.arrFriendName}">	<%-- 배열의 갯수만큼 반복되어야 한다.--%>
											<%-- items="${} 에 들어오는 것은 requset 영역에 저장되어있는 배열이나 List명 이다. --%>
				<li style="color: blue;">${friendName}</li>	<%-- var : 변수 --%>
				</c:forEach>
			</ol>
		</div>
	</c:if>


	<hr style="border: solid 1px red;">

	<c:if test="${empty requestScope.personList}">
		<div>
			<span style="color: red;">회원 명단이 없습니다.</span>
		</div>
	</c:if>


	<c:if test="${not empty requestScope.personList }">
		<div>
			<c:forEach var="psdto" items="${requestScope.personList}">	<%-- 배열의 갯수만큼 반복되어야 한다.--%>
								   <%-- items="${} 에 들어오는 것은 requset 영역에 저장되어있는 배열이나 List명 이다. --%>					
				<ol>
					<li>성명 : ${psdto.name}</li>	<%-- .다음에는 get 다음에 나오는 것이다. --%>
					<li>학력 : ${psdto.school}</li>	
					<li>색상 : ${psdto.color}</li>	
					<li>음식 : ${psdto.strFood}</li>	<%-- DTO 파일에서 가져온다. --%>
				</ol>
			</c:forEach>
		</div>
	</c:if>
	

</body>
</html>