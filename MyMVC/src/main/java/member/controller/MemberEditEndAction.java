package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;

public class MemberEditEndAction extends AbstractController {
	
	// 회원에 대한 정보를 실제 DB 에서 바꿔주겠다.
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
			// form 태그가 POST 방식으로 넘어온 것이라면!
			// 정상적인 접근경로	// where 절에는 hidden 이지만 고유한 userid 가 들어가야 한다.
			request.getParameter(method);
			
			// 나의수정정보를 입력한 페이지.jsp 에서 name 을 통해 온 값들을 다 받는다.
			 String userid = request.getParameter("userid");	// 정상적인 경로일 경우, view 단 페이지에서 다 받는다.
			 String name = request.getParameter("name"); 
	         String pwd = request.getParameter("pwd"); 
	         String email = request.getParameter("email"); 
	         String hp1 = request.getParameter("hp1"); 
	         String hp2 = request.getParameter("hp2"); 
	         String hp3 = request.getParameter("hp3"); 
	         String postcode = request.getParameter("postcode");
	         String address = request.getParameter("address"); 
	         String detailAddress = request.getParameter("detailAddress"); 
	         String extraAddress = request.getParameter("extraAddress");			
	         
	         String mobile = hp1+hp2+hp2;
	         
	         // 다음에는 VO 에 해당 값들을 넣는다.
	         // VO 에 가서 생성자를 만들자.
	         MemberVO member = new MemberVO(userid, pwd, name, email, mobile, postcode, address, detailAddress, extraAddress);
	         
	         // 위에서 생성된 것들을 DAO 로 보내자.
	         InterMemberDAO mdao = new MemberDAO();
	         int n = mdao.updateMember(member);	// 위의 member 를 DAO 로 보내자.
	         
	         String message = "";      
	         
	         // DB에 정상적으로 정보가 업데이트 되었다면 (n==1), session 정보도 바뀌어야 한다!!
	         if(n==1) {
	        	 
	        	 // *** session 에 저장된 loginuser 를 변경된 사용자의 정보값으로 변경해줘야 한다. *** //
	        	 // 1. 세션을 불러오자
	        	 HttpSession session = request.getSession();	// 로그인이 되었다.
	        	 MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");	// key값
	        	 
	        	 // userid 빼고 입력한 정보가 모두 바뀌어야 한다. (userid 는 where 절에 사용하기 위함이다.)	
	        	 loginuser.setPwd(pwd);
	        	 loginuser.setName(name);
	        	 loginuser.setEmail(email);
	        	 loginuser.setMobile(mobile);
	        	 loginuser.setPostcode(postcode);
	        	 loginuser.setAddress(extraAddress);
	        	 loginuser.setDetailaddress(detailAddress);
	        	 loginuser.setExtraaddress(extraAddress);
	        	 
	        	 // 수정이 이루어진 후 아래와 같은 메세지를 띄운다.!  수정이 완료되고 나서 팝업창이 닫혀야 한다. --> msg.jsp에서 self.close() 추가    
	        	 // 수정이 된 후 session 값이 바뀐 것이다. 그러나 수정된 후 팝업창이 닫히고 새로고침을 하지 않으면 이름이 자동으로 update 가 되지 않는다.
	        	 // 따라서 수정 후 팝업창이 닫히고 홈페이지에서 바로 바뀐 이름으로 자동 새로고침 적용되어야 한다.
	        	 // 따라서 메세지 창에 opener.location.reload(true); 를 추가한다.	// 부모창 새로고침
		         message = "회원정보 수정에 성공했습니다!";  	 
	         }
	         
	         else {
		         message = "회원정보 수정에 실패했습니다.";
			}
	         
	         String loc = "javascript:history.back()";

	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	     //  super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	         
		}
		else {
			// form 태그가 GET 방식으로 넘어온 것이라면! (POST 방식으로 넘어온 것이 아니라면)
			// 비정상적인 접근경로
			 String message = "비정상적인 경로를 통해 들어왔습니다.!!";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		
	}

}
