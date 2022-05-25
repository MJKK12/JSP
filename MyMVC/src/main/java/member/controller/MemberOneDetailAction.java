package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;

public class MemberOneDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)

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
			
			// 값을 받아오자.
			String userid = request.getParameter("userid");	//memberList.jsp 에서 넘겨준 userid
			InterMemberDAO mdao = new MemberDAO();
			MemberVO mvo = mdao.memberOneDetail(userid);		// userid 를 where 절에 넘어가야 한다.

			// dao 에서 한사람에 대해 정보를 얻어온 것을 view 단에 넘겨 주자.
			request.setAttribute("mvo", mvo);
			
			// 현재 페이지를 돌아갈 페이지(goBackURL)로 주소 지정 하기
			String goBackURL = request.getParameter("goBackURL");	// memberList.jsp 에서 가져온다.
			System.out.println("확인용 goBackURL => " + goBackURL);
			// 확인용 goBackURL => /member/memberList.up?currentShowPageNo=5 sizePerPage=10 searchType=name searchword=유
			// goBackURL 을 할 때는 전체를 전송하고 싶은데 , data 속에 & 이 포함되어 있다. (데이터 구분자)
			// memberListAction 에서 & 을 "" 로 바꿔온 것을 가져온다.
			
			request.setAttribute("goBackURL", goBackURL);	// 돌아갈 페이지를 view 단으로 보낸다.
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberOneDetail.jsp");
		}
		
	}

}
