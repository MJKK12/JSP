<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- 필요한 클래스를 import 하려면 <%@ page %> 를 사용하고, 이를 page directive(페이지 지시어) 라고 한다. --%>
<%@ page import="java.util.Date" %>

<%
	// 현재 시각을 알아오기
	Date now = new Date();	// 현재 시각

	String currentTime = String.format("%tF %tT %tA", now, now, now, now); 
%>	

<%= currentTime%>