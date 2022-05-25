package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;
import my.util.MyUtil;

public class MemberListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)

		// 보여줄 회원정보DB 를 DAO 에서 읽어온다.
		// 그러나 관리자가 아닌 사람이 주소창에 url 을 입력했을 때, 관리자전용 메뉴에 들어갈 수 있도록 하면 안된다.
		// 즉, 메뉴바만 막았을 뿐이고, url을 알아내서 주소창에서 관리자 외의 사람이 입력했을 경우를 방지해야 한다.
		// 메뉴바 & 주소창 직접입력(관리자 외)도 막아야 한다.
		
		// == 관리자(admin)로 로그인 했을 때에만 조회가 가능하도록 해야한다. == //
		// 이중으로 막아주도록 하자 (메뉴바 & 주소창 url 접속)
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");	// 세션에가서 key 값이 있는지 없는지 읽어온다. 리턴타입은 object 이기 때문에 MemberVO 로 바꾼다.
			
		if(loginuser == null || !"admin".equals(loginuser.getUserid())) {	// 로그인을 하지 않았거나 or 로그인된 유저의 아이디가 admin 이 아님
		//	로그인을 하지 않았거나 || 로그인을 했지만, 로그인한 유저의 id 가 admin 인 아닌 경우.	(올바르지 않은 접속)		
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
		
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");	// url 입력 시 맨앞에 '/' 입력 잊지말기
		}
		else {
			// 관리자(admin)로 로그인 했을 때 (올바른 접속)
			// == 페이징 처리가 된 모든 회원 또는 검색한 회원 목록 보여주기 == //
					
			// 검색이 있을 경우 시작 // // 검색 결과가 없을 때는 null 값이다.
			String searchType = request.getParameter("searchType");	// 검색대상의 컬럼명
			String searchword = request.getParameter("searchword");	// 
			// 검색이 있을 경우 끝 //
			// 검색 했으면 DB 에 보내주자.			
			
			// 관리자로 올바르게 접속했으면 DB 로 가자.
			InterMemberDAO mdao = new MemberDAO();

			Map<String, String> paraMap = new HashMap<>();
			
			// 현재 페이지 no 는 무엇이고 한 페이지당 몇개행을 볼 것인지를 불러오자. (jsp 에서 name 으로 지정한것을 getParameter 로 불러온다.)
			String currentShowPageNo = request.getParameter("currentShowPageNo");	// jsp 에서 name 을 currentShowPageNo 로 하겠다.
			// currentShowPageNo 는 사용자가 보고자 하는 페이지바의 페이지번호 이다.
			// 메뉴에서 회원목록 만을 클릭했을 경우에는 currentShowPageNo 은 null 이 된다.	(클릭하자마자 페이지바가 1을 가리킨다.)
			// currentShowPageNo 이 null 이라면, currentShowPageNo 를 1페이지로 바꿔야 한다.	--> 클릭하자마자 페이지 값이 1이기 때문이다.
			
			String sizePerPage = request.getParameter("sizePerPage"); 
			// sizePerPage 는 한 페이지당 보여줄 회원의 개수
			// 메뉴에서 회원목록 만을 클릭했을 경우에는 sizePerPage 는 null 이 된다.	(클릭하자마자 페이지바가 1을 가리킨다.)
			// sizePerPage 가 null 이라면 sizePerPage 를 10으로 바꿔야 한다. (10개씩 보여준다.)
			// "10" 또는 "5" 또는 "3"
			
			if(currentShowPageNo == null ) {	// 페이지바에서 선택하지 않았을 때, 저절로 1 페이지에 가있도록.
				// 회원목록만을 클릭했을 때, 페이지바에서 1을 누른 것이기 아니기 떄문에 저절로 1페이지가 되도록 만들어준다.
				currentShowPageNo = "1";	
			}
			
			if(sizePerPage == null ||
				// 회원목록만을 클릭했을 때, 맨 처음 1 페이지당 보여질 개수는 10개로 기본값을 해줘야 한다. // 3개 / 5개 / 10개이외의 것을 선택하면, 기본 페이지는 10개가 보이도록 한다.
				!("3".equals(sizePerPage) || "5".equals(sizePerPage) || "10".equals(sizePerPage))) {					
					sizePerPage = "10";
			}			
			
			// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 숫자가 아닌 문자를 입력한 경우 또는 (currentShowPageNo=강아지 / 이런식으로 적으면 안된다.)
			//     int 범위를 초과한 숫자를 입력한 경우라면 currentShowPageNo 는 1 페이지로 만들도록 한다. ==== //
			// currentShowPageNo 가 숫자인지 아닌지를 검증한다. (currentShowPageNo=숫자 / 이런식으로 나와야한다.)				
			try {
				Integer.parseInt(currentShowPageNo);	// currentShowPageNo 가 Int 의 범위로 들어오도록 한다. (정수 타입)				
			} catch (NumberFormatException e) {			// 사용자가 주소창에 숫자를 입력하지 않고 문자를 입력하는 장난을 쳤을 경우에도 1페이지가 기본값으로 뜨도록 한다.
				currentShowPageNo = "1";				// 즉, int 가 아니라면 페이지바의 1페이지번호로 기본 설정 해놓는다. 하지만, totalPage 이상의 수를 주소창에서 입력했을 경우도 막아줘야 한다.
			}
			
			
		 	// map 으로 DB에 정보를 보낸다.	map 속에 무언가를 담자. (--> 즉, 한 페이지당 몇개의 행을 보일 것인지?)
		 	// where 절에 1 페이지당 몇 행이 들어갈지를 보내줘야 한다.
		 	// 만약 유저가 11 페이지를 클릭했다면,			
			
			
			// map 에 넣어서 DB 로 보낸다.
			// return 되는 값은 memberVO로, 복수개가 리턴된다.	
			paraMap.put("sizePerPage",sizePerPage);
			
			// 검색이 있을 경우 시작 -- Map 속에 put 해준다. // // 즉, 검색이 있을경우 null 이 아니다.
			// searchType 에 (회원명,아이디,이메일) 이 외의 것들이 들어오지 못하게 막아준다.(get방식 이므로 다 막아줘야함)
			// 즉 위의 세 가지에 해당하는 것들이 view 로부터 들어올 때에만! map에 넣어주도록 한다.
			// return 은 해당메소드를 끝내는 것이다.
			if(searchType != null && !"".equals(searchType) && !"name".equals(searchType) && !"userid".equals(searchType) && !"email".equals(searchType)) {
				// 사용자가 웹브라우저 주소입력란에서 searchType 란에 장난친경우. (즉 컬럼명 이외의 것을 입력한 경우!!)
				String message = "부적절한 검색 입니다.";	// 장난치지 마세요!
				String loc = request.getContextPath()+"/member/memberList.up";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
			//	super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");	// 메세지를 띄운 후,
				
				return;	// execute() 메소드를 종료시킨다. (즉, map 에 넣어주지 않고 메소드를 종료하는 것임.)				
			}
			
			paraMap.put("searchType",searchType);
			paraMap.put("searchword",searchword);
			
			// 검색이 있을 경우 끝 -- Map 속에 put 해준다  //

			// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 전체 회원에 대한 총 페이지 알아오기.
			// 몇명인지 알아온 후에 페이지 블럭을 몇개 설정해야 할지 알아야 하기 때문에 DB 에서 가져온다.
			int totalPage = mdao.getTotalPage(paraMap);	// 현재 paraMap 속에 currentShowPageNo, sizePerPage 가 있다.			
		//	System.out.println(" ** 확인용 totalPage : " + totalPage);
		//	** 확인용 totalPage : 21	

			// totalPage 이상의 수를 주소창에서 입력했을 때 자동적으로 1페이지에 가있을 수 있게 세팅해놓는 것.			
			// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 totalPage 수 보다 큰 값을 입력하여
			//     장난친 경우 currentShowPageNo 는 1 페이지로 만들도록 한다. ==== //
			if( Integer.parseInt(currentShowPageNo) > totalPage) {
				currentShowPageNo = "1";	// 주소창에 토탈페이지수 이상의 수를 입력하여 장난친 경우에도 1page로 가도록 한다.
			};
			
			// map 에 넣어서 DB 로 보낸다.
			// return 되는 값은 memberVO로, 복수개가 리턴된다.			
			paraMap.put("currentShowPageNo",currentShowPageNo);	// total 페이지 이상의 값을 입력했을때 1페이지로 갈 수 있도록 위치를 옮겨준 것이다.
			
			List<MemberVO> memberList = mdao.selectPagingMember(paraMap);		// 한 페이지당 n 개씩 보여준다. (페이징 처리된 멤버를 불러온다.)			
	//		System.out.println("확인용 memberList : "+memberList.size());
			
			
			// view 단 페이지로 보여준다. 회원정보 목록을 보여준다. (select 된 항목들 복수개)
			// view 단에서 requsetScope 로 받는다.
			request.setAttribute("memberList", memberList);
			request.setAttribute("sizePerPage", sizePerPage);	// sizePerPage 를 jsp 에서 받아서 view 단으로 넘기자.
			
			
			// *** 페이지바 만들기 시작 ** //
			/*
	            1개 블럭당 10개씩 잘라서 페이지 만든다. (default 를 10개로 설정했을 때)
	            1개 페이지당 3개행 또는 5개행 또는  10개행을 보여주는데
	                만약에 1개 페이지당 5개행을 보여준다면 
	                총 몇개 블럭이 나와야 할까? 
	                총 회원수가 207명 이고, 1개 페이지당 보여줄 회원수가 5 이라면
	            207/5 = 41.4 ==> 42(totalPage)        
	                
	            1블럭               1 2 3 4 5 6 7 8 9 10 [다음]		--> 10개씩 잘라서 페이지 만든다.
	            2블럭   [이전] 11 12 13 14 15 16 17 18 19 20 [다음]	--> 10개씩
	            3블럭   [이전] 21 22 23 24 25 26 27 28 29 30 [다음]	--> 10개씩
	            4블럭   [이전] 31 32 33 34 35 36 37 38 39 40 [다음]
	            5블럭   [이전] 41 42 	--> 마지막 2명은 42페이지에 보여주면 된다. (2명이 41.4 페이지가 아니라 반올림한 42페이지에 가야하는 것이다.)
			*/	
			
			// ==== !!! pageNo 구하는 공식 !!! ==== // 
		      /*
		          1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은  1 이다.
		          11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.   
		          21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다.
		          
		           currentShowPageNo        pageNo  ==> ( (currentShowPageNo - 1)/blockSize ) * blockSize + 1 
		          ---------------------------------------------------------------------------------------------
		                 1                   1 = ( (1 - 1)/10 ) * 10 + 1 
		                 2                   1 = ( (2 - 1)/10 ) * 10 + 1 
		                 3                   1 = ( (3 - 1)/10 ) * 10 + 1 
		                 4                   1 = ( (4 - 1)/10 ) * 10 + 1  
		                 5                   1 = ( (5 - 1)/10 ) * 10 + 1 
		                 6                   1 = ( (6 - 1)/10 ) * 10 + 1 
		                 7                   1 = ( (7 - 1)/10 ) * 10 + 1 
		                 8                   1 = ( (8 - 1)/10 ) * 10 + 1 
		                 9                   1 = ( (9 - 1)/10 ) * 10 + 1 
		                10                   1 = ( (10 - 1)/10 ) * 10 + 1 
		                 
		                11                  11 = ( (11 - 1)/10 ) * 10 + 1 
		                12                  11 = ( (12 - 1)/10 ) * 10 + 1
		                13                  11 = ( (13 - 1)/10 ) * 10 + 1
		                14                  11 = ( (14 - 1)/10 ) * 10 + 1
		                15                  11 = ( (15 - 1)/10 ) * 10 + 1
		                16                  11 = ( (16 - 1)/10 ) * 10 + 1
		                17                  11 = ( (17 - 1)/10 ) * 10 + 1
		                18                  11 = ( (18 - 1)/10 ) * 10 + 1 
		                19                  11 = ( (19 - 1)/10 ) * 10 + 1
		                20                  11 = ( (20 - 1)/10 ) * 10 + 1
		                 
		                21                  21 = ( (21 - 1)/10 ) * 10 + 1 
		                22                  21 = ( (22 - 1)/10 ) * 10 + 1
		                23                  21 = ( (23 - 1)/10 ) * 10 + 1
		                24                  21 = ( (24 - 1)/10 ) * 10 + 1
		                25                  21 = ( (25 - 1)/10 ) * 10 + 1
		                26                  21 = ( (26 - 1)/10 ) * 10 + 1
		                27                  21 = ( (27 - 1)/10 ) * 10 + 1
		                28                  21 = ( (28 - 1)/10 ) * 10 + 1 
		                29                  21 = ( (29 - 1)/10 ) * 10 + 1
		                30                  21 = ( (30 - 1)/10 ) * 10 + 1                    

		       */
			
			String pageBar = "";
			
			int blockSize = 10;	// 회원명수옆에 있는 박스(sizePerPage)가 아니라, 페이지바에 있는 것.
			// blockSize 는 블럭(토막) 당 보여지는 페이지 번호의 개수이다. --> 페이지네이션에서 보여지는 바의 갯수
			
			int loop = 1;	// 페이지바의 10개묶음 패밀리를 반복하자! (1부터 10번 반복)
			// loop 는 1부터 증가하여 1개의 블럭을 이루는 페이지 번호의 갯수(지금은 blockSize를 10개로 설정해둠) 까지만 증가하는 용도이다.
			
			// === 다음은 pageNo 를 구하는 공식이다. ===
			int pageNo = ( (Integer.parseInt(currentShowPageNo)- 1)/blockSize ) * blockSize + 1;	// 페이지바에서 누른 pageNo
			// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
			
			if(searchType == null) {
				searchType = "";	// 검색타입이 설정되지 않았다면, 주소창에서 null 이었던 것을 "" 로 바꾸겠다.
			}
			if(searchword == null) {
				searchword = "";	// 검색어가 입력되지 않았다면, 주소창에서 null 이었던 것을 "" 로 바꾸겠다.
			}
			
			// *** [맨처음] [이전] 버튼 만들기 *** // searchType=name&searchword=유&sizePerPage=10 처럼 검색했을때 보이도록 하기 (getparameter 로 가져온 name 값을 가져온다.)
			if(pageNo != 1) {	// 맨 처음페이지(1page)블럭이 아닐때에만 [맨처음] [이전]버튼을 보여라.
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?currentShowPageNo=1&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchword="+searchword+"'>[맨처음]</a></li>";
				// 맨 처음은 항상 1페이지 == 무조건 1이다. (currentShowPageNo=1)
				
				// 1페이지에서의 이전은 없기 떄문에 1페이지에 머물러 있을 때 [이전] 버튼을 없애준다.
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchword="+searchword+"'>[이전]</a></li>";
				// 현재페이지에서 보여주는 페이지보다 한페이지 적어야 한다.
			}
			
			
			while( !(loop > blockSize || pageNo > totalPage) ) {	// 페이지바에 누적이 되어야 한다.  while 문에 (pageNo > totalPage) 조건을 통해 totalpage 이상의 값이 나오지 않도록 한다.
				// loop 가 blocksize 보다 클 때 반복문을 빠져나온다.
				// totalPage 이상은 없기때문에 pageNo > totalPage 가 되면 빠져나와라. 
				// pageNo 가 1부터 10번 반복
				if(pageNo == Integer.parseInt(currentShowPageNo)) {	// 내가 선택한 페이지와 그 페이지 번호가 같다면, 해당 페이지바 버튼에 active 표시하고, 현재페이지('#') 로 링크이동하도록 한다. 
					// ul 태그 속에는 li가 들어간다. href 뒤에 '/' 가 없으므로 상대경로 이다. (맨 뒤에것만 바뀐다. 자신 --> 자신에게 간다.)
					pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
				}
				else {	// 내가 선택한 페이지와 그 페이지 번호가 다르다면, 해당 페이지에 표시 active 표시를 하지 X
				// 	<%-- 부트스트랩을 통해 내가 클릭한 페이지가 어딘지 표시해주는 효과를 주겠다. --%>	
					pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchword="+searchword+"'>"+pageNo+"</a></li>";
				}
					
				// while 문이 돌 때마다 loop 를 증가시킨다. pageNo 역시 증가를 시켜줘야 한다.
				loop++;
				pageNo++;		
					
			}// end of while( !(loop > blockSize) ) {}------------------
			
			// 위의 while 문을 빠져나오게 되면, 11 페이지로 이동한다. --> 다음 블럭으로 이동한다.
			// *** [다음] [마지막] 만들기 *** ///	pageNo > totalPage : 맨 마지막 페이지
			// pageNo ==> 11 / currentShowPageNo 가 11이 된다는 뜻이다. (공식에 따라서 11이 된 것이다.) --> [다음]버튼 클릭 시 11부터 10번 반복한다.
			if(pageNo <= totalPage) {	// 즉, 맨 마지막 페이지에 있을때 [다음], [마지막]버튼을 보이지 않게 한다. 그 반대일때는 보이게 해라.
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchword="+searchword+"'>[다음]</a></li>";
				
				// totalPage : 마지막 페이지 [마지막] 버튼 클릭시 			
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?currentShowPageNo="+totalPage+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchword="+searchword+"'>[마지막]</a></li>";
			}
			// 원래 total 페이지가 21페이지까지인데, 21~30 / 31~40 .... 이렇게 계속 반복문으로 돌아가게 된다. 즉, 22부터 나오면 안된다는 뜻이다. -->  while 문에 (pageNo > totalPage) 조건을 추가한다.
						
			
			request.setAttribute("pageBar", pageBar);	// view 단에 페이지바도 넘겨줌. (문자열인 li 태그가 넘어온다.)
			
			// *** 페이지바 만들기 끝 ** //
			
			// **** 현재 페이지를 돌아갈 페이지(goBackURL)로 주소 지정하기 **** //
			// 예를 들어 http://localhost:9090/MyMVC/member/memberList.up?currentShowPageNo=3&sizePerPage=10&searchType=name&searchword=유 와 같은 주소창을 기억하도록 해야한다.
			// my.util 패키지를 생성하고 자주 사용할 메소드를 하나 따로 만들자. (FrontController 참고)
			// my.util 패키지의 getCurrnetURL 메소드 참고 ('?' 다음의 데이터까지 포함한 현재 URL 주소를 알려주는 메소드)
			// 회원 조회를 했을 때, 현재 그 페이지로 그대로 되돌아가기 위한 용도로 쓰인다. (단순히 javascript 이전이 아님)
			String currentURL = MyUtil.getCurrnetURL(request);	// 파라미터에 request 를 담아온다.
			
		//	System.out.println("확인용 currentURL : " + currentURL);
			/*
			 확인용 currentURL : /member/memberList.up?
			 확인용 currentURL : /member/memberList.up?currentShowPageNo=10&sizePerPage=10&searchType=&searchword=%EC%9C%A0
			 확인용 currentURL : /member/memberList.up?searchType=name&searchword=%EC%9C%A0&sizePerPage=10			
			*/		
			
			// URL 에는 띄어쓰기가 없다. 이것을 이용해서 & 에 띄어쓰기를 준다. (웹페이지에는 공백이 없다.) 이 값이 공백으로 넘어간다.
			currentURL = currentURL.replaceAll("&", " ");
			//	System.out.println("확인용 currentURL : " + currentURL);
			/*
			 확인용 currentURL : /member/memberList.up?
			 확인용 currentURL : /member/memberList.up?currentShowPageNo=10 sizePerPage=10 searchType= searchword=%EC%9C%A0
			 확인용 currentURL : /member/memberList.up?searchType=name searchword=%EC%9C%A0&sizePerPage=10			
			*/
			
			request.setAttribute("goBackURL", currentURL);	// 현재 페이지가 원래 있던 페이지로 돌아갈 페이지 이다.
			// view 단에 & 가 공백으로 바뀐다.
			// 검색 대상 받은것을 그대로 view 단 페이지에 넘겨줘야 한다. (회원명인지, 아이디인지, 이메일인지)
			// 검색이 있을 때만(if) getparameter로 넘겨받은 것(searchType과 searchword) 넘겨주면 된다. --> null 이라면 이렇게 바꿔온 것이다. null 일때만 if를 쓰고,
			// 위에서 searchType 과 searchword 를 "" 를 설정해줬기 때문에 아래처럼 코드를 쓴다.
			request.setAttribute("searchType", searchType);			
			request.setAttribute("searchword", searchword);			
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberList.jsp");
		}
		
	}
}
