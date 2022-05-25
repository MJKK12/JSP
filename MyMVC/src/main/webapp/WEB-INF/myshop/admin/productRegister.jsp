<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<jsp:include page="../../header2.jsp" />
<%-- 제품등록 페이지 --%>

<style type="text/css">

    table#tblProdInput {border: solid gray 1px; 
                       border-collapse: collapse; }
                       
    table#tblProdInput td {border: solid gray 1px; 
                          padding-left: 10px;
                          height: 50px; }
                          
    .prodInputName {background-color: #e6fff2; 
                    font-weight: bold; }                                                 
   
    .error {color: red; font-weight: bold; font-size: 9pt;}
   
</style>

<script>

	$(document).ready(function() {
		
		$("span.error").hide();	// '필수입력' 부분을 처음에는 hide 한다.
		
		// 제품 수량에 스피너(spinner) 달아주기 (jQueryUI 에서 참고한다. link&script 부분은 header2.jsp에 있음.)
		$("input#spinnerPqty").spinner({	// 갯수가 음수가 될수는 없으므로, 범위값을 지정해준다.
			// jQueryUI 에서 참고한 범위값 지정 코드
			spin:function(event,ui){
	            if(ui.value > 100) {	// 100개가 최대치로 설정되도록한다.(더이상 개수를 늘릴 수 없음.)
	               $(this).spinner("value", 100);	//$(this)는 자기자신임 == $("input#spinnerPqty")
	               return false;
	            }
	            else if(ui.value < 1) {	// 0이거나 음수 이면, 무조건 최소수량을 1을 주겠다.
	               $(this).spinner("value", 1);
	               return false;
	            }
	         }
		});// end of $("input#spinnerPqty").spinner({})------------------
		
		
		// 추가 이미지 파일에 spinner 달아주기
		$("input#spinnerImgQty").spinner({	// 갯수가 음수가 될수는 없으므로, 범위값을 지정해준다.
			// jQueryUI 에서 참고한 범위값 지정 코드
			spin:function(event,ui){
	            if(ui.value > 10) {					// 첨부파일 이미지가 10개가 최대치로 설정되도록한다.(더이상 개수를 늘릴 수 없음.)
	               $(this).spinner("value", 10);	//$(this)는 자기자신임 == $("input#spinnerPqty")
	               return false;
	            }
	            else if(ui.value < 0) {				// 첨부될 추가이미지가 없을수도 있다. (==0)
	               $(this).spinner("value", 0);
	               return false;
	            }
	         }
		});// end of $("input#spinnerPqty").spinner({})------------------
		
		// 파일갯수를 증가한 수 만큼 첨부할 file 타입도 증가하도록 한다.
		// #### 스피너(spinner)의 이벤트는 click 도 아니고, change 도 아니고, "spinstop" 이다.	
		$("input#spinnerImgQty").bind("spinstop", function(){	// 화살표 함수로 바뀌면 $(this) 를 받지 못한다. 그땐 $("input#spinnerImgQty") 를 다 적어줘야 한다.
			
			let html = "";
			const cnt = $(this).val();
		
		/*
			console.log("확인용 cnt => " + cnt);		// 확인용 cnt => 1,2,3........,10
			console.log("확인용 typeof cnt => " + typeof cnt);	// web 은 숫자라고 하더라도 항상 String 타입이다.
			// 확인용 typeof cnt => string
		*/
			for(let i=0; i<parseInt(cnt); i++) {		// Number(cnt) 또는 parseInt(cnt) --> String 에서 정수로 바꾸기 위함.
				html += "<br>";
				html += "<input type='file' name='attach"+i+"' class='btn btn-default' />";		// file 을 보내야 하므로 name 을 넣는다.
			}// end of for(let i=0; i<parseInt(cnt); i++) {}------
			
			$("div#divfileattach").html(html);
			$("input#attachCount").val(cnt);	// spinner 의 개수만큼 집어넣는다. (hidden 타입으로 해놓지만 존재한다.)
		});
		
		// 제품 등록하기 (필수 사항들이 입력되었는지 검사한다.)
		$("input#btnRegister").click(function() {
			
			let flag = false;
			
			$(".infoData").each(function(index,item) {		// 어떤 태그인지는 알 수 없지만, .infoData 를 가지고 있는 것 --> 하나하나 검사한다. (반복문 사용)
				const val = $(item).val().trim();			// value 값에서 공백을 제거한다.
				
				if(val == "") {
					// value 값이 없다면(""), 해당 태그 다음에 있는 next 태그를 보여줘라. (error를 설정해놓음)
					$(item).next().show();
					flag = true;	// return false 되기 전, 뭔가가 잘못 되면 깃발을 false 에서 true로 바꾸는 것!
					return false;	// each() 문에서 break; 와 같은 것.
				}				
			});
			
			// 모든것을 올바르게 다 채웠으면 submit 하자.
			if(!flag) {
				var frm = document.prodInputFrm;
				frm.submit();
			
			}
			
		});
		
		// 제품 등록 취소하기 ('취소하기' 버튼을 클릭했을 때 함수 실행)
		$("input[type='reset']").click(function() {
			$("span.error").hide();			// error 부분 원상복구
			$("div#divfileattach").empty();	// 파일 첨부부분도 비우자.
		});
				
	});// end of $(document).ready(function(){})-------------------

</script>

<div align="center" style="margin-bottom: 20px;">

<div style="border: solid green 2px; width: 250px; margin-top: 20px; padding-top: 10px; padding-bottom: 10px; border-left: hidden; border-right: hidden;">       
   <span style="font-size: 15pt; font-weight: bold;">제품등록&nbsp;[관리자전용]</span>   
</div>
<br/>

<%-- !!!!! ==== 중요 ==== !!!!! --%>
<%-- 폼에서 파일을 업로드 하려면 반드시 method 는 POST 이어야 하고 
     enctype="multipart/form-data" 으로 지정해주어야 한다.!! --%>	<%-- action : 자기자신에게 가도록 한다. --%>
<form name="prodInputFrm"
	  action="<%= request.getContextPath()%>/shop/admin/productRegister.up"
	  method="POST" 
	  enctype="multipart/form-data"> 
      
	<table id="tblProdInput" style="width: 80%;">
	<tbody>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 10px;">카테고리</td>
	      <td width="75%" align="left" style="padding-top: 10px;" >
	         <select name="fk_cnum" class="infoData">
	            <option value="">:::선택하세요:::</option>	<%-- var 가 vo 라면 vo.(get)xxx / map으로 가져왔다면, map."key"값 --%>
				<%-- 아래처럼 일일이 적지 말고 DB 에서 다 불러온다.
	               <option value="1">전자제품</option>
	               <option value="2">의  류</option>
	               <option value="3">도  서</option>
	            --%>
	            <c:forEach var="map" items="${requestScope.categoryList}">
	            	<option value="${map.cnum}">${map.cname}</option>	            
	            </c:forEach>
	         </select>
	         <span class="error">필수입력</span>
	      </td>   
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;" >
	         <input type="text" style="width: 300px;" name="pname" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제조사</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 300px;" name="pcompany" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품이미지</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="pimage1" class="infoData" /><span class="error">필수입력</span>
	         <input type="file" name="pimage2" class="infoData" /><span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품설명서 파일첨부(선택)</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="prdmanualFile" />
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품수량</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	              <input id="spinnerPqty" name="pqty" value="1" style="width: 30px; height: 20px;"> 개
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품정가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 100px;" name="price" class="box infoData" /> 원
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품판매가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 100px;" name="saleprice" class="box infoData" /> 원
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품스펙</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <select name="fk_snum" class="infoData">
	            <option value="">:::선택하세요:::</option>
	            <%-- 아래처럼 일일이 value 1,2,3 적는게 아니라 forEach 문을 사용한다. (DB에서 계속 바뀔 수 있기 때문)
	               <option value="1">전자제품</option>
	               <option value="2">의  류</option>
	               <option value="3">도  서</option>
            	--%>
	            <c:forEach var="spvo" items="${requestScope.specList}">
	            	<option value="${spvo.snum}">${spvo.sname}</option>	<%-- value 는 코드값 --%>
	            </c:forEach>
	         </select>
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품설명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <textarea name="pcontent" rows="5" cols="60"></textarea>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-bottom: 10px;">제품포인트</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden; padding-bottom: 10px;">
	         <input type="text" style="width: 100px;" name="point" class="box infoData" /> POINT
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   
	   <%-- ==== 첨부파일 타입 추가하기 ==== --%>
	    <tr>
	          <td width="25%" class="prodInputName" style="padding-bottom: 10px;">추가이미지파일(선택)</td>
	          <td>
	             <label for="spinnerImgQty">파일갯수 : </label>
	          	 <input id="spinnerImgQty" value="0" style="width: 30px; height: 20px;">
	             <div id="divfileattach"></div>
	              
	             <input type="hidden" name="attachCount" id="attachCount" />	<%-- 첨부파일 갯수를 넣는 곳 --%>
	              
	          </td>
	    </tr>
	   
	   <tr style="height: 70px;">
	      <td colspan="2" align="center" style="border-left: hidden; border-bottom: hidden; border-right: hidden;">
	          <input type="button" value="제품등록" id="btnRegister" style="width: 80px;" /> 
	          &nbsp;
	          <input type="reset" value="취소" style="width: 80px;" />   
	      </td>
	   </tr>
	</tbody>
	</table>
</form>
</div>

<jsp:include page="../../footer2.jsp" />