package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class PwdUpdateEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Verify~~~ 에서 request 한 것을 가져온다.	
		String userid = request.getParameter("userid");	// GET 이든지 POST 이든지 공통!

		String method = request.getMethod();	// 전송방식을 알아온다. (GET 또는 POST)

		if("POST".equalsIgnoreCase(method)) {
			String pwd = request.getParameter("pwd");
			
			// Map 에 담자.
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("pwd", pwd);
			paraMap.put("userid", userid);	// Update 해야하므로 누구인지도 알아와야 한다.
			
			InterMemberDAO mdao = new MemberDAO();
			int n = mdao.pwdUpdate(paraMap);	// pwdUpdate 하는 메소드를 만든다.
			// Update 이므로 int 로 리턴. (바뀌면 1 값으로 나오므로 int값 반환)
			
			request.setAttribute("n", n);	// DAO 에서 정상적으로 바뀌면 1 값으로 나와서, 그 1을 넘겨준다.
			// 정상적으로 1값이 넘어오면 (userid 는 고유하기 떄문에 1이 넘어옴.) view단 페이지로 넘겨준다.
			// 즉, n이 1값으로 넘어가야 view 단에서 암호변경이 되도록 창을 띄운다.
	//		String message = "암호가 성공적으로 변경되었습니다!";
			
		}
		
		request.setAttribute("userid", userid);		
		request.setAttribute("method", method);	// get 방식인지 post 방식인지 넘겨주자.
		
	//	super.setRedirect(false);	~.jsp 이므로 false 이다.
		super.setViewPage("/WEB-INF/login/pwdUpdateEnd.jsp");
		
		
	}

}
