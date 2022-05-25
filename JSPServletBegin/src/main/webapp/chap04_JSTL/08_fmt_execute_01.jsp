<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String[] pointArr1 = {"10","20.4","30.8","52"};
	request.setAttribute("pointArr1", pointArr1);
	
	String[] pointArr2 = null;
	request.setAttribute("pointArr2", pointArr2);
	
	int[] priceArr = {50000, 100000, 250000, 150000, 2000000};
	request.setAttribute("priceArr", priceArr);
	
	RequestDispatcher dispatcher = request.getRequestDispatcher("08_fmt_view_02.jsp");	// view단 페이지에 넘긴다.("")에 상대경로 입력
	dispatcher.forward(request, response);		// 08_fmt_view_02.jsp 파일만 request 에 저장되어 있는 것을 꺼내올 수 있다.
%>