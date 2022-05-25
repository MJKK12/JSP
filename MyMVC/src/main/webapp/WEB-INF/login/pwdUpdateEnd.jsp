<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 5 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/style.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js"></script> 

<style type="text/css">

#div_pwd {
      width: 70%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;
      position: relative;
   }
   
   #div_pwd2 {
      width: 70%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;
      position: relative;
   }
   
   #div_updateResult {
      width: 90%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;      
      position: relative;
   }
   
   #div_btnUpdate {
      width: 70%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;
      position: relative;
   }

</style>


<script type="text/javascript">
	
	$(document).ready(function(){

		$("button#btnUpdate").click(function name() {
			// 암호는 정규표현식에 적합해야 통과한다.
			const pwd = $("input#pwd").val();		// 새암호
			const pwd2 = $("input#pwd2").val();		// 새암호확인
			
		 // const regExp = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
		 // 또는
	        const regExp = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
	     // 숫자/문자/특수문자/ 포함 형태의 8~15자리 이내의 암호 정규표현식 객체 생성
	       
	     	const bool = regExp.test(pwd);	// true, false 값으로 반환 (기본값 true)
		         
		    if(!bool) {
				// 암호가 정규표현식에 위배된 경우 (8~15자리 이내의 암호 정규표현식)
				alert("암호는 8~15자리 이내의 영문자, 숫자, 특수기호가 혼합되어야 합니다.");				
				$("input#pwd").val("");
				$("input#pwd2").val("");
				return;	// 종료				
			}
		    else if (bool && pwd != pwd2) {
				// 암호 = 새암호확인이 일치하지 않는다.
				alert("암호가 일치하지 않습니다.");
				$("input#pwd").val("");
				$("input#pwd2").val("");
				return;	// 종료	    
		    }
			else {
				// 암호 = 새암호확인 이므로 DB 에 보내주자.
				const frm = document.pwdUpdateEndFrm;
				
				frm.action = "<%= ctxPath%>/login/pwdUpdateEnd.up";
				frm.method = "post";
				frm.submit();
				
			}
	     	
		})
				
	}); // end of $(document).ready(function()-------------------------
	
</script>	

<%-- 비밀번호 변경을 위한 새암호 창 띄우기 --%>
<form name="pwdUpdateEndFrm">

	<div id="div_pwd" align="center">
      <span style="color: blue; font-size: 12pt;">새암호</span><br/> 
      <input type="password" name="pwd" id="pwd" size="25" placeholder="PASSWORD" required />
   </div>
   
   <div id="div_pwd2" align="center">
        <span style="color: blue; font-size: 12pt;">새암호확인</span><br/>
      <input type="password" id="pwd2" size="25" placeholder="PASSWORD" required />
   </div>

	<input type="hidden" name="userid" value="${requestScope.userid}">
   <!-- 암호 변경은 get 방식일 때만 보여줘야 한다. -->
   <c:if test="${requestScope.method == 'GET'}">
		<div id="div_btnUpdate" align="center">
	           <button type="button" class="btn btn-success" id="btnUpdate">암호변경하기</button>
	    </div>   
	</c:if>
	<!-- 암호 변경은 post 방식일때와 int = 1 값이 넘어와야 변경된다. (DAO 에서 리턴값 1, 고유한 userid 1개에 대한 암호 변경) -->
   <c:if test="${requestScope.method == 'POST' && requestScope.n == 1 }">
		<div id="div_btnUpdateResult" align="center">
			사용자 ID ${requestScope.userid}님의 암호가 새롭게 변경되었습니다.<br/>
	    </div>   
	</c:if>	
</form>