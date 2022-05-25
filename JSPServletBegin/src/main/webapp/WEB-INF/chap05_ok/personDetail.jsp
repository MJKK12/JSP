<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	String ctxPath = request.getContextPath();
	//				/JSPServletBegin
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개별 회원의 성향 결과</title>

<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" type="text/css">

<style type="text/css">

	span#colorbox {
		display: inline-block;
		width: 30px;
		height: 30px;				
		margin-left: 10px;
		
		position: relative;		
		top: 8px;
	}

</style>

<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript">

	$(document).ready(function () {
		$("span#colorbox").css({'background-color':'${requestScope.psdto.color}'});	// 서블릿에서 보내준 color
	});

	// Function declaration
	function deletePerson() {
		
		const bool = confirm("정말 삭제하시겠습니까?");	// window.confirm 에서 window. 는 생략 가능하다.	
		
		if(bool) {
			// select 는 get 방식을 쓸 수 있지만, 삭제같은 경우에는 post 방식을 사용한다. (아무나 삭제하면 안되기 때문이다.)
			const frm = document.delFrm;		// form 태그인데 name이 delFrm 이다.
		//	console.log("확인용 삭제할 회원번호 seq => " + frm.seq.value);
			frm.action = "personDelete.do";
			frm.method = "post";
			frm.submit();
		}
		
	}// end of function deletePerson()------------------------
	
	function updatePerson() {
		
		const frm = document.updateFrm;
		// 보내주자.
		frm.action = "personUpdate.do";
		frm.method = "post";
		frm.submit();		
		
	}//end of function updatePerson()--------------------------

	</script>

</head>
<body class="py-5">

<div class="container">
	<div class="card">
		<div class="card-header">	<%-- card-header 에는 카드 머리말이 들어간다. --%>
			<p class="h4 text-center">
				<span class="text-primary">${requestScope.psdto.name}</span>&nbsp;<small>님의 개인성향 결과</small>
			</p>
		</div>	
		
		<div class="card-body">	<%-- card-body 에는 카드 내용이 들어간다. --%>
			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">회원번호</div>			
				<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.seq}</span></div>			
			</div>

			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">성명</div>			
				<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.name}</span></div>			
			</div>

			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">학력</div>			
				<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.school}</span></div>			
			</div>

			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">색상</div>			
				<div class="col-md-9 py-2">
					<c:choose>
						<c:when test="${requestScope.psdto.color eq 'red'}"><span class="h5">빨강</span></c:when>
						<c:when test="${requestScope.psdto.color eq 'blue'}"><span class="h5">파랑</span></c:when>
						<c:when test="${requestScope.psdto.color eq 'green'}"><span class="h5">초록</span></c:when>
						<c:when test="${requestScope.psdto.color eq 'yellow'}"><span class="h5">노랑</span></c:when>
						<c:otherwise><span class="h5">기타</span></c:otherwise> <%-- 위의 4개중에서 어느것에도 해당하지 않는다면 --%>
					</c:choose>
					<span id="colorbox" class="rounded-circle"></span>
							
				</div>			
			</div>
			
			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">음식</div>			
				<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.strFood}</span></div>			
			</div>
			
			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">음식이미지 파일명</div>			<%-- 음식 파일명을 적어준다. --%>
				<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.strFoodImgFileName}</span></div>			
			</div>
			
			<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0">	<%-- 반응형 웹:row --%>
				<div class="col-md-3 py-2">음식사진</div>			
				<div class="col-md-9 py-2">
					<%-- 김태희는 음식을 선택하지 않았으므로 이에 대비한 if를 써준다. --%>		
					<c:if test="${not empty requestScope.psdto.strFoodImgFileName}">	<%-- 선택한 음식이 있다면 ","를 기준으로 자른다. --%>
						<c:forTokens var="imgFileName" items="${requestScope.psdto.strFoodImgFileName}" delims=",">
							<img src="<%= ctxPath%>/chap05_images/${imgFileName}" width="130" class="img-fluid rounded mr-1" />
						</c:forTokens>
					</c:if>	
					
					<c:if test="${empty requestScope.psdto.strFoodImgFileName}">
						<span class="h5">좋아하는 음식이 없습니다.</span>
					</c:if>	
				</div>
			</div>
								
		</div>			
		
		<div class="card-footer">	<%-- card-footer 에는 카드 꼬리말이 들어간다. --%>
			<div class="row" style="position: relative; top: 10px; ">	<%-- relative 현재위치 에서, top 내려가라 --%>
				<div class="col-md-3">
					<p class="text-center">
						<a class="btn btn-info" role="button" href="personSelectAll.do">목록보기-1</a>
					</p>
				</div>	
				
				<div class="col-md-3">
					<p class="text-center">
						<a type="button" class="btn btn-success" onclick="javascript:location.href='personSelectAll.do'">목록보기-2</a>
					</p>
				</div>	
				
				<div class="col-md-3">
					<p class="text-center">
						<button type="button" class="btn btn-danger" onclick="deletePerson()">삭제하기</button>
					</p>
				</div>	
				
				<div class="col-md-3">
					<p class="text-center">
						<button type="button" class="btn btn-primary" onclick="updatePerson()">내정보수정하기</button>
					</p>				
				</div>	
			</div>
		</div>			
	</div>	
</div>

<%-- Post 방식을 통해 회원을 삭제하기 위해 화면에는 보이지 않는 form 태그를 만든다. --%>
<form name="delFrm">
	<input type="hidden" name="seq" value="${requestScope.psdto.seq}" /> <%-- 회원번호가 뜬다. --%>
	<input type="hidden" name="name" value="${requestScope.psdto.name}" />
</form>

<form name="updateFrm">
	<input type="hidden" name="seq" value="${requestScope.psdto.seq}" /> <%-- 회원번호가 뜬다. --%>
</form>

</body>
</html>