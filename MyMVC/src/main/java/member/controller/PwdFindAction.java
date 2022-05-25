package member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class PwdFindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// form 태그에서 보낸 id 와 email 을 받아와야 한다. (POST 방식으로 보냈을 때만 해야 한다.)	

			String method = request.getMethod();	// "GET" 또는 "POST"
			
			if("POST".equalsIgnoreCase(method)) {
				// '비밀번호찾기' 모달창에서 '찾기' 버튼을 눌렀을 경우
				// 유저가 입력한 아이디와 이메일이 넘어온다.
				String userid = request.getParameter("userid");
				String email = request.getParameter("email");
				
				InterMemberDAO mdao = new MemberDAO();
				
				// map 에 실어서 id 와 email 을 DAO 에 보내자. → 이러한 아이디가 DB 에 미리 존재 해야 함. (알아보기 위한 메소드를 만들자.)
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("userid", userid);
				paraMap.put("email", email);
				
				// 아이디와 이메일이 존재할 때에만! map 에 실어서 보내자.
				boolean isUserExist = mdao.isUserExist(paraMap);		// 유저가 존재하는지? 아닌지? (booelan)
				
				// 메일이 정상적으로 전송되었는지 유무를 알아오기 위한 용도
				boolean sendMailSuccess = false;	// boolean 값이므로 초기화값을 false 로 설정한다.
				
				if(isUserExist) {
					// 회원으로 존재하는 경우에 인증키를 생성하여 이메일로 발송한다.
					
					// 인증키를 랜덤하게 생성하도록 한다.
					Random rnd = new Random();
					
					String certificationCode = "";					
					// 인증키는 영문소문자 5글자 + 숫자 7글자로 만들겠다.
					// 예: certificationCode ==> abcde1234567
					
					char randchar = ' ';
					for(int i=0; i<5; i++) {
						/*
		                min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
		                int rndnum = rnd.nextInt(max - min + 1) + min;
	                   영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.     
	             	*/
						randchar = (char) (rnd.nextInt('z' - 'a' + 1) + 'a');
						certificationCode += randchar;	// 랜덤한 5개 영문자를 인증키에 차곡차곡 쌓아 넣겠다.
					}// end of for-----------------------------------------

					int randnum = 0;
					for(int i=0; i<7; i++) {
						randnum = rnd.nextInt(9 - 0 + 1) + 0;
						certificationCode += randnum;	// 랜덤한 숫자 7개를 인증키에 차곡차곡 넣겠다.
					}// end of for---------------------------------------
					
				//	System.out.println("확인용 certificationCode : " + certificationCode);
				// 	확인용 certificationCode : xbizd4833794
										
				// 랜덤하게 생성한 인증코드(certificationCode)를 비밀번호 찾기를 하고자 하는 사용자의 이메일로 전송한다.
					GoogleMail mail = new GoogleMail();	// GoogleMail 이 인스턴스 메소드이므로 객체를 하나 만든다.
					
					try {
						mail.sendmail(email, certificationCode);						
						sendMailSuccess = true;		// 메일 전송이 성공했음을 기록함. (위에서 sendMailSuccess 를 false로 초기화 했음.)
			
						// 세션불러오기 . 발급한 인증코드를 세션에 저장시킨다. (다른 자바,jsp 에서 쓸수 있게 끔 한다.)
						HttpSession session = request.getSession();
						session.setAttribute("certificationCode", certificationCode);
						
						
					} catch (Exception e) {
						// 메일 전송이 실패한 경우 (Exception)
						e.printStackTrace();
						sendMailSuccess = false;	// 메일 전송이 실패했음을 기록한다.
					}
					
				}// end of if(isUserExist)---------------------------------
			
				// 해당 유저가 존재하는지 보자.
				request.setAttribute("isUserExist", isUserExist);	// 이 값이 false 라면, 잘못 써온 것이다.
				request.setAttribute("sendMailSuccess", sendMailSuccess);	// 메일이 정상적으로 발송됐는지 확인.
				request.setAttribute("userid", userid);		// userid 를 그대로 유지해줘야 한다.
				request.setAttribute("email", email);	
				
			}// end of if("POST".equalsIgnoreCase(method))-----------
			
			
			// form 태그의 method 가 get 인지 post 방식인지를 넘겨줘야 한다.
			request.setAttribute("method", method);
			
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/login/pwdFind.jsp");	// view 단 페이지를 보여주자. (form 태그만 보여준다.)
	}

}
