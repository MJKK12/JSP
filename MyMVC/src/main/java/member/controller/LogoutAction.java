package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class LogoutAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 로그아웃 처리하기		
		HttpSession session = request.getSession();		// 세션 불러오기 
		
		////////////////////////////////////////////////////////////////////
		// 로그아웃을 하면 시작페이지로 가는 것이 아니라, 방금 보았던 그 페이지 그대로 가기 위한 것이다.
		String goBackURL = (String)session.getAttribute("goBackURL");	// 세션에서 읽어오자.
		
		if(goBackURL != null) {	
			goBackURL = request.getContextPath()+goBackURL;
							//	MyMVC
		}
		///////////////////////////////////////////////////////////////////////
		/*  로그아웃 시 주의 !!
			관리자는 본인이 보던 자신의 페이지 기록을 남기면 안됨. (관리자 전용 페이지는 관리자 외 다른 회원이 보아서는 안됨)
			즉, 관리자가 본인이 보던 관리자전용 페이지에서 로그아웃을 할 때 해당 페이지에 그대로 남아있도록 기록을 남기고, 
			session 을 invalidate 한 다음에 index 페이지로 가도록 한다. 
			(본인이 하던 작업 보호 --> 보안)
		 */

		super.setRedirect(true);	// 일때는 해당 jsp 페이지로 바로 가는 것이 아니라, Redirect 해서 시작페이지로 가는 것이다. (로그아웃 페이지를 따로 만드는 것이 X, 메인으로 보낸다.)

		if(goBackURL != null && !"admin".equals( ((MemberVO)session.getAttribute("loginuser")).getUserid() )) {	
			// 관리자가 아닌 일반 사용자로 들어와서 돌아갈 페이지가 있을 때 돌아갈 페이지로 돌아간다.

			// 세션 없애기 두 가지 방법		
			/* 첫번째 방법 : 세션을 그대로 존재하게끔 두고, 세션에 저장된 어떤 값(지금은 로그인 된 회원객체 - MemberVo(세션에 저장된 MemberVo 이다.)을 삭제하기
			
			   session.removeAttribute("loginuser"); // LoginAction 에 setAttribute 한 key 값을
			   없애는 것이다.
			*/		
			
			// 두번째 방법 : WAS 메모리 상에서 세션을 아예 삭제해버리기		
			session.invalidate();	// 세션을 아예 없애버린다. (더 자주사용하는 방법)			// 해당 URL 이 세션에 남아있다면.해당페이지에 머무르도록 한다. && 어드민이 아닐때. (session 에서 가져온 loginuser 의 id != admin)
			super.setViewPage(goBackURL);
		}
		else {
			// 돌아갈 페이지가 없거나 관리자로 로그아웃을 하면 무조건/MyMVC/index.up 페이지로 돌아간다. (관리자가 보던 페이지를 그대로 남겨두면 안된다. --> 다른사람이 보면 안됨.)
			session.invalidate();	// 세션을 아예 없애버린다. (더 자주사용하는 방법)			// 해당 URL 이 세션에 남아있다면.해당페이지에 머무르도록 한다. && 어드민이 아닐때. (session 에서 가져온 loginuser 의 id != admin)
			super.setViewPage(request.getContextPath()+"/index.up");	// 로그아웃 되면 시작페이지로 간다.
		}


	}
}
