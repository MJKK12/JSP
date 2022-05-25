<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
	String ctxPath = request.getContextPath();
	//	MyMVC
%>

<jsp:include page="../header.jsp" />

<style type="text/css">
	tr.memberInfo:hover {
		background-color: #e6ffe6;
		cursor: pointer;
	}
</style>

<script type="text/javascript">
	
	// 검색 (두개 중 택1)
	$(document).ready(function() {	// 문서가 로딩되면,

		// **** select 태그에 대한 이벤트는 click 이 아니라 change 이다 ****//
		
		$("select#sizePerPage").bind("change", function() {
			const frm = document.memberFrm
			frm.action = "memberList.up";
			frm.method = "get";
			frm.submit();
		});
		
		// 게시물 조회 갯수에 대해서 해당 칸에 보고싶은 갯수를 설정하기 위함
		$("select#sizePerPage").val("${requestScope.sizePerPage}");
		
		// 검색하기
		$("form[name='memberFrm']").submit(function(){	// 이 form 이 submit 될 때 검사하겠다.
			
			if($("select#searchType").val() == "") {	// searchType 의 value 값이 "" 로 되어있을 때,!!				
				alert("검색대상을 올바르게 선택하세요!");
				return false;	// return false; submit 을 하지 말라는 뜻이다.
			}

			if($("input#searchword").val().trim() == "") {	// 검색어를 입력하지 않았을 경우!
				alert("검색어는 공백 만으로는 되지 않습니다.\n");	// 검색어를 올바르게 입력하세요!
				return false;	// return false; submit 을 하지 말라는 뜻이다.
			}
		});
			
	
	// 검색결과 보이기	(keyup 또는 keyDown 했을 때 검색결과 보이기)
	$("input#searchword").bind("keyup", function name() {
		if(event.keyCode == 13) {
			// 검색어에서 엔터를 치면 검색하러 가도록 한다.
			goSearch();
		}
	});//$("input#searchword").bind("keyup", function name() {}---------

	// alert("확인용 ${requestScope.searchType}");
	//		확인용 ""
	//		확인용 name
	// 문서가 로딩되면 검색후에도 해당 검색타입과 검색어를 그대로 유지시켜 준다.
	// 문서가 로딩 되자마자 검색이 없을 때, option 태그의 value 값을 넣어서 그 value 값을 그대로 보이도록 한다.
	
	// 만약에 searchType 에서 넘어온 값이 (null 이 아니라 ""로 할 것)
	// 자바스크립트 이기 떄문에, 문자열이므로 ${requestScope.searchType} 에 "" 를 꼭 붙여줄것!!
	if( "${requestScope.searchType}" != "" ) {
	 $("select#searchType").val("${requestScope.searchType}"); 	
	 $("input#searchword").val("${requestScope.searchword}");
	}
	
	// 특정 회원을 클릭하면 그 회원의 상세 정보를 보여주도록 한다.
	$("tr.memberInfo").click( ()=>{
		
		const $target = $(event.target);
	//	alert("확인용 => " + $target.parent().html() );	// parent() 한 단계 올라가자. <td> --> <tr> 로
		const userid = $target.parent().children(".userid").text();		// parent <tr> 의 자식인 children <td>	
	//	alert("확인용 => " + userid);
		
		location.href="<%= ctxPath%>/member/memberOneDetail.up?userid="+userid+"&goBackURL=${requestScope.goBackURL}";	
		//															   				&goBackURL=/member/memberList.up?currentShowPageNo=10 sizePerPage=10 searchType= searchword=%EC%9C%A0
		// 																			Action 단에서 가져온다. Action 에서 & 을 "" 로 바꿔준 것이다.
	});
	
});// end of $(document).ready(function(){}); --------------------------------


// function declaration (검색)
function goSearch() {

	if($("select#searchType").val() == "") {	// searchType 의 value 값이 "" 로 되어있을 때,!!				
		alert("검색대상을 올바르게 선택하세요!");
		return;	// return; 함수를 종료하라는 뜻이다.
	}

	if($("input#searchword").val().trim() == "") {	// 검색어를 입력하지 않았을 경우!
		alert("검색어는 공백 만으로는 되지 않습니다.\n 검색어를 올바르게 입력하세요.");	// 검색어를 올바르게 입력하세요!
		return;	// return; 함수를 종료하라는 뜻이다.
	}
	
	const frm = document.memberFrm
	frm.action = "memberList.up";
	frm.method = "get";
	frm.submit();
}

	

</script>

<h2 style="margin: 20px;">::: 회원전체 목록 :::</h2>
<%-- 검색조건 : 회원명, 아이디 , 이메일 (이미지 참고) --%>
<%--<form name="memberFrm">--%>
	 <form name="memberFrm" action="memberList.up" method="get"> <%-- memberList.up 으로 가도록 한다. --%>
		<select id="searchType" name="searchType">	<%-- search 형태가 무엇인가? --%>
			<option value="">검색대상</option>	<%-- 아무것도 선택하지 않았을 때 검색조건이 보이도록 하기. --%>
			<option value="name">회원명</option>	<%-- option 에서 value 가 없으면 <> 사이 <> 에 있는것이 value 이다. (여기서는 회원명, 아이디, 이메일이라고 쓴 것이 value) --%>
			<option value="userid">아이디</option>	<%-- 검색대상은 value 값이다. DB 에서 보낼때의 컬럼명과 같다. --%>
			<option value="email">이메일</option>
		</select>
		<input type="text" id="searchword" name="searchword">				
		<%--   form 태그내에서 전송해야할 input 태그가 만약에 1개 밖에 없을 경우에는 유효성검사가 있더라도 
               유효성 검사를 거치지 않고 바로 submit()을 하는 경우가 발생한다.
               이것을 막아주는 방법은 input 태그를 하나 더 만들어 주면 된다. 
               그래서 아래와 같이 style="display: none;" 해서 1개 더 만든 것이다. 
        --%>
      <input type="text" style="display: none;" /> <%-- 조심할 것은 type="hidden" 이 아니다. --%>		
<%--<button type="button" onclick="goSearch();" style="margin-right: 30px;">검색</button> --%> <%-- 검색버튼 클릭시 함수 실행 --%>	
	  <input type="submit" value="검색" style="margin-right: 30px;" /> 
		
		<%-- 페이지당 회원명수를 보여준다. --%>
		<span style="color: red; font-weight: bold; font-size: 12pt;">페이지당 회원명수:</span>
	      <select id="sizePerPage" name="sizePerPage">
	         <option value="10">10</option>
	         <option value="5">5</option>
	         <option value="3">3</option>
	      </select>
	 </form>
	
	<%-- 이제 form 태그를 뿌려주자. --%>
	<table id="memberTbl" class="table table-bordered" style="width: 90%; margin-top: 20px;">
        <thead>
           <tr>
              <th>아이디</th>
              <th>회원명</th>
              <th>이메일</th>
              <th>성별</th>
           </tr>
        </thead>
		<%-- select 된 항목이 여러개가 나오게 된다. (default 는 10개가 보이도록 한다.) 반복문 사용한다. --%>
		<tbody>	<%-- items 에는 배열 아니면 list 이다. --%>	<%-- var : 하나하나 요소를 가져온다. 여기서는 한개한개의 요소가 membervo 이므로 mvo로 변수명 설정 --%>
			<c:if test="${not empty requestScope.memberList}">	<%-- 검색했을때 아무것도 나오지 않을때와 결과물이 나올때 2가지 경우의 수 --%>
				<c:forEach var="mvo" items="${requestScope.memberList}">
					<tr class="memberInfo">	<%-- 회원정보가 복수개로 나오므로 id가 아니라 class 를 준다. --%>
						<td class="userid">${mvo.userid}</td>		<%-- memberVo 에서 getXXX 에서 XXX 를 가져온다. --%>		
						<td>${mvo.name}</td>				
						<td>${mvo.email}</td>				
						<td><%-- db에서 1,2로 나타낸 성별을 바꿔준다. --%>
							<c:choose>
								<c:when test="${mvo.gender eq '1'}">
								남
								</c:when>
								<c:otherwise>
								여
								</c:otherwise>								
							</c:choose>
						</td>				
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${empty requestScope.memberList}">	<%-- 결과물이 아무것도 없다면(empty),  requestScope.memberList 가 empty 라면, 아래와 같은 문구를 띄운다. --%>
				<tr>
					<td colspan="4" style="text-align: center;">검색된 데이터가 존재하지 않습니다.</td>
				</tr>
			</c:if>		
		</tbody>
	</table>
	
	<%-- 페이지네이션 바, Action 단으로 넘긴다. 부트스트랩 사용 --%>
	<nav class="my-5">
		<div style="display: flex; width: 80%">
			<ul class="pagination" style='margin:auto;'>${requestScope.pageBar}</ul>
		</div>
	</nav>

<jsp:include page="../footer.jsp" />