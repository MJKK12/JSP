<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>form 태그를 사용한 데이터 전송 시 useBean 을 사용하여 결과 보여주기</title>
</head>
<body>

	<h2>개인성향 입력 결과 정보(JSP 표준액션 중 useBean 을 사용한 것)</h2>

	<jsp:useBean id="psdto" class="chap03.PersonDTO" />	<!-- 기본생성자로 객체를 만들었다. -->
	<jsp:setProperty property="name"   name="psdto" value="${param.name}" />		<!-- 객체를 넣어준다. property 뒤 첫문자는 반드시 소문자. -->
	<jsp:setProperty property="school" name="psdto" value="${param.school}" />		<!-- 객체를 넣어준다. property 뒤 첫문자는 반드시 소문자. -->
	<jsp:setProperty property="color"  name="psdto" value="${param.color}" />		<!-- 객체를 넣어준다. property 뒤 첫문자는 반드시 소문자. -->
	<jsp:setProperty property="food"   name="psdto" value="${paramValues.food}" />		<!-- 복수개는 paramValues.~~ 이다. -->

	<!-- 위에서 set 한 것을 get 해와서 출력해주자. -->
	<ul>
		<li>성명 :		<jsp:getProperty property="name" 	name="psdto"/> </li>
		<li>학력 :		<jsp:getProperty property="school" 	name="psdto"/> </li>
		<li>좋아하는 색상 :<jsp:getProperty property="color" 	name="psdto"/> </li>
		<li>좋아하는 음식 :<jsp:getProperty property="strFood" name="psdto"/> </li>
	</ul>
	
	<br/>
	<hr style="border: solid 1px red;">
	<br/>
	
	<jsp:useBean id="psdto_2" class="chap03.PersonDTO" />
	<jsp:setProperty property="*" name="psdto_2" />		<!-- 객체를 넣어준다. property 뒤 첫문자는 반드시 소문자. -->
	<!-- setProperty property="*" : set~ 를 다 쓰겠다는 말이다.(DTO 클래스의) -->
	<%--
		위와 같이 <jsp:setProperty property="*" name="psdto_2" />	를 사용하기 위한 전제조건은 
		chap03.PersonDTO 클래스에 setXXX() 메소드의 XXX 이름과 form 태그에서 전달되는 name 값이 같아야 한다.
		* 예시:setName 의 name 과 form 태그의 name="name"
			  .......................................
			  setFood 의 food 와 form 태그의 name="food"		
	 --%>
	
	<ul>
		<li>성명 :		<jsp:getProperty property="name" 	name="psdto_2"/> </li>
		<li>학력 :		<jsp:getProperty property="school" 	name="psdto_2"/> </li>
		<li>좋아하는 색상 :<jsp:getProperty property="color" 	name="psdto_2"/> </li>
		<li>좋아하는 음식 :<jsp:getProperty property="strFood" name="psdto_2"/> </li>
	</ul>
</body>
</html>