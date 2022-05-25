<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<%-- DB 에서 셀렉트된 카테고리 목록을 가져와야 한다. --%>
<%-- return 값이 복수개인 list 이다. forEach 를 사용할 것. --%>
<c:if test="${not empty requestScope.categoryList}">
	<div style="width: 95%;
               border: solid 1px gray;
               padding-top: 5px;
               padding-bottom: 5px;
               text-align: center;
               color: navy;
               font-size: 14pt;
               font-weight: bold;">
      CATEGORY LIST
   </div>
   
   <div style="width: 95%;
               border: solid 1px gray;
               border-top: hidden;
               padding-top: 5px;
               padding-bottom: 5px;
               text-align: center;">
		<%-- map 은 key 값을 적어준다. (DAO 에서 categoryList 를 넣을 때 map에 key 값을 넣어옴.) --%>
		<%-- 부모클래스에서 DB 로 부터 불러온 이름을 보여준다. --%>
		<%-- 각 카테고리 cnum 에 맞게 카테고리 cname 을 불러온다. --%>
		<a href="<%= request.getContextPath()%>/shop/mallHome1.up">전체</a>&nbsp;
		<c:forEach var ="map" items="${requestScope.categoryList}">
			<a href="<%= request.getContextPath()%>/shop/mallByCategory.up?cnum=${map.cnum}">${map.cname}</a>&nbsp;
		</c:forEach>
	</div>
	
</c:if>