<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.util.*, chap05.personDTO_02" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctxPath = request.getContextPath();
	//				/JSPServletBegin
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개인성향 모든 정보 출력 페이지</title>

<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" type="text/css">

<style type="text/css">

	div#tbl {
		width: 80%;
		margin: 20px auto;
	}

	div#tbl > table {
		width: 100%;
		border-collapse: collapse;
	}

	div#tbl > table th, div#tbl > table td {
		border: solid 1px gray;
	}

	div#tbl > table > tbody > tr:hover {
      background-color: blue;
      color: white;
      cursor: pointer;
   }
   
   	tbody > tr > td:nth-child(1) > span  { 
      color: blue; 
      display: none;    <!-- span 태그는 보이지 말라는 뜻 (즉, 보이진 않지만 존재하고 있다는 것) -->
   }
   
   	div.container > table > tbody > tr:hover {
      cursor: pointer;
   }

</style>

<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript">

	$(document).ready(function () {
		
		$("tbody > tr").click(function () {
			
			const $target = $(event.target);	// <td> 태그이다.
		//	console.log("확인용 $target.html() => " + $target.html() );
			
			const seq = $target.parent().find("span").text();	// tr의 span 태그를 찾는다.
		//	console.log("확인용 seq =>" + seq);
			
			location.href="personDetail.do?seq="+seq;	// seq : 회원번호
			
		});
	});

</script>

</head>
<body>

	<div id="tbl">
		<h3>개인성향 모든 정보 출력 페이지(스크립틀릿을 사용하여 작성한 것)</h3>
<%
	List<personDTO_02> personList = (List<personDTO_02>) request.getAttribute("personList");		
	// set으로 설정한 것을 get 으로 가져온다. 형변환을 위해 List<personDTO_02> 를 () 안에 넣는다.
	// 서블릿은 Action 단, jsp 는 view 단

	if(personList.size() > 0) { %>	<!-- table 태그가 html 이기 때문에 분리해준다. -->
		<table>
			<thead>
				<tr>
					<th>성명</th>	
					<th>학력</th>	
					<th>색상</th>	
					<th>음식</th>	
					<th>등록일자</th>	
				</tr>
			</thead>
		
			<tbody>
		<%	for(personDTO_02 psdto : personList) { %>	<!-- 얘까지는 java -->
				<tr>	<!-- tr이 for 문 만큼 반복된다. -->
					<td><span><%= psdto.getSeq() %></span> <%= psdto.getName() %></td>	<!-- 결과물이기 때문에 expression을 사용한다. -->
					<td><%= psdto.getSchool()%></td>
					<td><%= psdto.getColor()%></td>
					<td><%= psdto.getStrFood() %></td>	<!-- getFood 는 배열이므로 join 을 사용한다. -->
					<!-- null 이라면 없음으로 나오게끔 메소드를 하나 만들어 줄 것이다 (DTO에 -->
					<td><%= psdto.getRegisterday()%></td>
				</tr>
		<%	} %>	<!-- for 문은 java 의 영역이기 때문에 < % % > 를 사용해준다.-->

			</tbody>		
		</table>
<%	}
	else {%>
		<span style="color: red;">데이터가 존재하지 않습니다.</span>
<%	}
%>	
		
	</div>

	<hr style="border: solid 1px gold; margin: 30px 0;">
	
	<div class="container">
		<h3>개인성향 모든 정보 출력페이지(JSTL을 사용하여 작성한 것)</h3>
		
		<table class="table table-hover">
			<thead>
				<tr>
					<th>성명</th>	
					<th>학력</th>	
					<th>색상</th>
					<th>음식</th>	
					<th>등록일자</th>	
				</tr>
			</thead>
			
			<tbody>
				<c:if test="${ not empty requestScope.personList }">
					<c:forEach var="psdto" items="${personList}">	<%-- for문으로 뿌리자. List가 personDTO_02로 구성되어있음. --%>
						<tr>	<!-- tr이 for 문 만큼 반복된다. -->
							<td><span>${psdto.seq}</span> ${psdto.name}</td>	<%--결과물이기 때문에 expression을 사용한다.--%>
							<td>${psdto.school}</td>	<%-- .다음 항상 소문자사용 --%>
							<td>${psdto.color}</td>
							<td>${psdto.strFood}</td>	<!-- getFood 는 배열이므로 join 을 사용한다. -->
							<!-- null 이라면 없음으로 나오게끔 메소드를 하나 만들어 줄 것이다 (DTO에 -->
							<td>${psdto.registerday}</td>
						</tr>						
					</c:forEach>
				</c:if>
			
				<c:if test="${ empty requestScope.personList }">	<%-- select 된 것이 없다. --%>
					<span style="color: red;">데이터가 존재하지 않습니다.</span>
				</c:if>
			</tbody>
		 </table>
	</div>	
	
	<div style="width: 80%; margin: 0 auto;">
		<p class="text-center">
			<button type="button" class="btn btn-info" onclick="javascript:location.href='personRegister.do'">개인성향 입력페이지로 가기</button>	<!-- 상대경로이므로 personRegister.do 만 쓴다. -->
		</p>
	</div>
	
</body>
</html>