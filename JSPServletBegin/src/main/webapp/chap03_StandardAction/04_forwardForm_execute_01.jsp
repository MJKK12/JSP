<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	// 컨텍스트 패스명(context path name)을 알아오고자 한다.
	String ctxPath = request.getContextPath();
	
//	System.out.println("ctxpath => " + ctxPath);
// 	ctxPath => /JSPServletBegin     	<== WAS(톰캣서버) Document Base Path 설정의 기본값임.
// 	ctxPath => /aaa                 	<== WAS(톰캣서버) Document Base Path 설정을  /aaa 으로 변경한 경우임. 
// 	ctxPath =>     (아무글자도 없는 것임) 	<== WAS(톰캣서버) Document Base Path 설정을  / 으로 변경한 경우임.

%>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>POST 방식으로 데이터 전송하기</title>

<style type="text/css">

	ul {list-style-type: none;}
	li {line-height: 200%;}

</style>
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">

	$(document).ready(function () {
		
		// === 유효성 검사 === //
	/*		
		$("form[name='memberFrm']").submit(function () {
			
		});
	*/
	
	// 또는
	
		$("form[name='memberFrm']").submit(() => {
			
			//1. 성명은 필수입력 검사
			const name_length = $("input:text[name='name']").val().trim().length;
			
			if(name_length == 0) {
				alert("성명을 입력하세요!!");
				$("input:text[name='name']").val("").focus();	// name 을 공백으로 만들고 다시 커서를 focus 한다.
				return false;	// ★중요 : submit 을 하지 않고 종료한다.
			}
			
			//2. 학력은 필수입력 검사			
			const school_value = $("select[name='school']").val();
			if(school_value == "") {
				alert("학력을 선택하세요!!");
				return false;	// ★중요 : submit 을 하지 않고 종료한다.
			}
			
			//3. 색상은 필수입력 검사
			const color_checked_length = $("input:radio[name='color']:checked").length;	// radio 타입인데, name 이 color 이다. check 가 된 것이 몇개인가?
			if(color_checked_length == 0) {
				alert("색상을 선택하세요!!");
				return false;	// ★중요 : submit 을 하지 않고 종료한다.	
			}
			
			//4. 음식은 필수입력 검사
			const food_checked_length = $("input:checkbox[name='food']:checked").length;
			if(food_checked_length == 0) {
				alert("좋아하는 음식을 1개 이상 선택하세요!!");
				return false;	// ★중요 : submit 을 하지 않고 종료한다.	
			}
			
		});// end of $("form[name='memberFrm']").submit(() => {})-------------------------------
		
	});

</script>

</head>
<body>

	<form name="memberFrm" action="<%= ctxPath%>/registerPerson.do" method="post">	
		<fieldset>
	      <legend>개인성향 테스트(POST method)</legend>
	      <ul>
	         <li>
	            <label for="name">성명</label>
	            <input type="text" name="name" id="name" placeholder="성명입력"/> 
	         </li>
	         <li>
	            <label for="school">학력</label>
	            <select name="school" id="school">
					<option value="">학력선택</option>
					<option>고졸</option>
					<option>초대졸</option>
					<option>대졸</option>
					<option>대학원졸</option>
	            </select>
	         </li>
	         <li>
	            <label for="">좋아하는 색상</label>
	            <div>
	               <label for="red">빨강</label>
	               <input type="radio" name="color" id="red" value="red" />
	               
	               <label for="blue">파랑</label>
	               <input type="radio" name="color" id="blue" value="blue" />
	               
	               <label for="green">초록</label>
	               <input type="radio" name="color" id="green" value="green" />
	               
	               <label for="yellow">노랑</label>
	               <input type="radio" name="color" id="yellow" value="yellow" />
	            </div>
	         </li>
	         <li>
	            <label for="">좋아하는 음식(다중선택)</label>
	            <div>
	                <label for="food1">짜장면</label>
	               <input type="checkbox" name="food" id="food1" value="짜장면" />
	               &nbsp;
	               
	               <label for="food2">짬뽕</label>
	               <input type="checkbox" name="food" id="food2" value="짬뽕" />
	               &nbsp;
	               
	               <label for="food3">탕수육</label>
	               <input type="checkbox" name="food" id="food3" value="탕수육" />
	               &nbsp;
	               
	               <label for="food4">양장피</label>
	               <input type="checkbox" name="food" id="food4" value="양장피" />
	               &nbsp;
	               
	               <label for="food5">팔보채</label>
	               <input type="checkbox" name="food" id="food5" value="팔보채" />
	            </div>
	         </li>
	         <li>
	            <input type="submit" value="전송" />
	            <input type="reset" value="취소" />
	         </li>
	      </ul>
	   </fieldset>	
	</form>

</body>
</html>