<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>

 
<jsp:include page="../header.jsp" />

<style type="text/css">

div#mvoInfo {
      width: 60%; 
      text-align: left;
      border: solid 0px red;
      margin-top: 30px; 
      font-size: 13pt;
      line-height: 200%;
   }
   
   span.myli {
      display: inline-block;
      width: 90px;
      border: solid 0px blue;
   }
   
/* ============================================= */
   div#sms {
      margin: 0 auto; 
      /* border: solid 1px red; */ 
      overflow: hidden; 
      width: 50%;
      padding: 10px 0 10px 80px;
   }
   
   span#smsTitle {
      display: block;
      font-size: 13pt;
      font-weight: bold;
      margin-bottom: 10px;
   }
   
   textarea#smsContent {
      float: left;
      height: 100px;
   }
   
   button#btnSend {
      float: left;
      border: none;
      width: 50px;
      height: 100px;
      background-color: navy;
      color: white;
   }
   
   div#smsResult {
      clear: both;
      color: red;
      padding: 20px;
   }

</style>


<script type="text/javascript" >

//  Ajax 를 사용한다.
// '휴대폰 메시지'  '전송' 버튼을 눌렀을 때 나오도록 한다.
	$(document).ready(function() {
		
		$("div#smsResult").hide();	// 처음에는 smsResult 를 감추도록 한다.
		
		$("button#btnSend").click( () => {
			
		//	console.log( $("input#reservedate").val() + " " + $("input#reservetime").val() );	
		//	2022-04-07 14:18	
			
			// 연월일
			// 날짜에서 '-' 인 대시부분을 빼야 문자 발송이 가능하다.
			let reservedate = $("input#reservedate").val();
			reservedate = reservedate.split("-").join("");	// split 은 배열타입 , split 으로 쪼갠 후--> join 후 reservedate 에 넣는다.
			
			let reservetime = $("input#reservetime").val();
			reservetime = reservetime.split(":").join("");
			
			const datetime = reservedate + reservetime;
			
		//	console.log(datetime);
		//  202204071418

		// 객체를 만들자.
			let dataObj;
			
		// 발송예약일에서 연월일 또는 발송시간에 체크하지 않았다면 바로 발송한다. (객체를 만든다.)
			if( reservedate == "" || reservetime == "" ) {
				dataObj = {"mobile":"${requestScope.mvo.mobile}", 	// getParameter 해오는 값.
						   "smsContent":$("textarea#smsContent").val()};
						
			}
			else {
				// 예약 발송한다.
				// datetime 의 초기값을 설정하고 싶으면 datePicker 를 사용하면 된다.
				dataObj = {"mobile":"${requestScope.mvo.mobile}", 	// getParameter 해오는 값.
						   "smsContent":$("textarea#smsContent").val(),
						   "datetime":datetime};
			}	
			// 웹발신 문자를 발송하겠다.
			$.ajax({
				url:"<%= request.getContextPath()%>/member/smsSend.up",
				type:"POST",
				data:dataObj,
				dataType:"json",
				success:function(json) {	
					// {"group_id":"R2GlpTxiYT9TvhdQ","success_count":1,"error_count":0} 처럼 문구가 나온다.
				
					if(json.success_count == 1) {	// 발송이 올바르게 성공했다면!
						$("div#smsResult").html("문자 전송이 성공되었습니다.");
					}
					else if(json.error_count != 0) {	// error 가 0이 아닌 숫자 ==> 발송에 실패 했다.
						$("div#smsResult").html("문자 전송에 실패했습니다.");
					}
					
					$("div#smsResult").show();			// 발송이 성공적으로 됐거나 실패했을 때 결과물을 보여준다. 
					$("textarea#smsContent").val("");	// 발송이 성공적으로 됐거나 실패 된 후, smsContent 에 남아있는 값(val) 을 "" 로 바꿔준다.
					
				},
				error: function(request, status, error){
		               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }	
				
			
			});
		
		});
		
		/////////////////////////////////////////////////////////
		// 
		
		
		
		
		
	});// end of $(document).ready(function() {}-------------------------------------

	
	// 회원목록 [검색된결과] 를 보여주는 함수 (URL 을 기억해야 한다. 다른 페이지에 갔다가 다시 돌아왔을때에도 그대로 기억할 수 있도록 한다.)		
	// function declaration	(/MyMVC 만큼 ctxPath)
	// 이 버튼을 클릭했을 때 아까 내가 보고있던 그 페이지를 그대로 보여주도록 한다.
	function goMemberList() {
		let goBackURL = "${requestScope.goBackURL}";	// memberOnerDetail 에서 넘겨받은 것.
	//	alert("확인용 : " + goBackURL);
	//	확인용 : /member/memberList.up?currentShowPageNo=5 sizePerPage=10 searchType=name searchword=유
	//	--> 공백 부분을 & 로 다시 바꿔주자.
	// // 자바스크립트에서는  replaceall 이 없고 replace 밖에 없다. 
    // !!! 자바스크립트에서 replace를 replaceall 처럼 사용하기 !!! (정규표현식 //gi 를 사용해서 replaceAll 처럼 사용한다.)  //
    
    // "korea kena" ==> "korea kena".replace("k","y") ==> "yorea kena"
    // "korea kena".replace(/k/gi, "y") ==> "yorea yena"  여기서 주의할 것은 /"k"/ 아니라 /k/ 와 같이 "" 가 없어야 한다.
	
    //	변수 goBackURL 에서 공백 " "을 모두 "&" 로 변경하도록 한다.
    	goBackURL = goBackURL.replace(/ /gi,"&");
    //	alert("최종 확인용 : " + goBackURL);   
    //	최종 확인용 : /member/memberList.up?currentShowPageNo=5&sizePerPage=10&searchType=name&searchword=유
    		
		location.href = "/MyMVC"+goBackURL;
		
	}
	
	
	

</script>
<%-- mvo 에 담겨진 회원정보가 있으면 보여주기 --%> 
  
<c:if test="${empty requestScope.mvo}">
	존재하지 않는 회원입니다.<br>
</c:if>

<c:if test="${not empty requestScope.mvo}">
	<c:set var="mobile" value="${requestScope.mvo.mobile}" />
	<c:set var="birthday" value="${requestScope.mvo.birthday}" />
	
	
	<h3>::: ${requestScope.mvo.name} 님의 회원 상세정보 :::</h3>

	<div id="mvoInfo">
		<ol>
		   <li><span class="myli">아이디 : </span>${requestScope.mvo.userid}</li>
	       <li><span class="myli">회원명 : </span>${requestScope.mvo.name}</li>
	       <li><span class="myli">이메일 : </span>${requestScope.mvo.email}</li>
	       <li><span class="myli">휴대폰 : </span>${fn:substring(mobile, 0, 3)}-${fn:substring(mobile, 3, 7)}-${fn:substring(mobile, 7, 11)}</li>
	       <li><span class="myli">우편번호 : </span>${requestScope.mvo.postcode}</li>
	       <li><span class="myli">주소 : </span>${requestScope.mvo.address}&nbsp;${requestScope.mvo.detailaddress}&nbsp;${requestScope.mvo.extraaddress}</li>
	       <li><span class="myli">성별 : </span><c:choose><c:when test="${requestScope.mvo.gender eq '1'}">남</c:when><c:otherwise>여</c:otherwise></c:choose></li>
	       <li><span class="myli">생년월일 : </span>${fn:substring(birthday, 0, 4)}.${fn:substring(birthday, 4, 6)}.${fn:substring(birthday, 6, 8)}</li>
	       <li><span class="myli">나이 : </span>${requestScope.mvo.age}세</li>
	       <li><span class="myli">코인액 : </span><fmt:formatNumber value="${requestScope.mvo.coin}" pattern="###,###" />원</li>
	       <li><span class="myli">포인트 : </span><fmt:formatNumber value="${requestScope.mvo.point}" pattern="###,###" />POINT</li>
	       <li><span class="myli">가입일자 : </span>${requestScope.mvo.registerday}</li>	       
		</ol>       	
	</div>
	
   <%-- ==== 휴대폰 SMS(문자) 보내기 ==== --%>
   <div id="sms" align="left">
        <span id="smsTitle">&gt;&gt;휴대폰 SMS(문자) 보내기 내용 입력란&lt;&lt;</span>
        <div style="margin: 10px 0 20px 0">
           발송예약일&nbsp;<input type="date" id="reservedate" />&nbsp;<input type="time" id="reservetime" />
        </div>
        <textarea rows="4" cols="40" id="smsContent"></textarea>
        <button id="btnSend">전송</button>
        <div id="smsResult"></div>
   </div>

</c:if>

<div>
   <button style="margin-top: 50px;" type="button" onclick="javascript:history.back();">회원목록[history.back()]</button>
   &nbsp;&nbsp;
   <button style="margin-top: 50px;" type="button" onclick="goMemberList()">회원목록[검색된결과]</button>
   &nbsp;&nbsp;
   <button style="margin-top: 50px;" type="button" onclick="javascript:location.href='memberList.up'">회원목록[처음으로]</button> 
</div>

<jsp:include page="../footer.jsp" />        