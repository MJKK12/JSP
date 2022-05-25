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


<script type="text/javascript">
	
	$(document).ready(function(){

		const method = "${requestScope.method}";	// JS의 변수는 입력받은 데이터 타입에 의해 결정된다. ("" 을 씀으로써 string 타입, 숫자는 그냥 123 이렇게 써도 무방하다.)
		
	//	console.log("확인용 method : " + method);
		
		if(method == "GET") {
		// get 방식으로 비밀번호 찾기 버튼을 눌렀을 때 hide 처리 해라.
			$("div#div_findResult").hide();
		
		} else if (method == "POST") {
		// post 방식으로 받아왔다면, hide 했던 것을 보이게 한다.
			$("div#div_findResult").show();
			
			$("input#userid").val("${requestScope.userid}");	// userid 에 값을 넣어준다.
			$("input#email").val("${requestScope.email}");		// email 에 값을 넣어준다.

			if(${requestScope.sendMailSuccess == true}) {	// 이메일이 발송되었을 때만!
				$("div#div_btnFind").hide();	// 찾기 버튼을 클릭시 찾기 버튼을 숨긴다. (※ 메일이 성공적으로 왔을때에만!)			
			}
		}

		// 찾기 버튼을 '마우스'클릭 했을때 제출(goFind) 되도록 한다.
		$("button#btnFind").click(function () {
			goFind();
		
		});

		// 찾기 버튼을 이메일 버튼에 엔터를 눌렀을 때 제출되도록 한다.		
		$("input#email").bind("keydown", function name() {		// 
			if(event.keyCode == 13) {
				goFind();
			}
		});	
		
		// 아이디-이메일 입력 후 비밀번호 찾기 누른 후, 발송된 인증번호를 입력하고 '인증하기' 버튼을 눌렀을 때 나오는 함수
		$("button#btnConfirmCode").click(function() {
			// 인증키는 POST 방식으로 보내야 한다. --> form 태그가 필요하다. (새로운 form 태그 만들기)
			const frm = document.verifyCertificationFrm;	// 아래에 새로 만든 비밀번호 인증 form 을 가져온다.
			// form 에 값을 집어넣기
			frm.userCertificationCode.value = $("input#input_confirmCode").val();			// userCertificationCode 에 유저가 입력한 값을 넣어준다.
			frm.userid.value = $("input#userid").val();	<%-- 이 form 에 있는 입력한 id의 값을 넣어줘야 한다. --%>
			
			frm.action = "<%= ctxPath%>/login/verifyCertification.up"
			frm.method = "post";
			frm.submit();
			
		})
		
		
	}); // end of $(document).ready(function()-------------------------

	/// java 로 제출하는 goFind() 함수 -- 아이디찾기 마우스 클릭 or 엔터키 쳤을때 동시에 사용하는 함수를 만들어준 것임. ////
	function goFind() {
		
		// 아이디 및 이메일에 대한 유효성 검사(정규표현식)은 여기서 생략. 스스로 해볼 것!
		
		const frm = document.pwdFindFrm;		// form 태그의 name 인 userid 와 email이 java 로 넘어간다.
		frm.action = "<%=ctxPath%>/login/pwdFind.up";		
		frm.method = "post";	// 클릭 시 post 방식으로 /login/idFind.up 에 보낸다.	// action = 자신한테 보낸다. (pwdFindAction.java) 로 이동한다.
		frm.submit();	
					
	}	
			

</script>

<form name="pwdFindFrm">
   
   <ul style="list-style-type: none">
         <li style="margin: 25px 0">
            <label for="userid" style="display: inline-block; width: 90px">아이디</label>
            <input type="text" name="userid" id="userid" size="25" placeholder="아이디" autocomplete="off" required />
         </li>
         <li style="margin: 25px 0">
            <label for="email" style="display: inline-block; width: 90px">이메일</label>
            <input type="text" name="email" id="email" size="25" placeholder="abc@def.com" autocomplete="off" required />
         </li>
   </ul>
   
   <div class="my-3" id="div_btnFind">
    <p class="text-center">
       <button type="button" class="btn btn-success" id="btnFind">찾기</button>
    </p>
   </div>
   
   <div class="my-3" id="div_findResult">
        <p class="text-center">
		<!-- 1. 사용자 정보가 없습니다. 
			 2. 인증코드를 입력해주세요.
			 3. 메일발송에 실패했습니다.	-->
		<c:if test="${requestScope.isUserExist == false}">
			<span style="color:red;">사용자 정보가 없습니다.</span>
		</c:if>
		
		<c:if test="${requestScope.isUserExist == true && requestScope.sendMailSuccess == true}">	<%-- 회원이 존재한다는 것을 확인, 유저가 존재하고 이메일 발송에 성공하면! --%> 
		<%-- 유저가 존재하면서 메일이 정상적으로 발송됐는지 확인해야 한다. --%>
			<span style="font-size: 10pt;">인증코드가 ${requestScope.email}로 발송되었습니다.</span><br>
             <span style="font-size: 10pt;">인증코드를 입력해주세요.</span><br>
             <input type="text" name="input_confirmCode" id="input_confirmCode" required />
             <br><br>
             <button type="button" class="btn btn-info" id="btnConfirmCode">인증하기</button>
		</c:if>
		
		<c:if test="${requestScope.isUserExist == true && requestScope.sendMailSuccess == false}">	<%-- 유저는 존재하지만, 이메일 전송에 실패함. --%>
			<span style="color:red;">메일 발송에 실패했습니다.</span>
		</c:if>		
      </p>
   </div>
   
</form>

<%-- 비밀번호 인증해주는 form 만들기 --%>
<form name="verifyCertificationFrm">
	<%-- 인증코드와 유저아이디값 까지 넘겨야한다. --%>
	<input type="hidden" name="userCertificationCode">
	<input type="hidden" name="userid">

</form>
