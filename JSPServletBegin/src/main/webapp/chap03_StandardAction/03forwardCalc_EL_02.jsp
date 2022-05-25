<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 계산이 주 목적이다. -->
<%
	String s_firstNum = request.getParameter("firstNum");	// 내장객체인 request, return 타입은 String 이다.
	String s_secondNum = request.getParameter("secondNum");	// 내장객체인 request
	
	// 숫자모양을 가진 문자가 넘어온다. (숫자로 된 문자) → int 로 바꿔준다..
	int firstNum = Integer.parseInt(s_firstNum);
	int secondNum = Integer.parseInt(s_secondNum);
	
	int sum = 0;
	for(int i=firstNum; i<=secondNum; i++) {
		sum += i;
	}
	
	// 결과물은 sum 이다.
	System.out.println("sum =>" + sum);		
	
	/*
	    !!!! 중요 꼭 암기 !!!!
	    == request 내장객체는 클라이언트( 02forwardCalc_execute_01.jsp ) 가 
	       보내온 데이터를 읽어들이는 역할( request.getParameter("name명"); )도 있고 
	       또한 어떤 결과물을 저장시키는 저장소 기능( request.setAttribute("키", 저장할객체); ) 도 있다. 
 	*/
 	
// 	request.setAttribute("firstno", new Integer(firstNum));
// 	request.setAttribute("secondno", new Integer(secondNum));
//	원래는 위처럼 객체를 만들어서 저장해야 하지만 자바가 알아서 auto boxing(자동적으로 원시형 데이터(int)를 객체(integer 타입으로 만들어 주는 것) 을 해주기 때문에 아래처럼 쓸 수 있다.	
 	request.setAttribute("firstno", firstNum);
 	request.setAttribute("secondno", secondNum);

 //	request.setAttribute("hab", new Integer(sum)); 	
 	request.setAttribute("hab", sum);
%>

<jsp:forward page="03forwardCalc_EL_view_03.jsp" />
<%--
   웹브라우저 상에서 URL 주소는 그대로 http://localhost:9090/JSPServletBegin/chap03_StandardAction/03forwardCalc_EL_02.jsp 인데 
   웹브라우저 상에 보여지는 내용물은 http://localhost:9090/JSPServletBegin/chap03_StandardAction/03forwardCalc_EL_02.jsp 의 내용이 보여진다.      
--%>