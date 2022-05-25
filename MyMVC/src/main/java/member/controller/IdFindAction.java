package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.*;
import oracle.net.aso.p;

public class IdFindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

	// form 태그에서 보낸 id 와 email 을 받아와야 한다. (POST 방식으로 보냈을 때만 해야 한다.)	

		String method = request.getMethod();	// "GET" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) {
			// 아이디 찾기 모달창에서 "찾기" 버튼을 클릭했을 경우
			// form 태그에서 POST 방식으로 보내온다면,! (form 태그를 통해 넘어옴)			
			String name = request.getParameter("name");
			String email = request.getParameter("email");

			// dao 에서 가져와야 하기 떄문에 dao 를 만든다.
			InterMemberDAO mdao = new MemberDAO();
			
			// 그냥 보내지 말고, paraMap 으로 한다.			
			Map<String, String> paraMap = new HashMap<>();
			
			// map 에 이름과 이메일을 넣고, DB 의 where 절에 보낸다.
			paraMap.put("name", name);
			paraMap.put("email", email);
			
			String userid = mdao.findUserid(paraMap);	// 이러한 id 를 받아왔다.
			
			// DAO 에서 return (돌려준) userid 가 null 이 아니라면 (존재한다면)
			if(userid != null) {
				request.setAttribute("userid", userid);		// key 값을 jsp 에 보내야 한다. (모달창에 띄워줘야 한다.)								
			}
			else {
				// key 값이 존재하지 않으면, "존재하지 않습니다." 문구를 띄운다.
				// key 값은 value 값으로 나온다.
				request.setAttribute("userid", "존재하지 않습니다.");		// key 값을 jsp 에 보내야 한다. (모달창에 띄워줘야 한다.)				
			}
			
			// form 태그에서 넘어온 이름, 이메일을 그대로 request 영역(입력)에 담아서 view 단 페이지로 보낸다.
			request.setAttribute("name", name);
			request.setAttribute("email", email);
			
		}// end of if("POST".equalsIgnoreCase(method))-----------
				
		// form 태그의 method 가 get 인지 post 방식인지를 넘겨줘야 한다.
		request.setAttribute("method", method);
		
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/idFind.jsp");	// view 단 페이지를 보여주자. (form 태그만 보여준다.)
		
	}

}
