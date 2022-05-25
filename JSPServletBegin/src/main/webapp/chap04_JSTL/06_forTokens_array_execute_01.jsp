<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String friendNames1 = "김태희,전지현,차은우,송강,수지";
	String friendNames2 = "박보검,강동원.박보영/이제훈,아이유";		// 배열이 아닌 문자열

	request.setAttribute("friendNames1", friendNames1);
	request.setAttribute("friendNames2", friendNames2);

	
	RequestDispatcher dispatcher = request.getRequestDispatcher("06_forTokens_view_02.jsp");		// view 단 페이지로 보낸다.
	dispatcher.forward(request, response);	// forward 해주는 05_forEach~.jsp 파일만 arrFriendName 을 읽을 수 있다.
%>