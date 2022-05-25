<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../header2.jsp" />

<style type="text/css">

	label.prodInfo {
		display: inline-block;
		width: 65px;
		margin-left: 5px;
	/* 	border: solid 1px gray; */
	}

</style>

<script type="text/javascript">

	$(document).ready(function() {	// 문서가 로딩될때 아래의 함수를 실행해라.
		
			$("span#totalHITCount").hide();	// 맨처음에는 보이지 않도록 한다. 36 (전체 상품의 개수)
			$("span#countHIT").hide();		// 8 , 16, 24 ... (내가 본 제품의 누적 개수)

		// HIT상품 게시물을 더보기 위하여 "더보기..." 버튼 클릭액션에 대한 초기값 호출하기 
 		// 쇼핑몰홈[더보기] 버튼을 누르자마자 화면에 제품을 8개 보여줘야 한다.	
     	// 즉, 맨처음에는 "더보기..." 버튼을 클릭하지 않더라도 클릭한 것 처럼 8개의 HIT상품을 게시해주어야 한다는 말이다.
		displayHIT("1");	// 아래에 함수를 만들어준다.
		
		//	HIT 상품 게시물을 더보기 위하여 "더보기..." 버튼 클릭액션에 대한 이벤트 등록하기
		$("button#btnMoreHIT").click(function() {
		
			const $target = $(event.target);		// 현재 클릭한 곳(event.target --> "더보기..." 버튼)
			
			if($target.text() == "처음으로") {		// 맨 처음에는 '더보기...' 버튼 (끝까지 조회했을 경우 "처음으로" 버튼이 나온다.)
				// 지금까지 조회한 모든 제품목록에서 empty 상태로 만든 후, 맨 처음으로 돌아간다.
				$("div#displayHIT").empty();
				$("span#end").empty();
				displayHIT("1");		// "처음으로"  ---> "더보기" 로 바꿔준다.
				$target.text("더보기...");
 			}
			else {
				displayHIT($target.val());	// HIT 에 해당하는 상품목록들을 보이도록 해라.
				//	displayHIT("9");
				//	displayHIT("17");
				//	displayHIT("25");	.......
			}
		});	
	});// end of  $(document).ready(function(){})------------------------

	// function declaration 
	const lenHIT = 8;	// HIT 상품 "더보기..." 버튼을 클릭할 때 보여줄 상품의 개수(단위)크기, 여기서는 8로 고정한다.
	
	// display 할 HIT 상품 정보를 추가요청하기(Ajax 로 처리한다. --> 해당 URL 페이지는 그대로 있으면서, 더보기를 누를때마다 사진이 8개씩 늘어난다. append 처리가 된다. javaScript 로 처리하면 된다.)
	function displayHIT(start) {	// start 가 처음에 "1"  이라면 1~8 까지 상품 8개를 보여준다. (위에 displayHIT("1") 참고)
									// start 가 처음에 "9"  라면 9~16 까지 상품 8개를 보여준다. 
									// start 가 처음에 "17" 이라면 17~24 까지 상품 8개를 보여준다. 
									// start 가 처음에 "25" 라면 25~32 까지 상품 8개를 보여준다. 
									// start 가 처음에 "33" 이라면 33~36 까지 상품 4개를 보여준다. (최대 갯수가 36개였다. 마지막 상품)
		$.ajax({
			url:"/MyMVC/shop/mallDisplayJSON.up",		// 객체타입(jSON) 으로 받는다.
		//	type:"get",
			data:{"sname":"HIT"	// DB, sname 은 where 절 HIT	/ where sname = 'HIT'
				 ,"start":start	// 		"1" "9" "17" "25" "33"
				 ,"len":lenHIT},	// 고정값  8	 8	 8	  8	   8		
			dataType:"JSON",	// 결과물 타입 (JSON 타입으로 나와야 한다.)
			success:function(json) {
			
			//	console.log(json);
			//	console.log(typeof json);	// object	
				const str_json = JSON.stringify(json); // JSON 객체를 String 타입으로 변경해주는 것.
			//	console.log(typeof json);			   // String
			//	console.log(str_json);
				
				const obj_json = JSON.parse(str_json); //	JSON 모양으로 된 String 을 JSON 객체로 변경시켜주는 것.
			//	console.log(typeof obj_json);			// object
			//	console.log(obj_json);
	
			/*
				json => [{"pnum":36,"code":"100000","discountPercent":17,"pname":"노트북30","pcompany":"삼성전자","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"59.jpg","pqty":100,"pimage2":"60.jpg","pcontent":"30번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":35,"code":"100000","discountPercent":17,"pname":"노트북29","pcompany":"레노버","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"57.jpg","pqty":100,"pimage2":"58.jpg","pcontent":"29번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":34,"code":"100000","discountPercent":17,"pname":"노트북28","pcompany":"아수스","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"55.jpg","pqty":100,"pimage2":"56.jpg","pcontent":"28번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":33,"code":"100000","discountPercent":17,"pname":"노트북27","pcompany":"애플","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"53.jpg","pqty":100,"pimage2":"54.jpg","pcontent":"27번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":32,"code":"100000","discountPercent":17,"pname":"노트북26","pcompany":"MSI","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"51.jpg","pqty":100,"pimage2":"52.jpg","pcontent":"26번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":31,"code":"100000","discountPercent":17,"pname":"노트북25","pcompany":"삼성전자","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"49.jpg","pqty":100,"pimage2":"50.jpg","pcontent":"25번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":30,"code":"100000","discountPercent":17,"pname":"노트북24","pcompany":"한성컴퓨터","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"47.jpg","pqty":100,"pimage2":"48.jpg","pcontent":"24번 노트북","price":1200000,"sname":"HIT"}
						,{"pnum":29,"code":"100000","discountPercent":17,"pname":"노트북23","pcompany":"DELL","saleprice":1000000,"point":60,"pinputdate":"2022-04-04","pimage1":"45.jpg","pqty":100,"pimage2":"46.jpg","pcontent":"23번 노트북","price":1200000,"sname":"HIT"}]
	
				또는
				
				json => []			
			*/
				
				let html = "";
				
				if(start == "1" && json.length == 0 ) {
					// start 가 1부터 시작하고 json 의 배열길이가 0 , 데이터가 존재하지 않는다.
					// 처음부터 데이터가 존재하지 않는 경우.
					// ** 주의 **//
					// if(json == null) 이 아니라,
					// if(json.length == 0) 으로 해야한다!!
					html += "상품 준비중입니다...";
					
					// HIT 상품 결과를 출력해준다.
					$("div#displayHIT").html(html);
					
				}
				
				else if (json.length > 0) {
					// 배열이 존재한다. (최소 1개이상 존재), 데이터가 존재하는 경우
					// jQuery 사용, 결과물이 배열 [] 이다.
					$.each(json, function(index, item){
						
						html +=  "<div class='col-md-6 col-lg-3'>"+
			                        "<div class='card mb-3'>"+
			                            "<img src='/MyMVC/images/"+item.pimage1+"' class='card-img-top' style='width: 100%'/>"+
			                            "<div class='card-body' style='padding: 0; font-size: 11pt;'>"+
			                              "<ul class='list-unstyled mt-3 pl-3'>"+
			                                 "<li><label class='prodInfo'>제품명</label>"+item.pname+"</li>"+
			                                 "<li><label class='prodInfo'>정가</label><span style=\"color: red; text-decoration: line-through;\">"+(item.price).toLocaleString('en')+" 원</span></li>"+	// 화폐단위 숫자 세 자리마다 콤마 (toLocaleString)
			                                   "<li><label class='prodInfo'>판매가</label><span style=\"color: red; font-weight: bold;\">"+(item.saleprice).toLocaleString('en')+" 원</span></li>"+
			                                   "<li><label class='prodInfo'>할인율</label><span style=\"color: blue; font-weight: bold;\">["+item.discountPercent+"%] 할인</span></li>"+
			                                   "<li><label class='prodInfo'>포인트</label><span style=\"color: orange;\">"+item.point+" POINT</span></li>"+ 
			                                   "<li class='text-center'><a href='/MyMVC/shop/prodView.up?pnum="+item.pnum+"' class='stretched-link btn btn-outline-dark btn-sm' role='button'>자세히보기</a></li>"+ 
			                                   <%-- 카드 내부의 링크에 .stretched-link 클래스를 추가하면 전체 카드를 클릭할 수 있고 호버링할 수 있습니다(카드가 링크 역할을 함). --%>         
			                              "</ul>"+
			                            "</div>"+
			                         "</div>"+ 
			                      "</div>";
					});// end of ea$.each(json, function(index, item){})---------------
                    
   					// HIT 상품 결과를 출력해준다.
   					$("div#displayHIT").append(html);	// 덮어씌우는게 아님 --> 기존꺼를 덮고 overwrite 하는 것이 아니라, 기존것에다가 새것을 덧붙여 나가는 것(append)
   					// prepend : 앞에 덧붙여 나가는 것 / append : 뒤에 덧붙여 나가는 것 		
   					
   					// 8개씩 상품 결과를 출력해준 다음,
   					// ***** 매우 중요 !!! '더보기...' 버튼의 value 속성에 값을 지정하기 !!! *****//
   					$("button#btnMoreHIT").val( Number(start) + lenHIT );		// start 는 문자열임.
   					//  '더보기...' 버튼의 value 값이 9로 변경된다. ( 1 + 8 )	--> 8개씩 더보기 클릭 후 누적 누적 누적
   					//  '더보기...' 버튼의 value 값이 17로 변경된다. ( 9 + 8 )
   					//  '더보기...' 버튼의 value 값이 25로 변경된다. ( 17 + 8 )
   					//  '더보기...' 버튼의 value 값이 33으로 변경된다. ( 25 + 8 )
   					//  '더보기...' 버튼의 value 값이 41로 변경된다. (존재하지 않는 것이다.)	--> 그러나 41은 존재하지 X.
 
   					// countHIT에 지금까지 출력된 상품의 개수를 누적해서 기록한다.
   					$("span#countHIT").text( Number($("span#countHIT").text()) + json.length );
   					
   					// '더보기...' 버튼을 계속 클릭하여 countHIT 값과 totalHITCount 값이 일치하는 경우
   					if($("span#countHIT").text() == $("span#totalHITCount").text()) {
   						$("span#end").html("더이상 조회할 제품이 없습니다.");
   						$("button#btnMoreHIT").text("처음으로");		// '더보기...' 버튼의 글자를 '처음으로' 로 바꾼다.
   						$("span#countHIT").text("0");	// 0으로 바꿔준다.
   					} 
   					
				}
			
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	         }			
		});
		
		
	}// end of function displayHIT(start){}-----------------------------
	
	
	
	
</script>

<%-- HIT 상품을 모두 가져와서 디스플레이(더보기) 방식으로 페이징 처리 한 것 --%>

	<div>
		<p class="h3 my-3 text-center">- HIT 상품 -</p>
		
		<div class="row" id="displayHIT"></div>
	
		<div>
			<p class="text-center">
				<span id="end" style="display:block; margin:20px; font-size: 14pt; font-weight: bold; color: red;"></span>
				<button type="button" class="btn btn-secondary btn-lg" id="btnMoreHIT" value="">더보기...</button>
				<span id="totalHITCount">${requestScope.totalHITCount}</span>
				<span id="countHIT">0</span>			
			</p>
		</div>
	</div>

<jsp:include page="../footer2.jsp" />