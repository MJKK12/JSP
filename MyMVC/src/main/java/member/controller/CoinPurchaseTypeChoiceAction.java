package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseTypeChoiceAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 우리의 목표는 로그인 하지 않은 상태에서 get 방식에서 주소창에 그대로 쳤을 때 해당 창이 그대로 나오는 것이 아니라,
		// 로그인 했을 때에만 보일 수 있도록 하는 것이다.
		// 코인 충전을 하기 위한 전제 조건은 먼저 로그인을 해야하는 것이다.

		// request 를 넣어주면, 부모클래스에서 true 인지 false 인지를 반환한다.
		if(super.checkLogin(request)) {
			// 로그인을 했으면

			String userid = request.getParameter("userid");	//jsp의 userid 값. (로그인 했으면 받아오자!)
			// 해당 세션에 저장된 key 값 (get userid) 로그인 user 의 getuserid
			
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");	// return 타입이 object 이다.
			
			if(loginuser.getUserid().equals(userid)) {	// 로그인을 했기 때문에 절대 null 이 아니고, String 이 리턴값이다.
				// (정상적) 로그인한 사용자가 자신의 코인을 충전하는 경우
				
				
				//	super.setRedirect(false);	정상적으로 접근했을 경우, 아래의 주소로 이동한다.
				super.setViewPage("/WEB-INF/member/CoinPurchaseTypeChoice.jsp");				
			}
			else {
				// (비정상) 로그인한 사용자가 다른 사용자의 코인을 충전하려고 하는 경우 (즉, 사용자가 장난을 치려고 하는 경우이다.)
				String message = "다른 사용자의 코인충전 시도는 불가합니다.!!";
				String loc = "javascript:history.back()";	// 이전페이지로 이동한다.
			
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				//	super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");	// msg 로 이동하도록 한다.
			}

		}
		else {
			// 로그인을 하지 않았으면
			String message = "코인충전을 하기 위해서는 로그인이 필수 입니다!!!";
			String loc = "javascript:history.back()";	// 이전페이지로 이동한다.
		
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");	// msg 로 이동하도록 한다.
			
		}
		
		
	}

}
