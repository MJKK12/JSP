package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.model.*;

public class IdDuplicateCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();	//"GET" 또는 "POST"
		// get 방식이 아니라 post 방식을 사용해야 한다.
		if("POST".equalsIgnoreCase(method)) {
			String userid = request.getParameter("userid");	// 넘어온 값을 받는다. "userid" 가 name, $("input#userid")가 값.
		//	System.out.println("확인용 userid : " + userid);	
			
			InterMemberDAO mdao = new MemberDAO();
			boolean isExist = mdao.idDuplicateCheck(userid);
			
			// 자바스크립트의 객체를 쓰자.
			JSONObject jsonObj = new JSONObject();	// {} 자바스크립트 객체로 만든다는 것이다.
			jsonObj.put("isExist", isExist);		// key값에 {"isExist":true} 또는 {"isExist":false} 로 만들어 준다.
			// 아이디 존재하면 true, 존재하지 않으면 false;
			
		 	String json = jsonObj.toString();	// 문자열 형태인 "{"isExist":true}" 또는 "{"isExist":false}" 로 만들어 준다.
		 //	System.out.println("확인용 json : " + json);	
			// 확인용 json : {"isExist":false} DB 에 저장된 id가 없을 때
		 	// 또는
		 	// 확인용 json : {"isExist":true}	 DB 에 저장된 id가 있을 때
		 	request.setAttribute("json", json);	// json 을 request 영역에 넣는다.
		 	
		 //	super.setRedirect(false);	
		 	super.setViewPage("/WEB-INF/jsonview.jsp");	// 보여줄 페이지를 만든다.
		 	
		}
	}

}
