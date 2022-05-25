<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%
	String ctxPath = request.getContextPath();
	//		/MyMVC
%>

<style type="text/css">
	p#my_title {
	      font-size: 16pt;
	      margin: 20px;
	   }
	   
	   span#storename1 {
	      color: blue;
	      font-weight: bold;
	   }
	   
	   span#storename2 {
	      color: red;
	      font-weight: bold;
	   }
	   
	   span#storename3 {
	      color: green;
	      font-weight: bold;
	   }
</style>

<jsp:include page="header.jsp" />

<%-- 아래의 ${name_scope_page} 은 <c:set var="변수명" value="${값}" scope="" /> 를 테스트 하기 위해서 사용하는 것임. --%>
<%-- 변수의 선언은 header.jsp 파일에 선언했음. --%>
<%-- ~Scope는 생략가능하지만, 쓰는 것이 좋다. (변수이름 충돌 방지) --%>
<p id="my_title">
   안녕하세요? <span id="storename1">${pageScope.name_scope_page}</span> 입니다.<br>
   안녕하세요? <span id="storename2">${requestScope.name_scope_request}</span> 입니다.<br>
   안녕하세요? <span id="storename3">${sessionScope.name_scope_session}</span> 입니다.<br>
</p>
<%-- 끝 --%>

	<div class="container">	
	  <div style="width: 70%; margin: 0 auto;">	
		<div id="carouselExampleIndicators" class="carousel slide carousel-fade" data-ride="carousel">
		  <ol class="carousel-indicators">
		  <%-- 
		    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
		    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
		    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li> 
		  --%>		  
		    <c:forEach items="${requestScope.imgList}" varStatus="status"> <%-- request 에 저장되어 있던 key 값 --%>
				<c:if test="${status.index == 0}">
			    	<li data-target="#carouselExampleIndicators" data-slide-to="${status.index}" class="active"></li> <%-- ${status.index} 는 0부터 시작한다. JSTL 참고 --%>
				</c:if>
				<c:if test="${status.index > 0}">
			    <li data-target="#carouselExampleIndicators" data-slide-to="${status.index}"></li> <%-- ${status.count} 는 1부터 시작한다. --%>	
			    </c:if>			  	 
		    </c:forEach>
		  </ol>
		  <div class="carousel-inner">
		 <%--
		    <div class="carousel-item active">
		      <img src="../images/Koala.jpg" class="d-block w-100" alt="..."> <!-- d-block 은 display: block; 이고  w-100 은 width 의 크기는 <div class="carousel-item active">의 width 100% 로 잡으라는 것이다. -->
		    </div>
		    <div class="carousel-item">
		      <img src="../images/Lighthouse.jpg" class="d-block w-100" alt="...">
		    </div>
		    <div class="carousel-item">
		      <img src="../images/Penguins.jpg" class="d-block w-100" alt="...">	      
		    </div>
		  
		 --%>  
		  <c:forEach var="imgvo" items="${requestScope.imgList}" varStatus="status">
				<c:if test="${status.index == 0}">	<%-- 첫번째 것이라면 --%>
				    <div class="carousel-item active">
				      <img src="<%= ctxPath %>/images/${imgvo.imgfilename}" class="d-block w-100" > <!-- d-block 은 display: block; 이고  w-100 은 width 의 크기는 <div class="carousel-item active">의 width 100% 로 잡으라는 것이다. -->
				    </div>
				</c:if>
				<c:if test="${status.index > 0}">
				    <div class="carousel-item">
				      <img src="<%= ctxPath %>/images/${imgvo.imgfilename}" class="d-block w-100" >
				    </div>
			    </c:if>	
		  </c:forEach>
		 </div>
		 
		  <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
		    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
		    <span class="sr-only">Previous</span>
		  </a>
		  <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
		    <span class="carousel-control-next-icon" aria-hidden="true"></span>
		    <span class="sr-only">Next</span>
		  </a>
		</div>
	  </div>		
	</div>
<jsp:include page="footer.jsp" />
