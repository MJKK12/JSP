package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;

public class VerifyCertificationAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userCertificationCode = request.getParameter("userCertificationCode");	// 사용자가 전송한 인증코드
		String userid = request.getParameter("userid");
		
		// 세션불러오기 . 발급한 인증코드를 세션에 저장시킨다. (다른 자바,jsp 에서 쓸수 있게 끔 한다.)
		HttpSession session = request.getSession();		
		// 세션에 저장된 인증코드 가져오기.
		String certificationCode = (String) session.getAttribute("certificationCode");	// return 타입이 object. (실제 타입은 String 타입이다.) 따라서 String 타입으로 형변환 해야한다.

		String message = "";
		String loc = "";
		
		// 발급된 인증키 코드와 유저가 입력(보낸)한 인증키코드가 일치한지 아닌지
		if(certificationCode.equals(userCertificationCode)) {
			message = "인증이 성공 되었습니다.";
			loc = request.getContextPath()+"/login/pwdUpdateEnd.up?userid="+userid;	// 누구(userid)의 암호를 바꿀 것인지를 form태그에서 보내줘야 한다.
			// 유저가 입력한 인증키가 맞다면, "새로운 비밀번호 입력" 하는 창으로 새롭게 view 단 페이지가 변경되어야 한다.
			
		}
		else {	// alert 창은 message 와 loc로 처리한다.!
			message = ("발급된 인증 코드가 아닙니다. 인증코드를 재발급 받으시기 바랍니다."); // 틀렸으면 다시 해당 페이지로 가야함.
			loc = request.getContextPath()+"/login/pwdFind.up";	// 다시 비밀번호 입력창으로 간다.
			
		}
		
		// 메세지와 loc 를 함께 넘겨주어야 한다.
		request.setAttribute("message", message);	
		request.setAttribute("loc", loc);	
	
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
		
		
		// *** 중요 *** //
		// *** 세션에 저장된 인증코드 삭제하기.!!! *** // 즉, 잘못 입력한 인증코드를 삭제하고 다시 인증코드를 재발급 받아야 한다.
		session.removeAttribute("certificationCode");
		
		
		
	}
}
