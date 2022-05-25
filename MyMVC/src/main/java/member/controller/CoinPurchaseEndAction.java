package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// request 를 넣어주면, 부모클래스에서 true 인지 false 인지를 반환한다.
		// 아임포트 결제창을 사용하기 위한 전제조건을 [ 먼저 로그인 ]을 해야하는 것이다.
		if(super.checkLogin(request)) {
			// 로그인을 했으면

			String userid = request.getParameter("userid");	//jsp의 userid 값. (로그인 했으면 받아오자!)
			// 해당 세션에 저장된 key 값 (get userid) 로그인 user 의 getuserid
			
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");	// return 타입이 object 이다.
			
			if(loginuser.getUserid().equals(userid)) {	// 로그인을 했기 때문에 절대 null 이 아니고, String 이 리턴값이다.
				// (정상적) 로그인한 사용자가 자신의 코인을 충전하는 경우	(login인 시 입력한 login값 = DB 에 있는 userid)
				
				String coinmoney = request.getParameter("coinmoney");	// login.jsp 의 결제 단계창에서 보낸 coinmoney 를 가져온다.

				// 다 담는다. --> paymentGateWay.jsp 로 key 값을 보낸다.
				request.setAttribute("coinmoney", coinmoney);				
				request.setAttribute("email", loginuser.getEmail());	// email 을 담는다.
				request.setAttribute("name", loginuser.getName());		// 이름을 담는다.
				request.setAttribute("mobile", loginuser.getMobile());		// 핸드폰 번호를 담는다.
				request.setAttribute("userid", userid);		

				//	super.setRedirect(false);	정상적으로 접근했을 경우, 아래의 주소로 이동한다.
				super.setViewPage("/WEB-INF/member/paymentGateway.jsp");				
			}
			else {
				// (비정상) 로그인한 사용자가 다른 사용자의 코인을 충전하려고 하는 경우 (즉, 사용자가 장난을 치려고 하는 경우이다.)
				String message = "다른 사용자의 코인충전 결제는 불가합니다.!!";
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
