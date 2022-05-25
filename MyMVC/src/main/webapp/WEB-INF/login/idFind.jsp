<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		// get 방식으로 아이디 찾기 버튼을 눌렀을 때 hide 처리 해라.
			$("div#div_findResult").hide();	// id 를 처음부터 보이게 하지 않는다.
		
		} else if (method == "POST") {
			// 즉, post 방식으로 값이 넘어왔다면, 입력했던 name과 email 을 그대로 남기고자 한다. (넘어온 값을 꽂아준다.!)
			$("input#name").val("${requestScope.name}");
			$("input#email").val("${requestScope.email}");
		}

		// 찾기 버튼을 '마우스'클릭 했을때 제출(goFind) 되도록 한다.
		$("button#btnFind").click(function () {
		
		// 성명 및 email에 대한 유효성 검사(정규표현식) --> 스스로 해볼 것!
		//// 유효성 검사 작성 필요 ////
		
		
		// form 태그 제출한다.
<%-- 	const frm = document.idFindFrm;		// form 태그의 name 이 idFindFrm
		frm.action = "<%=ctxPath%>/login/idFind.up";	
		frm.method = "post";	// 클릭 시 post 방식으로 /login/idFind.up 에 보낸다.
		frm.submit(); --%>
			goFind();
		
		});

		// 이메일 버튼에 엔터를 눌렀을 때 제출되도록 한다.		
		$("input#email").bind("keydown", function name() {		// 
			if(event.keyCode == 13) {
				goFind();
			}
		});	
		
	}); // end of $(document).ready(function()-------------------------

	// Function declaration
	/// java 로 제출하는 goFind() 함수 -- 아이디찾기 마우스 클릭 or 엔터키 쳤을때 동시에 사용하는 함수를 만들어준 것임. ////
	function goFind() {
		
		const frm = document.idFindFrm;		// form 태그의 name 이 idFindFrm
		frm.action = "<%=ctxPath%>/login/idFind.up";	
		frm.method = "post";	// 클릭 시 post 방식으로 /login/idFind.up 에 보낸다.
		frm.submit();	
					
	}	
			

</script>

<form name="idFindFrm">
   
   <ul style="list-style-type: none">
         <li style="margin: 25px 0">
            <label for="name" style="display: inline-block; width: 90px">성명</label>
            <input type="text" name="name" id="name" size="25" placeholder="홍길동" autocomplete="off" required />
         </li>
         <li style="margin: 25px 0">
            <label for="email" style="display: inline-block; width: 90px">이메일</label>
            <input type="text" name="email" id="email" size="25" placeholder="abc@def.com" autocomplete="off" required />
         </li>
   </ul>
   
   <div class="my-3">
    <p class="text-center">
       <button type="button" class="btn btn-success" id="btnFind">찾기</button>
    </p>
   </div>
   
   <div class="my-3" id="div_findResult">
        <p class="text-center">
           ID : <span style="color: red; font-size: 16pt; font-weight: bold;">${requestScope.userid}</span> 
      </p>
   </div>
   
</form>
