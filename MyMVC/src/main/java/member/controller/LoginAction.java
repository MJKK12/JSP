package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;

public class LoginAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 유저가 장난 치지 못하게 막는다.
		
		String method = request.getMethod();	// "GET" 또는 "POST" 방식
		
		if(!"post".equalsIgnoreCase(method)) {
			// POST 방식으로 넘어온 것이 아니라면 (GET 방식이라면)
			// http://localhost:9090/MyMVC/login/login.up?userid=leess&pwd=qwer1234$ 이런식으로 들어왔다면
			String message = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back()";	// 이전페이지로 간다. (암기 !)
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		else {
			// POST 방식으로 넘어왔다면 (아이디와 비번이 post 방식에 따라 올바르게 넘어온 것) 
			String userid = request.getParameter("userid");
			String pwd = request.getParameter("pwd");
			
			// 로그인 기록을 남기기 위해 IP 기록을 가져와야 한다.
			// **** 클라이언트의 IP 주소를 알아오는 것 **** //
			String clientip = request.getRemoteAddr();	// 연결된 client 의 ip.
			// C:\NCS\workspace(jsp)\MyMVC\src\main\webapp\JSP 파일을 실행시켰을 때 IP 주소가 제대로 출력되기위한 방법.txt 을 참조하자.

		//	System.out.println("확인용 clientip : " + clientip);
			// 확인용 clientip : 127.0.0.1

			Map<String, String> paraMap = new HashMap<>();	// map 으로 넘겨준다.
			paraMap.put("userid", userid);
			paraMap.put("pwd", pwd);
			paraMap.put("clientip", clientip);
			
			// DB 로 가야하므로 DAO 로 간다.
			InterMemberDAO mdao = new MemberDAO();
			
			// 로그인 후, 한 사람에 대한 모든 정보를 가져온다.
			MemberVO loginuser = mdao.selectOneMember(paraMap);
			
			if(loginuser != null) {
				// 넘겨 받은 것이 null 이 아니라면, (아이디,비번이 맞아서 로그인성공)
				if(loginuser.getIdle() == 1)	{	// 로그인 한지 1년이 경과되어 휴면처리가 되었다면, (활동:0 , 휴면:1)
					String message = "로그인 한지 1년이 경과되어 휴면상태로 전환되었습니다. 관리자에게 문의 바랍니다.";	//→ msg.jsp 에 가서 alert 를 띄워준다.
					String loc = request.getContextPath()+"/index.up";	// request.getContextPath() : /MyMVC
					// 원래는 위와 같이 /index.up 이 아니라 휴면 계정을 풀어주는 페이지로 잡아줘야 한다. --> ex)/휴면계정풀어주는page.up
					
					request.setAttribute("message", message);	// msg.jsp에서 {~~~.key}
					request.setAttribute("loc", loc);	// msg.jsp에서 {~~~.key}

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");	
					
					return;	// execute() 메소드 종료 (즉, 다음 단계로 넘어가지 말고 execute 메소드는 여기서 끝내라는 뜻)
				}
				
				 // !!!! session(세션) 이라는 저장소에 로그인 되어진 loginuser 을 저장시켜두어야 한다.!!!! //
		         // session(세션) 이란 ? WAS 컴퓨터의 메모리(RAM)의 일부분을 사용하는 것으로 접속한 클라이언트 컴퓨터에서 보내온 정보를 저장하는 용도로 쓰인다. 
		         // 클라이언트 컴퓨터가 WAS 컴퓨터에 웹으로 접속을 하기만 하면 무조건 자동적으로 WAS 컴퓨터의 메모리(RAM)의 일부분에 session 이 생성되어진다.
		         // session 은 클라이언트 컴퓨터 웹브라우저당 1개씩 생성되어진다. 
		         // 예를 들면 클라이언트 컴퓨터가 크롬웹브라우저로 WAS 컴퓨터에 웹으로 연결하면 session이 하나 생성되어지고 ,
		         // 또 이어서 동일한 클라이언트 컴퓨터가 엣지웹브라우저로 WAS 컴퓨터에 웹으로 연결하면 또 하나의 새로운 session이 생성되어진다. 
		         /*
		               -------------
		               | 클라이언트    |             ---------------------
		               | A 웹브라우저  | -----------|   WAS 서버        |
		               -------------             |                  |
		                                         |  RAM (A session) |
		               --------------            |      (B session) | 
		               | 클라이언트     |           |                  |
		               | B 웹브라우저   | ----------|                  |
		               ---------------           --------------------
		               
		           !!!! 세션(session)이라는 저장 영역에 loginuser 를 저장시켜두면
		                Command.properties 파일에 기술된 모든 클래스 및  모든 JSP 페이지(파일)에서 
		                     세션(session)에 저장되어진 loginuser 정보를 사용할 수 있게 된다. !!!! 
		                     그러므로 어떤 정보를 여러 클래스 또는 여러 jsp 페이지에서 공통적으로 사용하고자 한다라면
		                     세션(session)에 저장해야 한다.!!!!          
		          */
				
				HttpSession session = request.getSession();	
				// 메모리에 생성된 session 을 불러오는 것이다.
				
				session.setAttribute("loginuser", loginuser);	// 로그인 user 값을 가져온다.
				// session에 로그인 된 사용자 정보인 loginuser 의 키이름을 "loginuser" 로 저장시켜 둔다.
				// session 에 loginuser 로 저장해둔 것이다.
				
				// 로그인 한지 1년이 경과되지 않은 경우는 아래의 명령을 수행한다. (활동중:0)			
				if(loginuser.isRequirePwdChange() == true) {
					// boolean type 은 is 이다.
					// requirePwdChange 가 true 라면 마지막으로 암호를 변경한 날짜가 3개월이 지난것이다. (VO 에 requirePwdChange 를 false 기본값으로 설정함.)
					String message = "비밀번호를 변경하신지 3개월이 경과되었습니다.비밀번호를 변경해주세요!";	//→ msg.jsp 에 가서 alert 를 띄워준다.
					String loc = request.getContextPath()+"/index.up";	// request.getContextPath() : /MyMVC
					// 원래는 위와 같이 /index.up 이 아니라 비밀번호 변경 페이지로 잡아줘야 한다. --> ex)/비밀번호 변경 page.up
					
					request.setAttribute("message", message);	// msg.jsp에서 {~~~.key}
					request.setAttribute("loc", loc);	// msg.jsp에서 {~~~.key}

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");	

				}
				else {   // (isRequirePwdChange() == true)
					// 비밀번호를 변경한지 3개월 이내인 경우 → 즉, 비밀번호 변경 페이지로 이동 XX
					
					// setRedirect 가 false(forward방식) 라면 setViewPage 를 보여준다. 
					// 하지만 새롭게 보여줄 페이지가 없으면 원래 그대로의 페이지에 간다. true 이면 Redirect 방식 - goBackURL 로 간다.
					// 페이지 이동을 시킨다.
					super.setRedirect(true);
					
					// 로그인을 하면 시작페이지(index.up)로 가는 것이 아니라 로그인을 시도하려고 머물렀던 그 페이지로 가기 위한 것이다.
					String goBackURL = (String)session.getAttribute("goBackURL");	// 부모클래스의 key 값을 가져온다.
					// getAttribute 는 객체타입이므로 다시 String 으로 바꿔준다.
					/* 현재 보고있던 페이지 예시 : http://localhost:9090/MyMVC/shop/prodView.up?pnum=59 (제품번호 59번에 해당하는 페이지)
					   또는 null (null 이라면, /index.up 으로 갈 것이고 null 이 아니면 돌아갈페이지(goBackURL())로 간다.
					*/

					if(goBackURL != null) {
						// 돌아갈 페이지가 있다면 (원래 머무르고 있던 페이지로 이동)
						super.setViewPage(request.getContextPath()+goBackURL);							
					}
					else {
						// 돌아갈 페이지가 없다면 --> 시작페이지로 이동
						super.setViewPage(request.getContextPath()+"/index.up");	//request.getContextPath():절대경로(/MvMVC)					
					}
				}				
			}
			else {
				// 넘겨 받은 것이 null 이면 (로그인 실패)
				String message = "로그인 실패!";	//→ msg.jsp 에 가서 alert 를 띄워준다.
				String loc = "javascript:history.back()";
				request.setAttribute("message", message);	// msg.jsp에서 {~~~.key}
				request.setAttribute("loc", loc);	// msg.jsp에서 {~~~.key}

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
			}
			
		}
		
	}

}
