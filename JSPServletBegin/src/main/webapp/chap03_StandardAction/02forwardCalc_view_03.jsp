<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int firstNum = (Integer)request.getAttribute("firstNum");	// 02forwardCalc_02.jsp 에서 set 한것을 get 해온다.
	int secondNum = (Integer)request.getAttribute("secondNum");	// 02forwardCalc_02.jsp 에서 set 한것을 get 해온다.
	int sum = (Integer)request.getAttribute("sum");	// 02forwardCalc_02.jsp 에서 set 한것을 get 해온다.
	// auto Unboxing (Integer ==> int)
	
	// form 태그에서 아래의 값은 원래 _02.jsp 가 하는 것인데, 이 파일이 forward 를 해주면, 직접적으로 getParameter 로 가져올 수 있다.
	String s_firstNum = request.getParameter("firstNum");	// 내장객체인 request, return 타입은 String 이다.
	String s_secondNum = request.getParameter("secondNum");	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계산된 결과값을 보여주는 곳</title>
</head>
<body>
	<h3>계산된 결과값 -1</h3>
		<%=firstNum%>부터 <%=secondNum%>까지의 합은?<br/>
		결과값 : <span style="color:red;"><%=sum%></span>

	<br/><br/>

	<h3>계산된 결과값 -2</h3>
		<%=s_firstNum%>부터 <%=s_secondNum%>까지의 합은?<br/>
		결과값 : <span style="color:blue;"><%=sum%></span>
</body>
</html>