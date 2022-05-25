<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코인충전 결제하기</title>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/style.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script>

<style type="text/css">

span {margin-right: 10px;}
                   
.stylePoint {background-color: red; 
             color: white;}
             
.purchase {cursor: pointer;
           color: red;}

</style>

<script type="text/javascript">

	$(document).ready(function() {
		
		$("td#error").hide();	// 맨 처음에는 에러메시지가 보이지 않도록 한다.
		
		let coinmoney = 0;	
		
		$("input:radio[name ='coinmoney']").bind("click", ()=>{	// 클릭하면 function 을 해라.
			
			const $target = $(event.target);	// radio 세개 중에 어느 것을 선택(target)했는지?

			coinmoney = $target.val();			// radio 선택시 coinmoney 가 얼마인지 잡힌다.
					
			let index = $("input:radio[name ='coinmoney']").index($target);	// radio 는 복수개인데, 세개 중에 실제로 클릭해 온 것 (그러므로 선택 갯수에 따라 0,1,2 가 나옴)		
			// 확인용 index => 0
			// 확인용 index => 1
			// 확인용 index => 2
			
			$("td>span").removeClass("stylePoint");			// 일단 제거하고, radio 에서 선택한 index 만 나타내도록 하겠다. (금액을 누를때 POINT 란에 빨간색 잔상이 다 남는 것을 방지)
			$("td>span").eq(index).addClass("stylePoint");	// eq : 배열에서 요소를 잡는 것과 같은 것.	해당 span 3개 중 0, 1 ,2 를 말하는 것이다.
		//  $("td>span").eq(index); ==> $("td>span")중에  index 번째의 요소인 엘리먼트를 선택자로 보는 것이다.
	    //                              $("td>span")은 마치 배열로 보면 된다. $("td>span").eq(index) 은 배열중에서 특정 요소를 끄집어 오는 것으로 보면 된다. 예를 들면 arr[i] 와 비슷한 뜻이다.			
			$("td#error").hide();	// 에러메세지가 보였다면, 금액의 radio 버튼을 누른 후에 이제는 감추자.
			
		});	
		
		$("td#purchase").hover(function() {
								$(event.target).addClass("purchase");	// 마우스가 올라간 곳에 addClass
							}, function() {
								$(event.target).removeClass("purchase");	// 원상복구	
							});
		
		// 라디오를 선택하지 않고 바로 충전결제 버튼을 누르면 error 버튼을 띄우도록 한다.
		$("td#purchase").click(function() {		// 얘를 클릭하면 function 해라.
			const checkedCnt = $("input:radio[name ='coinmoney']:checked").length;			// radio 는 name 이 항상 같아야 한다.  checkedCnt(체크된 갯수)
			
			if(checkedCnt == 0) {
				// 결제금액을 선택하지 않은 경우!!
				$("td#error").show();	// 에러 메세지를 띄우고 그 다음 단계를 진행하지 않는다.
				return; // 종료
			}
			else {
				// 결제금액을 선택했을 경우에는 결제단계로 넘어간다. (이때 금액에 따른 point 도 알아야한다. 위의 함수 참고.)
				// login.jsp 에 있는 아임포트 결제 함수릃 호출해온다.
				
			/*	=== 팝업창에서 부모창 함수 호출 방법 3가지 ===
	            1-1. 일반적인 방법
	            opener.location.href = "javascript:부모창스크립트 함수명();";
	                           
	            1-2. 일반적인 방법
	            window.opener.부모창스크립트 함수명();

	            2. jQuery를 이용한 방법
	            $(opener.location).attr("href", "javascript:부모창스크립트 함수명();");
	         */
			//	opener.location.href = "javascript:goCoinPurchaseEnd("+coinmoney+")";		// opener : 부모창(HOMEPAGE) / 자식착은 팝업창(코인충천 결제방식 선택 팝업창)
	        //	또는
			//	window.opener.goCoinPurchaseEnd("+coinmoney+");
			//	또는
	        	$(opener.location).attr("href", "javascript:goCoinPurchaseEnd("+coinmoney+")");
			// 함수에 coinmoney 가 얼마인지를 넣어준다.(위에서 coinmoney 변수처리함)
			
				self.close();	// 팝업창을 닫는 것이다.	(즉, 팝업창을 닫고 부모창에서 goCoinPurchaseEnd(coinmoney) 함수를 호출한다.)
			
			}
			
		});	
		
	});

</script>


</head>
<body>
   <div class="container">
     <h2 class="my-5">코인충천 결제방식 선택</h2>
     <p>코인충전 금액 높을수록 POINT를 무료로 많이 드립니다.</p> 
     
     <div class="table-responsive" style="margin-top: 30px;">           
        <table class="table">
          <thead>
            <tr>
              <th>금액</th>
              <th>POINT</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                 <label class="radio-inline"><input type="radio" name="coinmoney" value="300000" />&nbsp;300,000원</label>
            </td>
              <td>
               <span>3,000</span>
            </td>
            </tr>
            <tr>
              <td>
               <label class="radio-inline"><input type="radio" name="coinmoney" value="200000" />&nbsp;200,000원</label>
              </td>
              <td>
                 <span>2,000</span>
            </td>
            </tr>
            <tr>
              <td>
               <label class="radio-inline"><input type="radio" name="coinmoney" value="100000" />&nbsp;100,000원</label>
              </td>
              <td>
                 <span>1,000</span>
            </td>
            </tr>
            <tr>
               <td id="error" colspan="3" align="center" style="height: 50px; vertical-align: middle; color: red;">결제종류에 따른 금액을 선택하세요!!</td>
            </tr>
            <tr>
              <td id="purchase" colspan="3" align="center" style="height: 100px; vertical-align: middle;">[충전결제하기]</td>
            </tr>
          </tbody>
        </table>
     </div>
   </div>
</body>
</html>