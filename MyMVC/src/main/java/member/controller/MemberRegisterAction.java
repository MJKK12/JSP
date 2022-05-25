package member.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.*;

public class MemberRegisterAction extends AbstractController {
// 항상 추상클래스를 상속받는다.
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		// 돌아갈 페이지를 session 에다가 먼저 기억을 시켜둔 것이다 (부모클래스 가서 상세내용 참고)
		super.goBackURL(request);	// 현재 페이지를 기억하기 위함이다. (로그인/아웃을 하든 사용자가 머무르고 있던 페이지에 머무르게 해야한다.)
		
		String method = request.getMethod();		// 회원가입할 때 무엇인지 물어보기
		
		if("GET".equalsIgnoreCase(method)) { // method 가 get 방식이라면, 아래와 같이 띄운다.			
			// super.setRedirect(false); 
			super.setViewPage("/WEB-INF/member/memberRegister.jsp");	// view 단 페이지가 어딘지 보인다.
		}
		else {
			// 가입하기 버튼을 클릭했을 경우 
			// 값을 전송했으니 받아와야 한다. (form 태그에서 넘어온 값을 받아와야 한다.)
			String name = request.getParameter("name");		// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String userid = request.getParameter("userid");	// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String pwd = request.getParameter("pwd");		// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String email = request.getParameter("email");	// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String hp1 = request.getParameter("hp1");		// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String hp2 = request.getParameter("hp2");		// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String hp3 = request.getParameter("hp3");		// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String postcode = request.getParameter("postcode");				// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String address = request.getParameter("address");				// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String detailaddress = request.getParameter("detailAddress");	// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String extraaddress = request.getParameter("extraAddress");		// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String gender = request.getParameter("gender");					// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String birthyyyy = request.getParameter("birthyyyy");			// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String birthmm = request.getParameter("birthmm");				// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			String birthdd = request.getParameter("birthdd");				// 받아온 값을 vo 에 넣어서 DB 에 insert 해야 한다.
			
			String mobile = hp1+hp2+hp3;
			String birthday = birthyyyy+"-"+birthmm+"-"+birthdd;
			
			MemberVO member = new MemberVO(userid, pwd, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, birthday);

		/*			
			String message = "";
			String loc = "";
			
			try {
				InterMemberDAO mdao = new MemberDAO();
				int n = mdao.registerMember(member);	// DB 에 insert. 
				
				if(n==1) { // db 에 들어갔다면!
					message = "회원가입 성공!";
					loc = request.getContextPath()+"/index.up"; // 시작 페이지로 이동한다. (절대경로로 보낸다.)
					//	  /MyMVC/index.up
					
				}
			
			}catch (SQLException e) {
				message = "SQL구문 에러발생";
				loc = "javascript:history.back()"; // 자바스크립트를 이용한 이전페이지로 이동한다.
				e.printStackTrace();
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp"); */
		
			
			// ** 회원가입이 성공되면 자동으로 로그인 되도록 하겠다. ** //
			// userid 는 고유하므로 1 이 return 된다. (int n)
			// '1' 행이 삽입된 것.
			
			try {
				InterMemberDAO mdao = new MemberDAO();
				int n = mdao.registerMember(member);	// DB 에 insert. (DB 에 정식으로 회원 가입 완료)
				
				if(n==1) { // db 에 들어갔다면! (int n==1 이면 정상적으로 들어간 것.)

					request.setAttribute("userid", userid);	// 가입한 userid 를 view 단 페이지로 보내준다.
					request.setAttribute("pwd", pwd);
				
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/login/registerAfterAutoLogin.jsp");	// 회원가입 후 자동으로 로그인 되는 페이지로 간다.
				}
			
			}catch (SQLException e) {				
				e.printStackTrace();
				
			   String message = "SQL구문 에러발생";
               String loc = "javascript:history.back()"; // 자바스크립트를 이용한 이전페이지로 이동하는것.
            
               request.setAttribute("message", message);
               request.setAttribute("loc", loc);
               
            //   super.setRedirect(false); 
               	 super.setViewPage("/WEB-INF/msg.jsp");
			}
			////////////////////////////////////////////////////////////
			
		}
		
	}

}
