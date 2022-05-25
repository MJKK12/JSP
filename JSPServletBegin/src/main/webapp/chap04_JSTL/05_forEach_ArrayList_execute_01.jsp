<%@page import="java.util.ArrayList"%>
<%@page import="java.beans.PersistenceDelegate"%>
<%@page import="java.time.Period"%>

<%@page import="java.util.*, chap03.PersonDTO"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String[] arrFriendName = {"김태희","전지현","차은우","송강","수지"};

	request.setAttribute("arrFriendName", arrFriendName);

	//////////////////////////////////////////////////////////
	
	List<PersonDTO> personList = new ArrayList<>();
	
	PersonDTO person1 = new PersonDTO();
	person1.setName("수지");
	person1.setSchool("대졸");
	person1.setColor("red");
	person1.setFood("샌드위치, 과자, 라면".split("\\,"));	// 자르면 문자열로 된다.
	
	PersonDTO person2 = new PersonDTO();
	person2.setName("송강");
	person2.setSchool("대학원졸");
	person2.setColor("blue");
	person2.setFood("파스타, 피자, 리조또".split("\\,"));	// 자르면 문자열로 된다.
	
	
	PersonDTO person3 = new PersonDTO();
	person3.setName("차은우");
	person3.setSchool("대졸");
	person3.setColor("pink");
	person3.setFood("햄버거, 치킨, 아이스크림".split("\\,"));	// 자르면 문자열로 된다.
	
	personList.add(person1);	//personList에 담는다(add)
	personList.add(person2);
	personList.add(person3);
	
	request.setAttribute("personList", personList);
	
	RequestDispatcher dispatcher = request.getRequestDispatcher("05_forEach_view_02.jsp");		// view 단 페이지로 보낸다.
	dispatcher.forward(request, response);	// forward 해주는 05_forEach~.jsp 파일만 arrFriendName 을 읽을 수 있다.
%>