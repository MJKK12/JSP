<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 회원가입 form을 띄우고자 한다. -->
<% 
	String ctxPath = request.getContextPath();
	// MyMVC
%>

<jsp:include page="../header.jsp" />

<style type="text/css">

table#tblMemberRegister {
          width: 93%;
          
          /* 선을 숨기는 것 */
          border: hidden;
          
          margin: 10px;
   }  
   
   table#tblMemberRegister #th {
         height: 40px;
         text-align: center;
         background-color: silver;
         font-size: 14pt;
   }
   
   table#tblMemberRegister td {
         /* border: solid 1px gray;  */
         line-height: 30px;
         padding-top: 8px;
         padding-bottom: 8px;
   }
   
   .star { color: red;
           font-weight: bold;
           font-size: 13pt;
   }

</style>

<script type="text/javascript" src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript">

	// 전역변수를 하나 만든다.
	let b_flagIdDuplicateClick = false;
	// 가입하기 버튼 클릭 시, '아이디 중복확인'을 클릭했는지 알아보기 위한 용도
	// 클릭했다는 뜻은 false -> true 로 되게끔 한다.

	let b_flagEmailDuplicateClick = false;
	// 가입하기 버튼 클릭 시, '이메일 중복확인'을 클릭했는지 알아보기 위한 용도

	$(document).ready(function() {
		
		$("span.error").hide();	/*id가 error 인 것은 일단 숨기도록 한다.*/		
		$("input#name").focus(); /*input 태그에 name 인 것에 focus 를 준다.*/
		
		// 아이디가 name 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#name").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/
		
			const name = $target.val().trim();	/*성명쓸 때 공백이 들어가면 안되기 때문에 기본적으로 공백 제거를 위해 .trim()으로 한다.*/
			if(name == "") {
				// 입력하지 않거나 공백만 입력했을 경우를 막기 위함이다.
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$target.focus();				
			}
			// 이름을 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
			else {
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 성명을 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});
		
		// 아이디가 userid 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#userid").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/
		
			const userid = $target.val().trim();	/*성명쓸 때 공백이 들어가면 안되기 때문에 기본적으로 공백 제거를 위해 .trim()으로 한다.*/
			if(userid == "") {
				// 입력하지 않거나 공백만 입력했을 경우를 막기 위함이다.
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$target.focus();				
			}
			// 아이디를 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
			else {
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 성명을 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});

		// 아이디가 pwd 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#pwd").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/

		 // const regExp = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
		 // 또는
	        const regExp = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
	     // 숫자/문자/특수문자/ 포함 형태의 8~15자리 이내의 암호 정규표현식 객체 생성
	       
	     	const bool = regExp.test($target.val());	// true, false 값으로 반환 (기본값 true)
		         
		    if(!bool) {
				// 암호가 정규표현식에 위배된 경우
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$target.focus();				
			}

			// 비밀번호를 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
		    else {
			// 암호가 정규표현식에 맞는 경우
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 암호를 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});

		
		// 아이디가 pwdcheck 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#pwdcheck").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/

			const pwd = $("input#pwd").val();	// 원래 암호값(value)

			const pwdcheck = $target.val();		// 새로 입력한 암호확인값(value)
			
		    if(pwd != pwdcheck) {
				// 암호와 암호확인값이 다른 경우
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				$("input#pwd").prop("disabled",false)	// 암호란 - 암호확인란 둘 다 활성화 시키겠다.
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$("input#pwd").focus();			// 포커스는 암호에 다시 주겠다.	
			}

			// 비밀번호를 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
		    else {
			// 암호와 암호확인값이 같은 경우
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 암호확인란을 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});

		
		// 아이디가 email 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#email").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/

			// const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	        // 또는
	        const regExp = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i); 
	        // 이메일 정규표현식 객체 생성
	       
	     	const bool = regExp.test($target.val());	// true, false 값으로 반환 (기본값 true)
		         
		    if(!bool) {
				// 이메일이 정규표현식에 위배된 경우
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$target.focus();				
			}

			// 이메일을 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
		    else {
			// 이메일이 정규표현식에 맞는 경우
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 이메일을 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});

		
		// 아이디가 hp2 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#hp2").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/

			// const regExp = /^[1-9][0-9]{3}$/g;
	        // 또는
	        const regExp = new RegExp(/^[1-9][0-9]{3}$/g); 
	        // 연락처 정규표현식 객체 생성 (숫자 4자리만 들어오도록 검사한다. 첫글자는 숫자 1-9만 가능하다.)
	       
	     	const bool = regExp.test($target.val());	// true, false 값으로 반환 (기본값 true)
		         
		    if(!bool) {
				// 국번이 정규표현식에 위배된 경우
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$target.focus();				
			}

			// 국번을 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
		    else {
			// 국번이 정규표현식에 맞는 경우
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 국번을 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});		

		// 아이디가 hp3 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트 처리해주는 것이다.
		$("input#hp3").blur( ()=> {		/*focus가 id에 와있다가 focus 를 잃어버렸을 때나 tab 했을때 발생되는 이벤트가 blur 이다.*/
			
			const $target = $(event.target);	/*blur 가 된 곳(성명란)*/

			// const regExp = /^\d{4}$/g;
	        // 또는
	        const regExp = new RegExp(/^\d{4}$/g); 
	        // 연락처 정규표현식 객체 생성 (숫자 4자리만 들어오도록 검사한다.)
	       
	     	const bool = regExp.test($target.val());	// true, false 값으로 반환 (기본값 true)
		         
		    if(!bool) {
				// 마지막 전화번호 4자리가 정규표현식에 위배된 경우
				$("table#tblMemberRegister :input").prop("disabled", true); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
				$target.prop("disabled",false)
				
			//	$target.next().show();
			//	또는	
				$target.parent().find(".error").show();	//".error" 인 부분을 찾아서 보여라
				
				$target.focus();				
			}

			// 마지막 전화번호 4자리를 올바르게 적었을 경우 error 문구를 지우고 다음 탭으로 넘어가야 한다.
		    else {
			// 마지막 전화번호 4자리가 정규표현식에 맞는 경우
				$("table#tblMemberRegister :input").prop("disabled", false); /*tblMemberRegister 테이블 속에 있는 모든 input 태그*/
			
			//	$target.next().show();
			//	또는	
					$target.parent().find(".error").hide();	// 국번을 올바르게 입력 했으므로 에러 문구를 지운다.
			}
				
		});				

		// 우편번호 찾기
		$("img#zipcodeSearch").click(function () {
			// 우편번호 띄우기 (카카오에서 불러온 코드-오픈 API)
	      new daum.Postcode({
	            oncomplete: function(data) {
	                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

	                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	                let addr = ''; // 주소 변수
	                let extraAddr = ''; // 참고항목 변수

	                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	                    addr = data.roadAddress;
	                } else { // 사용자가 지번 주소를 선택했을 경우(J)
	                    addr = data.jibunAddress;
	                }

	                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
	                if(data.userSelectedType === 'R'){
	                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
	                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
	                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
	                        extraAddr += data.bname;
	                    }
	                    // 건물명이 있고, 공동주택일 경우 추가한다.
	                    if(data.buildingName !== '' && data.apartment === 'Y'){
	                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
	                    }
	                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
	                    if(extraAddr !== ''){
	                        extraAddr = ' (' + extraAddr + ')';
	                    }
	                    // 조합된 참고항목을 해당 필드에 넣는다.
	                    document.getElementById("extraAddress").value = extraAddr;
	                
	                } else {
	                    document.getElementById("extraAddress").value = '';
	                }

	                // 우편번호와 주소 정보를 해당 필드에 넣는다.
	                document.getElementById('postcode').value = data.zonecode;
	                document.getElementById("address").value = addr;
	                // 커서를 상세주소 필드로 이동한다.
	                document.getElementById("detailAddress").focus();
	            }
	        }).open();
		});
		

		/////////////////////////////////////////////////////////////////////////////////
				
		let mm_html = "";	/* 생년월일 중 월만 쓰고자 한다. (12번 반복) */
		for(let i=1; i<=12; i++) {
			if(i<10) {
				mm_html += "<option value ='0"+i+"'>0"+i+"</option>";			
			}
			else {
				mm_html += "<option value ='"+i+"'>"+i+"</option>";	
			}
		}
		
		$("select#birthmm").html(mm_html);
		
		
		let dd_html = "";	/* 생년월일 중 일만 쓰고자 한다. (31번 반복) */
		for(let i=1; i<=31; i++) {
			if(i<10) {
				dd_html += "<option value ='0"+i+"'>0"+i+"</option>";			
			}
			else {
				dd_html += "<option value ='"+i+"'>"+i+"</option>";	
			}
		}
		
		$("select#birthdd").html(dd_html);
		
	// === jQuery UI 의 datepicker === //
	// 생년월일 부분
	$("input#datepicker").datepicker({
	           dateFormat: 'yy-mm-dd'  //Input Display Format 변경
	          ,showOtherMonths: true   //빈 공간에 현재월의 앞뒤월의 날짜를 표시
	          ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
	          ,changeYear: true        //콤보박스에서 년 선택 가능
	          ,changeMonth: true       //콤보박스에서 월 선택 가능                
	          ,showOn: "both"          //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
	          ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
	          ,buttonImageOnly: true   //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
	          ,buttonText: "선택"       //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
	          ,yearSuffix: "년"         //달력의 년도 부분 뒤에 붙는 텍스트
	          ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
	          ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
	          ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
	          ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
	        //,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
	        //,maxDate: "+1M" //최대 선택일자(+1D:하루후, +1M:한달후, +1Y:일년후)                
	      });                    
	      
	  //초기값을 오늘 날짜로 설정
	  $('input#datepicker').datepicker('setDate', 'TODAY'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후) 
		
	///////////////////////////////////////////////////////////////////////////////
		
		
	// === 전체 datepicker 옵션 일괄 설정하기 ===  
	// 재직기간 부분
	// 한번의 설정으로 $("input#fromDate"), $('input#toDate')의 옵션을 모두 설정할 수 있다.
	  $(function() {
	      //모든 datepicker에 대한 공통 옵션 설정
	      $.datepicker.setDefaults({
	          dateFormat: 'yy-mm-dd' //Input Display Format 변경
	          ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
	          ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
	          ,changeYear: true //콤보박스에서 년 선택 가능
	          ,changeMonth: true //콤보박스에서 월 선택 가능                
	       // ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
	       // ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
	       // ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
	       // ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
	          ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
	          ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
	          ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
	          ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
	          ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
	       // ,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
	       // ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                    
	      });
	
	      //input을 datepicker로 선언
	      $("input#fromDate").datepicker();                    
	      $("input#toDate").datepicker();
	      
	      //From의 초기값을 오늘 날짜로 설정
	      $('input#fromDate').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
	      
	      //To의 초기값을 3일후로 설정
	      $('input#toDate').datepicker('setDate', '+3D'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
	  });		

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

		///// 아이디 중복검사 하기 /////
		$("img#idcheck").click(() => {	// img 를 클릭하면, 아래와 같은 

			b_flagIdDuplicateClick = true;
			// 가입하기 버튼 클릭 시, '아이디 중복확인'을 클릭했는지 알아보기 위한 용도
			// 클릭했다는 뜻은 false -> true 로 되게끔 한다. (클릭하면 바꾼다.)
			
			// 입력하고자 하는 아이디가 데이터베이스에 테이블에 존재하는지 존재하지 않는지 알아와야 한다.
			/*
			  Ajax (Asynchronous JavaScript and XML)란?
				==> 이름만 보면 알 수 있듯이 '비동기 방식의 자바스크립트와 XML' 로서
			    Asynchronous JavaScript + XML 인 것이다.
			    한마디로 말하면, Ajax 란? Client 와 Server 간에 XML 데이터를 JavaScript 를 사용하여 비동기 통신으로 주고 받는 기술이다.
			    하지만 요즘에는 데이터 전송을 위한 데이터 포맷방법으로 XML 을 사용하기 보다는 JSON(Javascript Object Notation 자바스크립트 객체 표기법) 을 더 많이 사용한다.
			    참고로 HTML은 데이터 표현을 위한 포맷방법이다.
			    그리고, 비동기식이란 어떤 하나의 웹페이지에서 여러가지 서로 다른 다양한 일처리가 개별적으로 발생한다는 뜻으로서, 
			    어떤 하나의 웹페이지에서 서버와 통신하는 그 일처리가 발생하는 동안 일처리가 마무리 되기전에 또 다른 작업을 할 수 있다는 의미이다.
			
			  JSON 은 {"key1":"data1","key2":"data2","key3":"data3"} 이러한 모양을 띄는 것을 말한다.
			  예를 들면 {"id":"leess","passwd":"qwer1234@","age":26,"pass":true} 이런 것이다.
			  
			 */
			
			// ==== jQuery 를 이용한 Ajax (Asynchronous JavaScript and XML) 처리하기 ==== //
			// 아래의 url("key:값") 로 요청을 보내겠다. data는 js의 객체 모양으로 보낸다.
			$.ajax({
				url:"<%= ctxPath%>/member/idDuplicateCheck.up",		
				data:{"userid":$("input#userid").val()},	// data 는 MyMVC/member/idDuplicateCheck.up 로 전송해야할 데이터를 말한다.
				type:"post", // type 생략하면 "get" 이다. get 방식인지 post 방식인지 알아야 한다. (method: 가 아니라 type: 으로 쓴다.)
			//	async:false, // 동기 처리 (지도는 동기처리로 해야한다.)
				async:true,	 // 비동기 처리(기본값임)			
				success:function(text){	// text 는 결과물이다.
				
				//	console.log("확인용 : text =>" + text);
				// 확인용 : text => {"isExist":false}
				//	console.log("확인용 타입 typeof(text) => " + typeof(text));
				// 	확인용 타입 typeof(text) => string
				
					const json = JSON.parse(text);
				// JSON.parse(text); 은 JSON 형식으로 되어진 문자열을 자바스크립트 객체로 변환해주는 것이다.
                // 조심할 것은 text 는 반드시 JSON 형식으로 되어진 문자열이어야 한다.	

				//	console.log("확인용 : json =>" + json);
				// 확인용 : json =>[object Object]
				//	console.log("확인용 타입 typeof(json) => " + typeof(json));
				// 확인용 타입 typeof(json) => object
				
				//	console.log("확인용 => " + json.isExist);
				// 	확인용 => false
				
					if(json.isExist == true) {
						// 입력한 $("input#userid").val() 값이 이미 사용중이다.
						$("span#idcheckResult").html($("input#userid").val() + " 은 중복된 ID 이므로 사용 불가합니다.").css("color","orange");	// 저 span 태그에 넣어주겠다.
						$("input#userid").val("");	// 이미 해당 아이디가 존재하면, 아이디 값을 "" 텅 비게 만들어준다.
					}
					else {
						// 입력한 $("input#userid").val() 값이 DB tbl_member 테이블에 존재하지 않는 경우 (즉 중복된 아이디가 없다.)
						$("span#idcheckResult").html($("input#userid").val() + " 은 사용 가능합니다.").css("color","green");
					}
				
				},
				error:function(request, status, error) {
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
					
			});

	});
	
		// 아이디 값이 변경되면 가입하기 버튼 클릭 시, '아이디 중복확인'을 클릭했는지 안했는지를 알아보기 위한 용도를 초기화 시킨다.
		$("input#userid").bind("change", ()=>{	// 아이디가 변경되면 ___(함수) 를 해라.
			let b_flagIdDuplicateClick = false;	// 다시 초기화 해라. 
		});	
		

		// 이메일 값이 변경되면 가입하기 버튼 클릭 시, '이메일 중복확인'을 클릭했는지 안했는지를 알아보기 위한 용도를 초기화 시킨다.
		$("input#email").bind("change", ()=>{		// 이메일이 변경되면 ___(함수) 를 해라.
			let b_flagEmailDuplicateClick = false;	// 다시 초기화 해라. 
		});			
		
		
	});// end of $(document).ready(function()-------------------

	//Function Declaration	
	
	// 이메일 중복여부 검사하기
	function isExistEmailCheck() {
		
		b_flagEmailDuplicateClick = true;
		// 가입하기 버튼 클릭 시, '이메일 중복확인'을 클릭했는지 알아보기 위한 용도
		
		// 입력하고자 하는 이메일이 데이터베이스에 테이블에 존재하는지 존재하지 않는지 알아와야 한다.
		/*
		Ajax (Asynchronous JavaScript and XML)란?
			==> 이름만 보면 알 수 있듯이 '비동기 방식의 자바스크립트와 XML' 로서
		    Asynchronous JavaScript + XML 인 것이다.
		    한마디로 말하면, Ajax 란? Client 와 Server 간에 XML 데이터를 JavaScript 를 사용하여 비동기 통신으로 주고 받는 기술이다.
		    하지만 요즘에는 데이터 전송을 위한 데이터 포맷방법으로 XML 을 사용하기 보다는 JSON 을 더 많이 사용한다.
		    참고로 HTML은 데이터 표현을 위한 포맷방법이다.
		    그리고, 비동기식이란 어떤 하나의 웹페이지에서 여러가지 서로 다른 다양한 일처리가 개별적으로 발생한다는 뜻으로서, 
		    어떤 하나의 웹페이지에서 서버와 통신하는 그 일처리가 발생하는 동안 일처리가 마무리 되기전에 또 다른 작업을 할 수 있다는 의미이다.
		*/
		
		// ==== jQuery 를 이용한 Ajax (Asynchronous JavaScript and XML) 처리하기 ==== //
		// 아래의 url("key:값") 로 요청을 보내겠다. data는 js의 객체 모양으로 보낸다.
		$.ajax({
			url:"<%= ctxPath%>/member/emailDuplicateCheck.up",		
			data:{"email":$("input#email").val()},	// data 는 MyMVC/member/idDuplicateCheck.up 로 전송해야할 데이터를 말한다.
			type:"post", // type 생략하면 "get" 이다. get 방식인지 post 방식인지 알아야 한다. (method: 가 아니라 type: 으로 쓴다.)
			// const json = JSON.parse(text); 또는!
			dataType:"json",
		//	async:false, // 동기 처리 (지도는 동기처리로 해야한다.)
			async:true,	 // 비동기 처리(기본값임)
			success:function(json){	// text 는 결과물이다.
			
			//	console.log("확인용 json => " + json);
			// 	확인용 json => [object Object]
			//	console.log("확인용 typeof(json) => " + typeof(json));
			//	확인용 typeof(json) => object	
						
				if(json.isExist == true) {
					// 입력한 $("input#email").val() 값이 이미 사용중이라면
					$("span#emailCheckResult").html($("input#email").val() + " 은 중복된 email 이므로 사용 불가합니다.").css("color","red");	// 저 span 태그에 넣어주겠다.
					$("input#email").val("");	// 이메일 값이 존재하면 해당 이메일 입력 칸을 "" 으로 비운다.
				}
				else {
					// 입력한 $("input#email").val() 값이 DB tbl_member 테이블에 존재하지 않는 경우 (즉 중복된 아이디가 없다.)
					$("span#emailCheckResult").html($("input#email").val() + " 은 사용 가능합니다.").css("color","blue");
				}
			
			},
			error:function(request, status, error) {
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
				
		});		
		
	}// function isExistEmailCheck()-------------------------------------
	
	// 가입하기
	function goRegister() {
		
		// ① 필수입력사항에 모두 입력이 되었는지 검사한다.
		let b_FlagRequiredInfo = false;
		
		$("input.requiredInfo").each(function(index, item) {
			const data = $(item).val().trim();	// 각각의 항목에 값(val)을 다 채워 넣었는지 확인
			if(data == "") {	// data 가 "", 텅 비었다! (아무것도 적지 않았다.)
				alert("* 표시된 필수 입력사항은 모두 입력하셔야 합니다.")
				b_FlagRequiredInfo = true;	// break 하기 전에 깃발 올린다!
				return false; // for 문에서 break 와 같은 기능이다. 즉, 필수입력사항 검사하고 성별을 또 검사할 필요가 없이 여기서 끝내야 한다.				
			}
			
		});
		// 위에서 빠져나온 후에 종료하고, 성별 선택하는지 검사하는 곳에 넘어가게 하지 않기 위해 여기서 return 을 씀으로써 끝낸다.
		if(b_FlagRequiredInfo == true) {
			return;	// 이 return 은 함수의 종료를 뜻한다.
		}
		
		// ② 성별이 선택 되었는지 검사한다.
		// radio 는 항상 name 이 똑같아야 한다.
		const genderCheckedLength = $("input:radio[name='gender']:checked").length;
		
		if(genderCheckedLength == 0){
			// 성별을 체크하지 않았을 때 (길이가 0이면 입력하지 않은것임)
			alert("성별을 선택 해주세요.");
			return; // 함수가 밑으로 내려가지 않게 여기서 끝낸다. (종료)
		}
		
		// ③ 이용약관에 동의했는지 검사한다.
		const agreeCheckedLength = $("input:checkbox[id='agree']:checked").length;
		
		if(agreeCheckedLength == 0){
			// 이용약관에 체크하지 않았을 때
			alert("이용약관에 동의 시에만 회원가입이 가능합니다.");
			return; // 함수가 밑으로 내려가지 않게 여기서 끝낸다. (종료)
		}
		
		// ④ 아이디 중복확인을 클릭했는지 검사한다.
		if(!b_flagIdDuplicateClick) { 	// b_flagIdDuplicateClick 가 false 라면,
			// 가입하기 버튼 클릭 시, '아이디 중복확인'을 클릭했는지 알아보기 위한 용도
			alert("아이디 중복확인을 클릭하여 id 중복검사를 하세요!");
			return;
		
		}

		// ⑤ 이메일 중복확인을 클릭했는지 검사한다.
		if(!b_flagEmailDuplicateClick) { 	// b_flagIdDuplicateClick 가 false 라면,
			// 가입하기 버튼 클릭 시, '이메일 중복확인'을 클릭했는지 알아보기 위한 용도
			alert("이메일 중복확인을 클릭하여 email 중복검사를 하세요!");
			return;		
		}		
		
	
	const frm = document.registerFrm;	// form 태그
	frm.action = "memberRegister.up";	// 상대경로
	frm.method = "post";
	frm.submit();	// 유효성 검사를 다 충족했을때 submit 한다! (기본은 get 방식이다.)

	}// end of function goRegister()------------------------------------------------
	
	// 다 맞게 입력 했으면 이제 submit 한다.
	
</script>

<div class="row" id="divRegisterFrm">
   <div class="col-md-12" align="center">
   <form name="registerFrm">

	   <table id="tblMemberRegister">
	      <thead>
	      <tr>
	          
	           <th colspan="2" id="th">회원가입 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>) :::</th>
	      </tr>
	      </thead>
	      <tbody>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">성명&nbsp;<span class="star">*</span></td>
	         <td style="width: 80%; text-align: left;">
	             <input type="text" name="name" id="name" class="requiredInfo" /> 
	            <span class="error">성명은 필수입력 사항입니다.</span>
	         </td>
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">아이디&nbsp;<span class="star">*</span></td>
	         <td style="width: 80%; text-align: left;">
	             <input type="text" name="userid" id="userid" class="requiredInfo" />&nbsp;&nbsp;
	             <!-- 아이디중복체크 -->
	             <img id="idcheck" src="../images/b_id_check.gif" style="vertical-align: middle;" />
	             <span id="idcheckResult"></span>
	             <span class="error">아이디는 필수입력 사항입니다.</span>
	         </td> 
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">비밀번호&nbsp;<span class="star">*</span></td>
	         <td style="width: 80%; text-align: left;"><input type="password" name="pwd" id="pwd" class="requiredInfo" />
	            <span class="error">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로 입력하세요.</span>
	         </td>
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">비밀번호확인&nbsp;<span class="star">*</span></td>
	         <td style="width: 80%; text-align: left;"><input type="password" id="pwdcheck" class="requiredInfo" /> 
	            <span class="error">암호가 일치하지 않습니다.</span>
	         </td>
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">이메일&nbsp;<span class="star">*</span></td>
	         <td style="width: 80%; text-align: left;"><input type="text" name="email" id="email" class="requiredInfo" placeholder="abc@def.com" /> 
	             <span class="error">이메일 형식에 맞지 않습니다.</span>
	             
	             <%-- ==== 퀴즈 시작 ==== --%>
	             <span style="display: inline-block; width: 80px; height: 30px; border: solid 1px gray; border-radius: 5px; font-size: 8pt; text-align: center; margin-left: 10px; cursor: pointer;" onclick="isExistEmailCheck();">이메일중복확인</span> 
	             <span id="emailCheckResult"></span>
	             <%-- ==== 퀴즈 끝 ==== --%>
	         </td>
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">연락처</td>
	         <td style="width: 80%; text-align: left;">
	             <input type="text" id="hp1" name="hp1" size="6" maxlength="3" value="010" readonly />&nbsp;-&nbsp;
	             <input type="text" id="hp2" name="hp2" size="6" maxlength="4" />&nbsp;-&nbsp;
	             <input type="text" id="hp3" name="hp3" size="6" maxlength="4" />
	             <span class="error">휴대폰 형식이 아닙니다.</span>
	         </td>
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">우편번호</td>
	         <td style="width: 80%; text-align: left;">
	            <input type="text" id="postcode" name="postcode" size="6" maxlength="5" />&nbsp;&nbsp;
	            <%-- 우편번호 찾기 --%>
	            <img id="zipcodeSearch" src="../images/b_zipcode.gif" style="vertical-align: middle;" />
	            <span class="error">우편번호 형식이 아닙니다.</span>
	         </td>
	      </tr>
	      <tr>
	         <td style="width: 20%; font-weight: bold;">주소</td>
	         <td style="width: 80%; text-align: left;">
	            <input type="text" id="address" name="address" size="40" placeholder="주소" /><br/>
	            <input type="text" id="detailAddress" name="detailAddress" size="40" placeholder="상세주소" />&nbsp;<input type="text" id="extraAddress" name="extraAddress" size="40" placeholder="참고항목" /> 
	            <span class="error">주소를 입력하세요</span>
	         </td>
	      </tr>
	      
	      <tr>
	         <td style="width: 20%; font-weight: bold;">성별</td>
	         <td style="width: 80%; text-align: left;">
	            <input type="radio" id="male" name="gender" value="1" /><label for="male" style="margin-left: 2%;">남자</label>
	            <input type="radio" id="female" name="gender" value="2" style="margin-left: 10%;" /><label for="female" style="margin-left: 2%;">여자</label>
	         </td>
	      </tr>
	      
	      <tr>
	         <td style="width: 20%; font-weight: bold;">생년월일</td>
	         <td style="width: 80%; text-align: left;">
	            <input type="number" id="birthyyyy" name="birthyyyy" min="1950" max="2050" step="1" value="1995" style="width: 80px;" required />
	            
	            <select id="birthmm" name="birthmm" style="margin-left: 2%; width: 60px; padding: 8px;">
	               <%-- 
	               <option value ="01">01</option>
	               <option value ="02">02</option>
	               <option value ="03">03</option>
	               <option value ="04">04</option>
	               <option value ="05">05</option>
	               <option value ="06">06</option>
	               <option value ="07">07</option>
	               <option value ="08">08</option>
	               <option value ="09">09</option>
	               <option value ="10">10</option>
	               <option value ="11">11</option>
	               <option value ="12">12</option>
	               --%>
	            </select> 
	            
	            <select id="birthdd" name="birthdd" style="margin-left: 2%; width: 60px; padding: 8px;">
	               <%-- 
	               <option value ="01">01</option>
	               <option value ="02">02</option>
	               <option value ="03">03</option>
	               <option value ="04">04</option>
	               <option value ="05">05</option>
	               <option value ="06">06</option>
	               <option value ="07">07</option>
	               <option value ="08">08</option>
	               <option value ="09">09</option>
	               <option value ="10">10</option>
	               <option value ="11">11</option>
	               <option value ="12">12</option>
	               <option value ="13">13</option>
	               <option value ="14">14</option>
	               <option value ="15">15</option>
	               <option value ="16">16</option>
	               <option value ="17">17</option>
	               <option value ="18">18</option>
	               <option value ="19">19</option>
	               <option value ="20">20</option>
	               <option value ="21">21</option>
	               <option value ="22">22</option>
	               <option value ="23">23</option>
	               <option value ="24">24</option>
	               <option value ="25">25</option>
	               <option value ="26">26</option>
	               <option value ="27">27</option>
	               <option value ="28">28</option>
	               <option value ="29">29</option>
	               <option value ="30">30</option>
	               <option value ="31">31</option>
	               --%>
	            </select> 
	         </td>
	      </tr>
	      
	      <tr>
	         <td style="width: 20%; font-weight: bold;">생년월일</td>
	         <td style="width: 80%; text-align: left;">
	            <input type="text" id="datepicker">
	         </td>
	      </tr>
	      
	      <tr>
	         <td style="width: 20%; font-weight: bold;">재직기간</td>
	         <td style="width: 80%; text-align: left;">
	            From: <input type="text" id="fromDate">&nbsp;&nbsp; 
	            To: <input type="text" id="toDate">
	         </td>
	      </tr>
	         
	      <tr>
	         <td colspan="2">
	            <label for="agree">이용약관에 동의합니다</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
	         </td>
	      </tr>
	      <tr>
	         <td colspan="2" style="text-align: center; vertical-align: middle;">
	            <iframe src="../iframeAgree/agree.html" width="85%" height="150px" class="box" ></iframe>
	         </td>
	      </tr>
	      <tr>
	         <td colspan="2" style="line-height: 90px;" class="text-center">
	            <%-- 
	            <button type="button" id="btnRegister" style="background-image:url('/MyMVC/images/join.png'); border:none; width: 135px; height: 34px; margin-left: 30%;" onClick="goRegister();"></button> 
	            --%>
	            <button type="button" id="btnRegister" class="btn btn-dark btn-lg" onClick="goRegister()">가입하기</button> 
	         </td>
	      </tr>
	      </tbody>
	   </table>
   </form>
   </div>
</div>
<jsp:include page="../footer.jsp" />