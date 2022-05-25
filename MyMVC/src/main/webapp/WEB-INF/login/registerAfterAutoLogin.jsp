<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String ctxPath = request.getContextPath();
%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script type="text/javascript">

	window.onload = function () {
		// 회원으로 정식 가입 되면 화면을 띄운다.
		
		alert("회원가입을 축하드립니다.!!");
		
		const frm = document.loginFrm;		
		frm.action = "<%= ctxPath%>/login/login.up";
		frm.method = "post";
		frm.submit();
		
	}// end of window.onload = function ()----------------

</script>

</head>
<body>

	<form name="loginFrm">
      <input type="hidden" name="userid" value="${requestScope.userid}"/>
      <input type="hidden" name="pwd" value="${requestScope.pwd}"/>
   </form>

</body>
</html>