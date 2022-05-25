<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 표준액션 중 forward 에 대해서 알아봅니다.</title>
</head>
<body>
	<h2>입력한 두개의 수 사이를 누적한 값 알아오기(EL 사용한 예제)</h2>

	<form name="myFrm">
		<p>
	         첫번째 수 : <input type="text" name="firstNum" size="5" maxlength="5" /><br/>
	         두번째 수 : <input type="text" name="secondNum" size="5" maxlength="5" /><br/>
	         <button type="button" onclick="goSubmit()">계산하기</button> 
	    </p>	
    </form>
    
    <script type="text/javascript">
    
		//Function Declaration	
		function goSubmit() {
			
			// 정규표현식으로 유효성 검사 (입력 칸에 숫자 / 한글 입력 됐을 경우)
			const regExp = /^[0-9]{1,5}$/;
			
			const frm = document.myFrm;
			const num1 = frm.firstNum.value.trim();
			const num2 = frm.secondNum.value.trim();
		
			if( !(regExp.test(num1) && regExp.test(num2)) ) {	// 모두 숫자로만 이루어져야 한다.
				alert("숫자로만 입력하세요.!!");
				frm.firstNum.value = "";
				frm.secondNum.value = "";				
				frm.firstNum.focus();		// 첫번째 입력 칸에 다시 커서를 옮긴다.
				return; // 종료
			}
			
			frm.action = "03forwardCalc_EL_02.jsp";	// 일처리 해줄 jsp 파일. (01->02->03 순을 기억)
		//	frm.method = "get";	// method 를 명기하지 않으면 기본값은 "get" 이다.
			frm.submit();	// 전송한다.
			
		}// end of function goSubmit(){}--------------
		
    </script>
</body>
</html>