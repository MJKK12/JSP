<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 어차피 header.jsp 에 html 이 들어있기 때문에 여기선 쓰지 않아도 된다. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<style type="text/css">
	table#loginTbl {
	      width: 95%;
	      border: solid 1px gray;
	      border-collapse: collapse;
	      margin-top: 20px;
	   }
	   
	   table#loginTbl #th {
	      background-color: silver;
	      font-size: 14pt;
	      text-align: center;
	      height: 30px;
	   }
	   
	   table#loginTbl td {
	      border: solid 1px gray;
	      line-height: 30px;
	   }
</style>

<script type="text/javascript">

	$(document).ready(function() {
		
		$("button#btnSubmit").click(function() {
			// 로그인 할 때, 로그인 버튼을 클릭 or 비번을 입력하고 엔터를 쳐야 한다. → goLogin()이라는 공통 함수를 만든다.
			goLogin();	// 로그인을 시도한다.
		});

		$("input#loginPwd").bind("keydown", function(event) {		
			// 로그인 할 때, 로그인 버튼을 클릭 or 비번을 입력하고 엔터를 쳐야 한다.
			if(event.keyCode == 13) {	// 암호 입력란에서 엔터를 입력(keydown) 했을 경우!
				goLogin();
			}
		});
		
		//////////////////////////////////////////////////////////
		
		// 문서가 로딩되면, 로컬스토리지에 해당 값이 있는지 조회하고, 있을 경우 아이디 항목에 계속 그 값이 저장되도록 할 것.
		// === 로컬스토리지(localStorage)에 저장된 key 가 "saveid" 인 userid 값을 불러와서 input 태그 userid에 넣어주기. === //
		const loginUserid = localStorage.getItem('saveid');
		
		if(loginUserid != null) {
			$("input#loginUserid").val(loginUserid);
			$("input#checkbox[id='saveid']").prop("checked",true);
		}
		
		$("button.myclose").click(function () {
			// 모달창에 입력한 값을 초기화 하기 위함
			javascript:history.go(0);	// 새로고침
			// 현재 페이지를 새로고침을 함으로써 모달창에 입력한 성명과 이메일의 값이 텍스트박스에 남겨있지 않고 삭제하는 효과를 누린다.			
			
		  /* === 새로고침(다시읽기) 방법 3가지 차이점 ===
	        >>> 1. 일반적인 다시읽기 <<<
	        window.location.reload();
	        ==> 이렇게 하면 컴퓨터의 캐시에서 우선 파일을 찾아본다.
	                 없으면 서버에서 받아온다. 
	        
	        >>> 2. 강력하고 강제적인 다시읽기 <<<
	        window.location.reload(true);
	        ==> true 라는 파라미터를 입력하면, 무조건 서버에서 직접 파일을 가져오게 된다.
	                 캐시는 완전히 무시된다.
	        
	        >>> 3. 부드럽고 소극적인 다시읽기 <<<
	        history.go(0);
	        ==> 이렇게 하면 캐시에서 현재 페이지의 파일들을 항상 우선적으로 찾는다.
	     */			
		})
		
	});// end of $(document).ready(function()---------------------

	
	//Function Declaration (로그인 처리 함수 만들기)
	// == 로그인 처리 함수 == //
	function goLogin() {
		// 아이디 입력에 대한 유효성 검사가 필요하다.
		const loginUserid = $("input#loginUserid").val().trim();	// 입력한 id 값에 대해 공백 제거.
		const loginPwd = $("input#loginPwd").val().trim();	// 입력한 pwd 값에 대해 공백 제거.
		
		if(loginUserid == "") {
			alert("아이디를 입력하세요!");
			$("input#loginUserid").val("");		
			$("input#loginUserid").focus();
			return; // goLogin() 함수 종료
		}	
				
		if(loginPwd == "") {
			alert("암호를 입력하세요!");
			$("input#loginPwd").val("");		
			$("input#loginPwd").focus();		
			return; // goLogin() 함수 종료
		}
		
		/*
	      >> 로컬 스토리지(localStorage)와 세션 스토리지(sessionStorage) << 
	            로컬 스토리지와 세션 스토리지는 HTML5에서 추가된 저장소이다.
	            간단한 키와 값을 저장할 수 있다. 키-밸류 스토리지의 형태이다.
	          
	          ※ 로컬 스토리지와 세션 스토리지의 차이점은 데이터의 영구성이다. 
	             로컬 스토리지의 데이터는 사용자가 지우지 않는 이상 계속 브라우저에 남아 있게 된다. 
	             만료 기간을 설정할 수 없다.
	             하지만 세션 스토리지의 데이터는 윈도우나 브라우저 탭을 닫을 경우 자동적으로 제거된다.
	             지속적으로 필요한 데이터(자동 로그인 등)는 로컬 스토리지에 저장하고, 
	             잠깐 동안 필요한 정보(일회성 로그인 정보라든가)는 세션 스토리지에 저장하도록 한다. 
	             그러나 비밀번호같은 중요한 정보는 절대로 저장하면 안된다.
	             왜냐하면  클라이언트 컴퓨터 브라우저에 저장하는 것이기 때문에 타인에 의해 도용당할 수 있기 때문이다.
	      
	             로컬 스토리지랑 세션 스토리지가 나오기 이전에도 브라우저에 저장소 역할을 하는 게 있었다.
	             바로 쿠키인데 쿠키는 만료 기한이 있는 키-값 저장소이다.
	      
	             쿠키는 4kb 용량 제한이 있고, 매번 서버 요청마다 서버로 쿠키가 같이 전송된다.
	             만약 4kb 용량 제한을 거의 다 채운 쿠키가 있다면, 요청을 할 때마다 기본 4kb의 데이터를 사용한다. 
	          	 4kb 중에는 서버에 필요하지 않은 데이터들도 있을 수 있다. 
	             그러므로 데이터 낭비가 발생할 수 있게 된다. 
	             바로 그런 데이터들을 이제 로컬 스토리지와 세션 스토리지에 저장할 수 있다. 
	             이 두 저장소의 데이터는 서버로 자동 전송되지 않는다.
	      
	         >> 로컬 스토리지(localStorage) <<
	            로컬 스토리지는 window.localStorage에 위치한다. 
	            키 밸류 저장소이기 때문에 키와 밸류를 순서대로 저장하면 된다. 
	            값으로는 문자열, boolean, 숫자, null, undefined 등을 저장할 수 있지만, 
	            모두 문자열로 변환된다. 키도 문자열로 변환된다.
	      
	            localStorage.setItem('name', '이순신');
	            localStorage.setItem('birth', 1994);
	      
	            localStorage.getItem('name');        // 이순신
	            localStorage.getItem('birth');       // 1994 (문자열)
	      
	            localStorage.removeItem('birth');   // birth 삭제
	            localStorage.getItem('birth');      // null (삭제됨)
	      
	            localStorage.clear();               // 전체 삭제
	      
	            localStorage.setItem(키, 값)으로 로컬스토리지에 저장함.
	            localStorage.getItem(키)로 조회함. 
	            localStorage.removeItem(키)하면 해당 키가 지워지고, 
	            localStorage.clear()하면 스토리지 전체가 비워진다.
	      
	            localStorage.setItem('object', { userid : 'leess', name : '이순신' });
	            localStorage.getItem('object');   // [object Object]
                객체는 제대로 저장되지 않고 toString 메소드가 호출된 형태로 저장된다. 
	            [object 생성자]형으로 저장되는 것이다. 
                객체를 저장하려면 두 가지 방법이 있다. 
                그냥 키-값 형식으로 풀어서 여러 개를 저장할 수도 있다. 
                한 번에 한 객체를 통째로 저장하려면 JSON.stringify를 해야된다. 
                객체 형식 그대로 문자열로 변환하는 것이다. 받을 때는 JSON.parse하면 된다.
	      
	            localStorage.setItem('object', JSON.stringify({ userid : 'leess', name : '이순신' }));
	            JSON.parse(localStorage.getItem('object')); // { userid : 'leess', name : '이순신' }
	           
                이와같이 데이터를 지우기 전까지는 계속 저장되어 있기 때문에 
                사용자의 설정(보안에 민감하지 않은)이나 데이터들을 넣어두면 된다.  
	      
	         >> 세션 스토리지(sessionStorage) <<
	            세션 스토리지는 window.sessionStorage에 위치한다. 
	            clear, getItem, setItem, removeItem, key 등 
                모든 메소드가 로컬 스토리지(localStorage)와 같다. 
                단지 로컬스토리지와는 다르게 데이터가 영구적으로 보관되지는 않을 뿐이다. 
	                  
	         >> 로컬 스토리지(localStorage)와 세션 스토리지(sessionStorage) 에 저장된 데이터를 보는 방법 << 
                크롬인 경우 F12(개발자도구) Application 탭에 가면 Storage - LocalStorage 와 SessionStorage 가 보여진다.
                거기에 들어가서 보면 Key 와 Value 값이 보여진다.
	   */
	         
	    if($("input:checkbox[id='saveid']").prop("checked")) {
	    //	alert("아이디 저장 체크 완료!");	    
	    	localStorage.setItem('saveid', $("input#loginUserid").val());
	    }
	    else {
	    //	alert("아이디 저장 체크 해제!");
	    	localStorage.removeItem('saveid');
		}
				
		const frm = document.loginFrm;
		frm.action = "<%= request.getContextPath() %>/login/login.up";
		frm.method = "post";	// post 방식으로 보낸다. (주소창에 입력한 값이 다 보이면 안된다.)
		frm.submit();
		
	}// end of goLogin()-----------------------------------------


	
	//Function Declaration (로그아웃 처리 함수 만들기)
	// == 로그아웃 처리 함수 == //	request.getContextPath() 는 절대경로인 /MyMVC
	function goLogOut() {
		
		// 로그아웃을 처리해주는 페이지로 이동
		location.href="<%= request.getContextPath()%>/login/logout.up";	
		
		
	}// end of function goLogOut()--------------------------
	
	
	// 코인충전 결제금액 선택하기 (실제로 카드 결제, 파라미터에 누군지 userid가 들어온다.)
	function goCoinPurchaseTypeChoice(userid) {
		
		// 코인구매금액 선택 팝업창 띄우기	(DB 업데이트를 위해서 어떤 userid 인지를 파악해야한다.)
		const url = "<%= request.getContextPath()%>/member/coinPurchaseTypeChoice.up?userid="+userid;
		
		window.open(url,"coinPurchaseTypeChoice",
					"left=350px, top=100px, width=650px, height=570px");

	}// end of goCoinPurchaseTypeChoice(userid)-------------------------------
	
	// === 아임포트 결제를 해주는 함수 === //
	function goCoinPurchaseEnd(coinmoney) {	// 결제금액(coinmoney)이 얼마인지 알아야 한다.		
	//	alert("확인용 부모창의 함수 호출함. 결제금액 : "+coinmoney+" 원");
	
	
	// userid 값을 알아오기 위함이다.
		const userid = "${sessionScope.loginuser.userid}";	// session 영역의 loginuser 의 userid
		
	//	alert("확인용 결제할 사용자 userid : " + userid)		// 결제할 사용자 id
	
	// 아임포트 결제 팝업창 띄우기
		const url = "<%= request.getContextPath()%>/member/coinPurchaseEnd.up?userid="+userid+"&coinmoney="+coinmoney;
		
		window.open(url,"coinPurchaseEnd",
					"left=350px, top=100px, width=1000px, height=600px");
	
	
	}// end of function goCoinPurchaseEnd()-----------------------
	
	// == DB 상의 tbl_member 테이블의 해당 사용자의 coin 금액을 증가(update) 시켜주는 함수 == //
	function goCoinUpdate(userid, coinmoney) {	// userid 는 무엇이며, coinmoney 는 얼마인지? --> DB 에서 update 한다.
		
	//		alert("확인용 사용자 userid : " + userid + ", 코인액 : " + coinmoney )		// 결제할 사용자 id

			// form 태그에 실어서 post 방식으로 보낸다.
			const frm = document.coinUpdateFrm;
			frm.userid.value = userid;	// userid 값을 넣어주자.
			frm.coinmoney.value = coinmoney;	// coinmoney 값을 넣어주자.
			// 코인 머니를 넣어준다. (Update)
			frm.action = "<%= request.getContextPath()%>/member/coinUpdateLoginUser.up";
			frm.method = "post";
			frm.submit();
			
	}
	
	// 나의 정보 수정(Update)하는 함수 만들기//	
	function goEditPersonal(userid) {

		// 나의정보 수정하기 팝업창 띄우기
		const url = "<%= request.getContextPath()%>/member/memberEdit.up?userid="+userid;
		
		// 팝업창을 가운데 띄우고자 한다.
		// 너비 800, 높이 600 인 팝업창을 화면 가운데 위치시키기 / Math.ceil() 함수는 주어진 숫자보다 크거나 같은 숫자 중 가장 작은 숫자를 integer 로 반환
		const pop_width = 800;	// 기본이 px 이므로 쓰지 않는다.
		const pop_height = 600;
		const pop_left = Math.ceil((window.screen.width - pop_width)/2);	<%-- 정수로 만든다. --%>// Math.ceil: 보다 큰  (노트북, 데스크탑 모니터의 크기 - pop_width)/2
		const pop_top = Math.ceil((window.screen.width - pop_height)/2);		<%-- 정수로 만든다. --%>;
		
		window.open(url,"memberEdit",
					"left="+pop_left+", top="+pop_top+", width="+pop_width+", height="+pop_height);		
		
	}
	
	
</script>


<%-- 로그인을 하기 위한 form 생성. --%>	<%-- loginAction 에 httpSession 넣어둠. --%>
<c:if test="${empty sessionScope.loginuser}">		<%-- loginuser 라는 key 값이 있는지?, 없으면 form 태그 계속 띄운다. --%>
	<form name="loginFrm">
		<table id=loginTbl>
			<thead>
				<tr>
					<th colspan="2" id="th">LOGIN</th>
				</tr>				
			</thead>
			
			<tbody>
				<tr>
	               <td style="width: 20%; border-bottom: hidden; border-right: hidden; padding: 10px;">ID</td>
	               <td style="width: 80%; border-bottom: hidden; border-left: hidden; padding: 10px;"><input type="text" id="loginUserid" name="userid" size="20" class="box" autocomplete="off" /></td>
	            </tr>   
	            <tr>
	               <td style="width: 20%; border-top: hidden; border-bottom: hidden; border-right: hidden; padding: 10px;">암호</td>
	               <td style="width: 80%; border-top: hidden; border-bottom: hidden; border-left: hidden; padding: 10px;"><input type="password" id="loginPwd" name="pwd" size="20" class="box" /></td>
	            </tr>
	            
	            <%-- === 아이디 찾기, 비밀번호 찾기 === --%>
	            <tr>
	               <td colspan="2" align="center">
	                  <a style="cursor: pointer;" data-toggle="modal" data-target="#userIdfind" data-dismiss="modal">아이디찾기</a> /
	                  <a style="cursor: pointer;" data-toggle="modal" data-target="#passwdFind" data-dismiss="modal" data-backdrop="static">비밀번호찾기</a>
	               </td>
	            </tr>
	            
	            <tr>
	               <td colspan="2" align="center" style="padding: 10px;">
	                  <input type="checkbox" id="saveid" name="saveid" /><label for="saveid">아이디저장</label>
	               <%--    
	                  <button type="button" id="btnSubmit" style="width: 67px; height: 27px; background-image: url('<%= request.getContextPath()%>/images/login.png'); vertical-align: middle; border: none;"></button>
	               --%>
	                   <button type="button" id="btnSubmit" class="btn btn-primary btn-sm ml-5">로그인</button>
	               </td>
	            </tr>		
			</tbody>		
		</table>
	</form>
</c:if>


<%-- 로그인 된 화면 --%>
<c:if test="${not empty sessionScope.loginuser}">	<%-- session 영역에 loginuser 라는 키값이 있다. --%>
	<table style="width: 95%; height: 130px;">
          <tr style="background-color: #f2f2f2;">
             <td align="center" style="padding: 20px;">
                <span style="color: blue; font-weight: bold;">${(sessionScope.loginuser).name}</span>
                [<span style="color: red; font-weight: bold;">${(sessionScope.loginuser).userid}</span>]님
                <br/><br/>
                <div align="left" style="padding-left: 20px; line-height: 150%;">
                   <span style="font-weight: bold;">코인액&nbsp;:</span>&nbsp;&nbsp; <fmt:formatNumber value="${(sessionScope.loginuser).coin}" pattern="###,###" /> 원
                   <br/>
                   <span style="font-weight: bold;">포인트&nbsp;:</span>&nbsp;&nbsp; <fmt:formatNumber value="${(sessionScope.loginuser).point}" pattern="###,###" /> POINT
                </div>
                <br/>로그인 중...<br/><br/>
                [<a href="javascript:goEditPersonal('${(sessionScope.loginuser).userid}')">나의정보</a>]&nbsp;&nbsp;
                 [<a href="javascript:goCoinPurchaseTypeChoice('${(sessionScope.loginuser).userid}')">코인충전</a>] 
                 <br/><br/>
                <button type="button" class="btn btn-danger" onclick="goLogOut();">로그아웃</button>
             </td>
          </tr>
    </table>
</c:if>




<%-- **** 아이디 찾기 Modal **** --%>
<!-- Modal -->
<!-- Modal 구성 요소는 현재 페이지 상단에 표시되는 대화 상자/팝업 창입니다. -->
<div class="modal fade" id="userIdfind" data-backdrop="static">
  <div class="modal-dialog">
    <div class="modal-content">
      
      <!-- Modal header -->
      <div class="modal-header">
        <h5 class="modal-title">아이디 찾기</h5>
        <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
      </div>
      
      <!-- Modal body -->
      <div class="modal-body">
      	<div id="idFind">
      		<iframe style="border: none; width: 100%" src="<%= request.getContextPath() %>/login/idFind.up">
      		</iframe>
		</div>
      </div>
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger myclose" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>



<%-- **** 비밀번호 찾기 Modal **** --%>
<!-- Modal -->
<!-- Modal 구성 요소는 현재 페이지 상단에 표시되는 대화 상자/팝업 창입니다. -->
<div class="modal fade" id="passwdFind">
  <div class="modal-dialog">
    <div class="modal-content">
      
      <!-- Modal header -->
      <div class="modal-header">
        <h5 class="modal-title">비밀번호 찾기</h5>
        <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
      </div>
      
      <!-- Modal body -->
      <div class="modal-body">
      	<div id="pwFind">
      		<iframe style="border: none; width: 100%" src="<%= request.getContextPath() %>/login/pwdFind.up">
      		</iframe>
		</div>
      </div>
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger myclose" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<%-- PG(Payment Gateway 결제)에 코인금액을 카드(카카오페이등)로 결제후 DB상에 사용자의 코인액을 update 를 해주는 폼이다. --%>
<form name="coinUpdateFrm">
   <input type="hidden" name="userid" />
   <input type="hidden" name="coinmoney" />
</form>



