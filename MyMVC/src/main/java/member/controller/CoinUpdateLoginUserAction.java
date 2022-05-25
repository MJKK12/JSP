package member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class CoinUpdateLoginUserAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// DB 에 업데이트 해주자.
		// login.jsp 에서 userid 와 coinmoney 가 넘어왔다.
		String userid = request.getParameter("userid");
		String coinmoney = request.getParameter("coinmoney");
		
		Map<String, String> paraMap = new HashMap<>();
		// paraMap 에 담는다.
		paraMap.put("userid", userid);
		paraMap.put("coinmoney", coinmoney);
		
		// 그리고 DAO 를 가져온다.
		InterMemberDAO mdao = new MemberDAO();
		
		int n = mdao.coinUpdate(paraMap);	// DB에 코인 및 포인트 증가하기 (paraMap 을 보내서 Update 한다.)
											
		String message = "";
		String loc = "";
		
		
		// dao 에서 넘어온 return 값이 1 이라면! ('1 행'이 업데이트 되었습니다 = 1 )
		if(n==1) {	// update 가 성공적으로 된 것이다.( userid, coinmoney)
			
			HttpSession session = request.getSession();	// session 에 다 들어와 있음
			MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
			
			// ** [세션값을 변경하기] : 세션은 어떤 것을 하기 전에 읽어온다. 자동적으로 세션도 바꿔줘야 한다. 매번 로그아웃 할 필요가 없다. 
			// 결제완료가 되면, 세션의 코인액/포인트 항목까지 그에 맞게 업데이트를 해줘야 한다. ** //
			loginuser.setCoin(loginuser.getCoin() + Integer.parseInt(coinmoney));	// setCoin(현재로그인유저.코인 + 업데이트된 coinmoney) 코인액을 새롭게 충전한 금액으로 동일하게 update 해줘야 한다.
			loginuser.setPoint(loginuser.getPoint() + (int)(Integer.parseInt(coinmoney) * 0.01));	// 포인트액을 새롭게 충전한 금액으로 동일하게 update 해줘야 한다.
			
			message = loginuser.getName() +"님의 "+coinmoney+"원 결제가 완료됐습니다.";	// 누구인지(loginuser) 알아와야 한다. (따라서 위에 session 을 씀으로써 불러온다.)
			loc = request.getContextPath()+"/index.up";	// 시작페이지로 이동한다.
		}
		
		else {	// DB 에서 update 에 실패한 것이다.
			message = "코인액 결제에 실패했습니다.";
			loc = "javascript:history.back()";	// 실패시 이전 페이지로 이동			
		}
		
		// 메세지와 loc 를 보내준다.
		request.setAttribute("message", message);
		request.setAttribute("loc", loc);
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
	}

}
