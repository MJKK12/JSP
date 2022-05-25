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
<title>GET/POST 방식으로 데이터 전송하기</title>

<style type="text/css">

	ul {list-style-type: none;}
	li {line-height: 200%;}

</style>

</head>
<body>

	<form action="<%= ctxPath%>/03_getPostMethod.do" method="post">	
	<%-- !! 중요 !!
	     확장자가 .jsp 또는 .html 인 파일에서 URL경로를 나타낼때 맨 앞에 / 가 오면
	     그 앞에는  http://ip주소:포트번호 가 자동으로 붙게 된다.
		 <%= ctxPath%> 이  /JSPServletBegin 이다.      
	     즉, 우리는  http://localhost:9090/JSPServletBegin/03_getPostMethod.do 으로 된다.        
  	--%>
  	
 <%--  form 태그에서 submit(전송)을 하면
       http://localhost:9090/JSPServletBegin/03_getPostMethod.do 으로 데이터를 전송시킨다.
       method 를 생략하면 method="get" 으로 된다.
            
       POST방식은  웹브라우저 주소창에  http://URL주소만나옴 으로 나타내주는 것이며 ? 가 없다.
       POST방식은 전송할 데이터를 웹브라우저 주소창 아닌 본문에 넣어서 보낸다.
       POST방식은 웹브라우저 주소창에 데이터를 공개하지 않으므로 보안성은 좋지만 속도는 GET 방식보다 느리다.
       그래서 일반적으로 보안과 관계있는 데이터 입력,수정(예: 회원가입, 내정보수정)와 같은 insert, update 절에서 주로 사용된다.
  --%>
  	
  	<%-- 
	     submit 을 하면 <%= ctxPath%>/03_getPostMethod.do 로 보내서 처리를 요청한다.
	     /JSPServletBegin/03_getPostMethod.do 은 누가 처리를 해주는지는 배치서술자인 web.xml 에 기술되어져 있어야 한다.
	     web.xml 에 가보면 <servlet-mapping>에 URL 패턴으로 /03_getPostMethod.do 가 기술되어져 있고,
	     이어서 실제로 처리를 해주는 <servlet-class>에 클래스명이 기술되어져 있다.
	     바로 이렇게 기술되어진 <servlet-class>클래스명이 action 일처리를 해주게 된다.
	        
	    !!! 아주 중요한 것은 web.xml은 언제 읽어들이는냐? 하면
	    WAS가 구동되어질때 딱 1번만 읽어들여서 WAS가 web.xml에 기술된 내용을 기억하도록 되어있다.
	    그러므로 web.xml에 내용을 추가하거나 삭제하는 등 변경되어지면 변경된 내용대로 작동하기 위해서는 반드시 WAS를 껐다가 켜야만 한다. !!!    
  	--%>
		<fieldset>
	      <legend>개인성향 테스트(GET/POST method)</legend>
	      <ul>
	         <li>
	            <label for="name">성명</label>
	            <input type="text" name="name" id="name" placeholder="성명입력"/> 
	         </li>
	         <li>
	            <label for="school">학력</label>
	            <select name="school" id="school">
	               <option value="고졸">고졸</option>
	               <option value="초대졸">초대졸</option>
	               <option value="대졸">대졸</option>
	               <option value="대학원졸">대학원졸</option>
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